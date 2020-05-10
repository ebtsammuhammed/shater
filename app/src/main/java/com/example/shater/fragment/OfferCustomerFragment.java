package com.example.shater.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.adapter.OfferCustomerAdapter;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.receiverFromProvider;
import com.example.shater.models.userInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OfferCustomerFragment extends Fragment {
    private View view;
    ImageView btn_sort;
    TextView tv_displaycustomeroffer ;
    RecyclerView rc_offerCust;
    RecyclerView.Adapter adapter ;
    RecyclerView.LayoutManager manager ;
    private int sortItem = 0;
    List<receiverFromProvider> reciver ;


    public OfferCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_offer_customer, container, false);
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();

        tv_displaycustomeroffer = (TextView) view.findViewById(R.id.tv_displaycustomeroffer);
        btn_sort = (ImageView) view.findViewById(R.id.btn_sort);

        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortItem = sortData();
                if(reciver.size()!= 0){
                    if(sortItem == 1){
                        Collections.sort(reciver , receiverFromProvider.ByPrice);
                        Collections.reverse(reciver);
                        adapter.notifyDataSetChanged();
                    }
                    if(sortItem == 2){
                        Collections.sort(reciver , receiverFromProvider.ByRating);
                        adapter.notifyDataSetChanged();
                    }
                    if(sortItem == 3){
                        Collections.sort(reciver , receiverFromProvider.ByExperince);
                        adapter.notifyDataSetChanged();

                    }
                }

            }
        });
        rc_offerCust = (RecyclerView) view.findViewById(R.id.rc_offerCust);
        rc_offerCust.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext());
        rc_offerCust.setLayoutManager(manager);

        try {
         reciverFromProviders();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        if(offerCustomerInfos.size() != 0)
//        rc_offerCust.setAdapter(new OfferCustomerAdapter(getContext() ,offerCustomerInfos ));
    }

    private List<receiverFromProvider> reciverFromProviders () throws IOException, ClassNotFoundException {

        userInfo info =(userInfo) new CacheJson().readObject(getContext() , "userInfo");

        reciver = new ArrayList<receiverFromProvider>();

        DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("users/offer/"+info.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){

                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {

                        receiverFromProvider service =Snapshot.getValue(receiverFromProvider.class);

                        if(service.getAccept_customer()==0){
                            tv_displaycustomeroffer.setVisibility(View.GONE);
                            rc_offerCust.setVisibility(View.VISIBLE);
                            btn_sort.setVisibility(View.VISIBLE);
                            reciver.add(service);
                        }
                    }
                    if(reciver.size() !=0){
                        adapter = new OfferCustomerAdapter(getContext() ,reciver ) ;
                        rc_offerCust.setAdapter(adapter);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "onCancelled", Toast.LENGTH_SHORT).show();
            }
        });

        return reciver ;
    }

    private int sortData(){

        AlertDialog.Builder  builder =new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Sort");
        String [] data = {"Lowerst price" , "Most Rating", "Most Experinced"};
        builder.setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {

                switch (item){
                    case 0:
                        sortItem = 1;
                        break;
                    case 1:
                        sortItem = 2;
                        break;
                    case 2:
                        sortItem = 3;
                        break;
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return sortItem;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            reciverFromProviders();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            reciverFromProviders();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
