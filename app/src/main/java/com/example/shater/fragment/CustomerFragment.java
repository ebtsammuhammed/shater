package com.example.shater.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.adapter.MakeCustomerAdapter;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.providerInfo;
import com.example.shater.models.requestCustomerToService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomerFragment extends Fragment {


    private View view ;
    TextView tv_displayNoCustomerRequ;
    RecyclerView rc_customerMaker;
    RecyclerView.Adapter adapter ;
    RecyclerView.LayoutManager manager ;
    DatabaseReference reference ;
    CacheJson cacheJson =  new CacheJson();



    public CustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        tv_displayNoCustomerRequ = (TextView) view.findViewById(R.id.tv_displayCustomerRequ);
        rc_customerMaker = (RecyclerView) view.findViewById(R.id.rv_makeCustomer);
        rc_customerMaker.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext());
        rc_customerMaker.setLayoutManager(manager);
        // to get data from firebase
        getRequestCustomerFromService ();

    }

    private List<requestCustomerToService> getRequestCustomerFromService (){
        final List<requestCustomerToService> toServices = new ArrayList<requestCustomerToService>();
        // painting to tset
        String path = null;
        try {
            providerInfo info = (providerInfo) cacheJson.readObject(getContext() , "providerInfo");
            path = "Service/"+info.getCategory();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        reference = FirebaseDatabase.getInstance().getReference().child(path);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    // display list
                    tv_displayNoCustomerRequ.setVisibility(View.GONE);
                    rc_customerMaker.setVisibility(View.VISIBLE);
                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        requestCustomerToService service = Snapshot.getValue(requestCustomerToService.class);
                        if(service.isMakeOffer() == false)
                            toServices.add(service);
                    }
                    if(toServices.size() !=0)
                    rc_customerMaker.setAdapter(new MakeCustomerAdapter(getContext() ,toServices ));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "onCancelled", Toast.LENGTH_SHORT).show();
            }
        });

        return toServices;
    }

    @Override
    public void onResume() {
        super.onResume();
        getRequestCustomerFromService();
    }

    @Override
    public void onPause() {
        super.onPause();
        getRequestCustomerFromService();
    }


}
