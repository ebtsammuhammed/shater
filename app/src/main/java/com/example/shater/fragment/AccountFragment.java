package com.example.shater.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shater.R;
import com.example.shater.activity.RequestActivity;
import com.example.shater.activity.SplashActivity;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.providerInfo;
import com.example.shater.models.userInfo;

import java.io.FileNotFoundException;
import java.io.IOException;


public class AccountFragment extends Fragment {

    private View view;
    ImageView img_account;
    Button btn_logout ;
    TextView tv_nameAccount , tv_phoneAccount ;
    CacheJson cacheJson ;
    userInfo info ;
    providerInfo infos ;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_account, container, false) ;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cacheJson = new CacheJson();

        img_account = (ImageView) view.findViewById(R.id.img_account);
        tv_nameAccount = (TextView) view.findViewById(R.id.tv_name_acc);
        tv_phoneAccount = (TextView) view.findViewById(R.id.tv_phone_acc);
        try {
            // set data in textview
            if(cacheJson.fileExists(getContext() , "userInfo")){
                info = (userInfo) cacheJson.readObject(getContext() , "userInfo");
                img_account.setImageResource(R.mipmap.customer_used);
                tv_nameAccount.setText(info.getName());
                tv_phoneAccount.setText(info.getPhone_number());
            }else if (cacheJson.fileExists(getContext() , "providerInfo")){
                infos = (providerInfo) cacheJson.readObject(getContext() , "providerInfo");
                img_account.setImageResource(R.mipmap.provider_used);
                tv_nameAccount.setText(infos.getName());
                tv_phoneAccount.setText(infos.getPhone_number());
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        btn_logout  = (Button) view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(cacheJson.fileExists(getContext() , "userInfo")){
                        cacheJson.deleteFile(getContext() , "userInfo");
                        Intent intent = new Intent(getActivity() , SplashActivity.class);
                        startActivity(intent);
                        ((Activity)getActivity()).overridePendingTransition(0,0);
                    }else if (cacheJson.fileExists(getContext() , "providerInfo")){
                        cacheJson.deleteFile(getContext() , "providerInfo");
                        Intent intent = new Intent(getActivity() , SplashActivity.class);
                        startActivity(intent);
                        ((Activity)getActivity()).overridePendingTransition(0,0);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
