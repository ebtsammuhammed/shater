package com.example.shater.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shater.R;
import com.example.shater.activity.RequestActivity;


public class ServiceFragment extends Fragment {

    private View view;
    ImageView imv_home , imv_technology , imv_electricity , imv_maintenance , imv_painting , imv_parking ;



    public ServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service, container, false) ;
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();

        imv_home = (ImageView) view.findViewById(R.id.imv_home);
        imv_technology = (ImageView) view.findViewById(R.id.imv_technology);
        imv_electricity = (ImageView) view.findViewById(R.id.imv_electricity);
        imv_maintenance = (ImageView) view.findViewById(R.id.imv_maintenance);
        imv_painting = (ImageView) view.findViewById(R.id.imv_painting);
        imv_parking = (ImageView) view.findViewById(R.id.imv_parking);


        imv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRquestPage();
            }
        });

        imv_technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRquestPage();
            }
        });

        imv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRquestPage();
            }
        });

        imv_electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRquestPage();
            }
        });

        imv_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRquestPage();
            }
        });

        imv_painting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRquestPage();
            }
        });

        imv_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRquestPage();
            }
        });

    }

    private void moveToRquestPage (){

        Intent intent = new Intent(getActivity() , RequestActivity.class);
        startActivity(intent);
        ((Activity)getActivity()).overridePendingTransition(0,0);
    }

}
