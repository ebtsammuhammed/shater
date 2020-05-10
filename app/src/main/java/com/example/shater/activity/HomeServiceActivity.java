package com.example.shater.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.fragment.AccountFragment;
import com.example.shater.fragment.CustomerFragment;
import com.example.shater.fragment.OfferServiceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeServiceActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_service);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_service);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment ;
                switch (item.getItemId()) {
                    case R.id.action_customer:
                        fragment = new CustomerFragment();
                        loadFragment(fragment);
                        Toast.makeText(HomeServiceActivity.this, "Customer", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_myoffer:
                        fragment = new OfferServiceFragment();
                        loadFragment(fragment);
                        Toast.makeText(HomeServiceActivity.this, "myoffer", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_account:
                        fragment =  new AccountFragment();
                        loadFragment(fragment);
                        Toast.makeText(HomeServiceActivity.this, "Account", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment (Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fram_service , fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
