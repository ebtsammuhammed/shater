package com.example.shater.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shater.R;
import com.example.shater.activity.MakeOfferActivity;
import com.example.shater.activity.ViewAcceptMyOffer;
import com.example.shater.models.receiverFromProvider;
import com.google.gson.Gson;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfferServiceAdapter extends RecyclerView.Adapter<OfferServiceAdapter.OfferServiceViewHolder> {

    private  List<receiverFromProvider> fromProviders ;
    private Context context ;

    public OfferServiceAdapter(List<receiverFromProvider> fromProviders, Context context) {
        this.fromProviders = fromProviders;
        this.context = context;
    }

    @NonNull
    @Override
    public OfferServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_item_offerservice, parent, false);
        return new OfferServiceAdapter.OfferServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferServiceViewHolder holder, int position) {

        final receiverFromProvider provider = fromProviders.get(position);
        holder.tv_priceYourOffer.setText(("Your price:"+(int) provider.getPrice()));

        if(provider.getAccept_customer()==2){
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_view.setVisibility(View.GONE);
            holder.btn_pending.setVisibility(View.VISIBLE);
        }

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String json = new Gson().toJson(provider);
                Intent intent =  new Intent(context , ViewAcceptMyOffer.class);
                intent.putExtra("reciverFromProvider",json);
                context.startActivity(intent);
            }
        });

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String json = new Gson().toJson(provider);
                Intent intent =  new Intent(context , ViewAcceptMyOffer.class);
                intent.putExtra("reciverFromProvider",json);
                context.startActivity(intent);
            }
        });

        holder.btn_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String json = new Gson().toJson(provider);
                Intent intent =  new Intent(context , MakeOfferActivity.class);
                intent.putExtra("fromCustomerActivity", false);
                intent.putExtra("reciverFromProvider",json);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return fromProviders.size();
    }

    public class OfferServiceViewHolder extends RecyclerView.ViewHolder{

        TextView tv_priceYourOffer ;
        Button btn_accept , btn_view , btn_pending;

        public OfferServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_priceYourOffer = (TextView) itemView.findViewById(R.id.tv_priceoffer);
            btn_accept = (Button) itemView.findViewById(R.id.btn_accept_myoffer);
            btn_view = (Button) itemView.findViewById(R.id.btn_view_myoffer);
            btn_pending = (Button) itemView.findViewById(R.id.btn_pending);

        }
    }
}
