package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.adapter.AdminAdapter;
import com.example.shater.models.providerInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    String API = "AIzaSyA1T1sB0syevZxTNiUel_SxjnnZsxV5wx4";

    TextView tv_displayNoProvider;
    RecyclerView rc_Admin;
    RecyclerView.Adapter adapter ;
    RecyclerView.LayoutManager manager ;
    DatabaseReference reference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        tv_displayNoProvider = (TextView)findViewById(R.id.tv_displayProvider);
        rc_Admin = (RecyclerView) findViewById(R.id.tv_admin);
        rc_Admin.setHasFixedSize(true);
        manager = new LinearLayoutManager(AdminActivity.this);
        rc_Admin.setLayoutManager(manager);
        List<providerInfo> providerInfos = getSignUpOfProvider ();
        rc_Admin.setAdapter(new AdminAdapter(AdminActivity.this ,providerInfos ));
    }

    private List<providerInfo> getSignUpOfProvider() {
        final List<providerInfo> infos = new ArrayList<providerInfo>();
        reference = FirebaseDatabase.getInstance().getReference().child("provider/info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {


                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        providerInfo info = Snapshot.getValue(providerInfo.class);
                        if(info.getAccept_Admin()==0){
                            // display list
                            tv_displayNoProvider.setVisibility(View.GONE);
                            rc_Admin.setVisibility(View.VISIBLE);
                            infos.add(info);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(AdminActivity.this, "onCancelled", Toast.LENGTH_SHORT).show();
            }
        });
        return infos ;
    }
}
