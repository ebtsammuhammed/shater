package com.example.shater.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.Cache;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.receiverFromProvider;
import com.example.shater.models.userInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;

public class CraditActivity extends AppCompatActivity {

    Button btn_submit ;
    receiverFromProvider reciver ;
    DatabaseReference reference ;
    SharedPreferences preferences ;
    SharedPreferences.Editor editor ;
    final String PAY ="pay";
    userInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cradit);

        reference = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        preferences = getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        reciver = gson.fromJson(getIntent().getStringExtra("reciverFromProvider"), receiverFromProvider.class);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPaymentAcceptInDatabase("provider/offer/"+reciver.getId_provider()+"/"+reciver.getId_offer());
                setPaymentAcceptInDatabase("users/offer/"+reciver.getId_customer()+"/"+reciver.getId_offer());
                editor.putBoolean("pay" , true);
                editor.commit();
                Toast.makeText(CraditActivity.this, "Payment Done", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CraditActivity.this , ViewOfferProviderActivity.class));
                finish();
            }
        });
    }

    private void setPaymentAcceptInDatabase (String path ){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        reference.child("pay_customer").setValue(1);

    }
}
