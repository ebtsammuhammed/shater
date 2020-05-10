package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.models.providerInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class ViewAccountOfAdminActivity extends AppCompatActivity {

    Bundle bundle ;
    providerInfo info ;
    TextView tv_name , tv_email , tv_phone , tv_category , tv_eperience ;
    Button btn_accept , btn_refuse;
    ImageView img_cert;
    DatabaseReference reference ;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_of_admin);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // set data of provider
       info = new Gson().fromJson(getIntent().getStringExtra("info"), providerInfo.class);

        tv_name =  (TextView) findViewById(R.id.tv_name_pro);
        tv_email =  (TextView) findViewById(R.id.tv_email_pro);
        tv_phone =  (TextView) findViewById(R.id.tv_phone_pro);
        tv_category =  (TextView) findViewById(R.id.tv_catagory_pro);
        tv_eperience =  (TextView) findViewById(R.id.tv_exper_pro);
        img_cert = (ImageView) findViewById(R.id.img_cert);
        StorageReference reference =mStorageRef.child("Images/"+info.getIdImage());
        downloadImage(reference);

        // set information of provider in textview
        tv_name.setText(info.getName());
        tv_email.setText(info.getEmail());
        tv_phone.setText(info.getPhone_number());
        tv_category.setText(info.getCategory());
        tv_eperience.setText(info.getExperience());


        btn_accept = (Button) findViewById(R.id.btn_Accepte);
        btn_refuse = (Button) findViewById(R.id.btn_refuse);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeItemAcceptInDataBase (1 ,info.getId());
            }
        });

        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeItemAcceptInDataBase (2 ,info.getId());
            }
        });





    }

    private void downloadImage(StorageReference m ){
        final long ONE_MEGABYTE = 1024*1024*5 ;
        m.getBytes(ONE_MEGABYTE).addOnSuccessListener(
                new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0 , bytes.length);
                img_cert.setVisibility(View.VISIBLE);
                img_cert.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewAccountOfAdminActivity.this, "onFailure to"+e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void changeItemAcceptInDataBase(int numberAccept , String providerID) {
        reference = FirebaseDatabase.getInstance().getReference("provider/info");
        reference.child(providerID).child("accept_Admin").setValue(numberAccept);
        Toast.makeText(this, "Change accept", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ViewAccountOfAdminActivity.this , AdminActivity.class));
        finish();
    }
}
