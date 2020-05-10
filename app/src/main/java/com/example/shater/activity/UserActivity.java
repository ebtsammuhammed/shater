package com.example.shater.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shater.R;

public class UserActivity extends AppCompatActivity {

    Button provider_btn , customer_btn ;
    TextView tv_admin;
   // userInfo info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

      //  info = new userInfo();
        provider_btn = (Button) findViewById(R.id.provider_btn);
        customer_btn = (Button) findViewById(R.id.cutomer_btn);
        tv_admin = (TextView) findViewById(R.id.tv_admin);

        provider_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // info.setUser("Service Provider");
               Intent intent = new Intent(UserActivity.this , SignInActivity.class);
               intent.putExtra("user" , "Service Provider");
               startActivity(intent);
               finish();
            }
        });

        customer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //info.setUser("customer");
                Intent intent = new Intent(UserActivity.this , SignInActivity.class);
                intent.putExtra("user" , "Customer");
                startActivity(intent);
                finish();
            }
        });

        tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this , SignInActivity.class);
                intent.putExtra("user" , "Admin");
                startActivity(intent);
                finish();
            }
        });
    }
}
