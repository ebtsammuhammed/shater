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
import com.example.shater.fragment.OfferCustomerFragment;
import com.example.shater.fragment.ServiceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeCustomerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_customer);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment ;
                switch (item.getItemId()) {
                    case R.id.action_service:
                        fragment = new ServiceFragment();
                        loadFragment(fragment);
                        Toast.makeText(HomeCustomerActivity.this, "Customer", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_offercust:
                        fragment = new OfferCustomerFragment();
                        loadFragment(fragment);
                        Toast.makeText(HomeCustomerActivity.this, "myoffer", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_account:
                        fragment =  new AccountFragment();
                        loadFragment(fragment);
                        Toast.makeText(HomeCustomerActivity.this, "Account", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment (Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fram_customer , fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
