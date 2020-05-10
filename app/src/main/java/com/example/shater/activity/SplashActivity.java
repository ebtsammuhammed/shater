package com.example.shater.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shater.R;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.receiverFromProvider;
import com.example.shater.models.userInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    Button btn_start;
    CacheJson cacheJson ;
    SharedPreferences preferences;
    receiverFromProvider  receiver ;
    final String PAY ="pay";
    final String RATE = "rate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btn_start = (Button) findViewById(R.id.btn_start);
        cacheJson = new CacheJson();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cacheJson.fileExists(SplashActivity.this, "userInfo")) {
//                    if(cacheJson.fileExists(SplashActivity.this, "reciverFromProvider")){
//                        try {
//                            receiver = (receiverFromProvider)cacheJson.readObject(SplashActivity.this , "reciverFromProvider");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
//                        preferences = getPreferences(Context.MODE_PRIVATE);
//                        boolean pay = preferences.getBoolean(PAY , false);
//                        boolean rate = preferences.getBoolean(RATE , false);
//                        if(currentDate.equals(receiver.getDate()) && pay && rate){
//                            Intent intent = new Intent(SplashActivity.this , ViewOfferProviderActivity.class);
//                            intent.putExtra("fromSplash" , true);
//                            startActivity(intent);
//
//                        }else if(currentDate.equals(receiver.getDate()) && !pay && !rate){
//                            try {
//                                cacheJson.deleteFile(SplashActivity.this ,"reciverFromProvider");
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                            startActivity(new Intent(SplashActivity.this, HomeCustomerActivity.class));
//                        }
//                        else
//                            startActivity(new Intent(SplashActivity.this, HomeCustomerActivity.class));
//
//                    }else

                    startActivity(new Intent(SplashActivity.this, HomeCustomerActivity.class));


                } else if (cacheJson.fileExists(SplashActivity.this, "providerInfo")) {
                    startActivity(new Intent(SplashActivity.this, HomeServiceActivity.class));
                    finish();
                } else{
                    startActivity(new Intent(SplashActivity.this, UserActivity.class));
                    finish();
                }


            }
        });
    }
}
