package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.providerInfo;
import com.example.shater.models.userInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    Intent intent ;
    String user = null ;
    Button singIn_btn;
    EditText email_edt , password_edt ;
    TextView signUp_tv;
    private FirebaseAuth auth;
    DatabaseReference reference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
      //  auth = FirebaseAuth.getInstance();

       // if (auth.getCurrentUser() != null) {
       //     startActivity(new Intent(SignInActivity.this, HomeActivity.class));
       //     finish();
       // }

        setContentView(R.layout.activity_signin);

        intent = getIntent();
        user = intent.getStringExtra("user");
        singIn_btn = (Button) findViewById(R.id.signin_btn);
        email_edt = (EditText) findViewById(R.id.email_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        signUp_tv = (TextView) findViewById(R.id.signup_tv);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        singIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = email_edt.getText().toString();
                final String password = password_edt.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(user.equals("Admin") && email.equals("shatershater19@gmail.com") && password.equals("shater1234567")){
                    startActivity(new Intent(SignInActivity.this, AdminActivity.class));
                    finish();

                }else {
                    //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        password_edt.setError("Password too short, enter minimum 6 characters!");
                                    } else {
                                        Toast.makeText(SignInActivity.this, "Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                        getDataUserFromDataBase();

                                }
                            }
                        });
                }
            }
        });


        signUp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInActivity.this , SignUpActivity.class);
                intent.putExtra("user" , user);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDataUserFromDataBase (){
        final List<userInfo> infos = new ArrayList<userInfo>();
        final List<providerInfo> providerInfos = new ArrayList<providerInfo>();
        final CacheJson cacheJson = new CacheJson();

        if(user.equals("Customer")){
            try {
                
            
        reference = FirebaseDatabase.getInstance().getReference().child("users/info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {

                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        userInfo value =  Snapshot.getValue(userInfo.class);
                        infos.add(value);
                    }
                    for (int i =0 ; i<infos.size() ; i++){

                        if( infos.get(i).getEmail().equals(email_edt.getText().toString()) &&  infos.get(i).getPassword().equals(password_edt.getText().toString())){
                            try {
                                cacheJson.writeObject(SignInActivity.this , "userInfo" , infos.get(i));

                                    startActivity(new Intent(SignInActivity.this, HomeCustomerActivity.class));
                                    finish();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(SignInActivity.this, "onCancelled"+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
            }catch (Exception e){

                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }
       
        else if(user.equals("Service Provider")){

            reference = FirebaseDatabase.getInstance().getReference().child("provider/info");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {

                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                            providerInfo value =  Snapshot.getValue(providerInfo.class);
                            providerInfos.add(value);
                        }
                        for (int i =0 ; i<providerInfos.size() ; i++){

                            if( providerInfos.get(i).getEmail().equals(email_edt.getText().toString())
                                    &&  providerInfos.get(i).getPassword().equals(password_edt.getText().toString())){
                                if(providerInfos.get(i).getAccept_Admin() == 1){
                                    try {
                                        cacheJson.writeObject(SignInActivity.this , "providerInfo" , providerInfos.get(i));

                                        startActivity(new Intent(SignInActivity.this, HomeServiceActivity.class));
                                        finish();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else if(providerInfos.get(i).getAccept_Admin() == 0){
                                    new AlertDialog.Builder(SignInActivity.this)
                                            .setMessage(" Waiting for admin permission!").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(SignInActivity.this, SplashActivity.class));
                                            finish();
                                        }
                                    }).show();
                                }else if(providerInfos.get(i).getAccept_Admin() == 2){
                                    new AlertDialog.Builder(SignInActivity.this)
                                            .setMessage(" The admin refused your request !").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(SignInActivity.this, SplashActivity.class));
                                            finish();
                                        }
                                    }).show();
                                }

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(SignInActivity.this, "onCancelled", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(SignInActivity.this, "check your email and password or sign up", Toast.LENGTH_LONG).show();

        }
    }
}
