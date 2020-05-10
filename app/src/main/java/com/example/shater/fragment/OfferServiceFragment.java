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
import com.example.shater.adapter.OfferServiceAdapter;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.providerInfo;
import com.example.shater.models.receiverFromProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OfferServiceFragment extends Fragment {


    private View view;
    TextView tv_displayserviceoffer ;
    RecyclerView rc_offerservice;
    RecyclerView.Adapter adapter ;
    RecyclerView.LayoutManager manager ;
    providerInfo info ;

    public OfferServiceFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_offer_service, container, false);
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        tv_displayserviceoffer = (TextView) view.findViewById(R.id.tv_displayserviceoffer);
        rc_offerservice = (RecyclerView) view.findViewById(R.id.rc_offerService);
        rc_offerservice.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext());
        rc_offerservice.setLayoutManager(manager);

        try {
            // to get data from firebase
            getMyOffer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private List<receiverFromProvider> getMyOffer() throws IOException, ClassNotFoundException{

        try {
           info = (providerInfo)new CacheJson().readObject(getContext() , "providerInfo");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        final List<receiverFromProvider> receiver = new ArrayList<>() ;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("provider/offer/"+info.getId());
        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {


                if(dataSnapshot.getValue() != null) {
                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        receiverFromProvider service = Snapshot.getValue(receiverFromProvider.class);

                        if (service.getAccept_customer() == 1 || service.getAccept_customer() == 2) {
                            tv_displayserviceoffer.setVisibility(View.GONE);
                            rc_offerservice.setVisibility(View.VISIBLE);
                            receiver.add(service);

                        }
                    }
                    if(receiver.size() !=0)
                         rc_offerservice.setAdapter(new OfferServiceAdapter(receiver , getContext()  ));
                }
                }catch (Exception e ){
                    Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "onCancelled", Toast.LENGTH_SHORT).show();
            }
        });
        return receiver ;
    }
}
