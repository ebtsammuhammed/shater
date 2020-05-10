package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.providerInfo;
import com.example.shater.models.userInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    Intent intent ;
    CacheJson cacheJson ;
    Button signUp_btn , btn_setimage;
    ImageView img_set;
    EditText name_edt, email_edt , password_edt, phone_edt , exper_edt  ;
    Spinner sp_categrayProvider ;
    String catogry ,experience = null;
    DatabaseReference Database ;
    private StorageReference mStorageRef;
    private StorageTask task ;

    public Uri imageUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        auth = FirebaseAuth.getInstance();
        intent = getIntent();
        final String user = intent.getStringExtra("user");
        Database = FirebaseDatabase.getInstance().getReference();
        cacheJson = new CacheJson();

        btn_setimage = (Button) findViewById(R.id.setImage_btn);
        img_set = (ImageView) findViewById(R.id.img_set);
        signUp_btn = (Button) findViewById(R.id.signup_btn);
        name_edt = (EditText) findViewById(R.id.name_edt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        exper_edt = (EditText) findViewById(R.id.exper_edt);
        sp_categrayProvider = (Spinner) findViewById(R.id.sp_categrayProvider);

        if(user.equals("Service Provider")){
            exper_edt.setVisibility(View.VISIBLE);
            sp_categrayProvider.setVisibility(View.VISIBLE);
            btn_setimage.setVisibility(View.VISIBLE);
            final String []category = {"Home decor","Technology","Repair","Painting","Parking shades","Electricity" ,"A/C Maintenance" , "Plumping",
                    "Carpentry", "Moving Furniture" , "Floor tiling"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,category);
            sp_categrayProvider.setAdapter(adapter);
            sp_categrayProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    catogry = (String)adapterView.getItemAtPosition(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        btn_setimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosImage();
            }
        });



        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String name = name_edt.getText().toString().trim();
                final String email = email_edt.getText().toString().trim();
                final String password = password_edt.getText().toString().trim();
                final String phone = phone_edt.getText().toString().trim();

                if (user.equals("Service Provider")) {
                    experience = exper_edt.getText().toString().trim();


                }

                //set information of user

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //create user

                if (!user.equals("Admin")){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                if (user.equals("Customer")){

                                    String userId = Database.push().getKey();
                                    userInfo infos = new userInfo(name, email, password, user, phone, userId);
                                    // pushing user to 'users' node using the userId
                                    sendDataToFirebase("users" , infos , userId);
                                    // set data in Cache class
                                    try {
                                        cacheJson.writeObject(SignUpActivity.this, "userInfo", infos);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    // move to home customer
                                    startActivity(new Intent(SignUpActivity.this, HomeCustomerActivity.class));
                                    finish();
                                }
                                else if (user.equals("Service Provider")){

                                    String Id = Database.push().getKey();
                                    String imageId = System.currentTimeMillis()+"."+getExtension(imageUri) ;

                                    providerInfo info = new providerInfo();
                                    info.setName(name);
                                    info.setEmail(email);
                                    info.setPassword(password);
                                    info.setUser(user);
                                    info.setPhone_number(phone);
                                    info.setId(Id);
                                    info.setCategory(catogry);
                                    info.setExperience(experience);
                                    info.setAccept_Admin(0);
                                    info.setStartRate(0);
                                    info.setIdImage(imageId);
                                    sendDataToFirebase("provider", info , Id);
                                    // upload image in firebasStorage
                                    StorageReference reference = mStorageRef.child(imageId);
                                    reference.putFile(imageUri)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // Get a URL to the uploaded content
                                                    // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                    Toast.makeText(SignUpActivity.this, "image upload Successfully ", Toast.LENGTH_SHORT).show();
                                                    new AlertDialog.Builder(SignUpActivity.this).setTitle("SignUp Done.")
                                                            .setMessage("the Sign up is Done waiting for admin permission  ").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            startActivity(new Intent(SignUpActivity.this, SplashActivity.class));
                                                            finish();
                                                        }
                                                    }).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(SignUpActivity.this, "image upload Failure "+exception, Toast.LENGTH_SHORT).show();

                                                }
                                            });




                                }

                            }
                        }
                    });

            }else {
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                }

            }
        });
    }

    // Get Imag from your device
    private void choosImage (){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , 1);
    }

    private String getExtension(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            img_set.setVisibility(View.VISIBLE);
            img_set.setImageURI(imageUri);
        }
    }

    private void sendDataToFirebase(String nameNode , Object object , String id){

        Database =  FirebaseDatabase.getInstance().getReference(nameNode).child("info");
        //String userId = Database.push().getKey();
        Database.child(id).setValue(object);

    }
}

