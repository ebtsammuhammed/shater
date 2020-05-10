package com.example.shater.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shater.R;
import com.example.shater.activity.ViewOfferProviderActivity;
import com.example.shater.helper.CacheJson;
import com.example.shater.models.receiverFromProvider;
import com.example.shater.models.userInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfferCustomerAdapter extends RecyclerView.Adapter<OfferCustomerAdapter.OfferCustomerViewHolder> {

    private Context context;

    private List<receiverFromProvider>  reciver ;

    public OfferCustomerAdapter(Context context, List<receiverFromProvider> reciver) {
        this.context = context;
        this.reciver = reciver;
    }

    @NonNull
    @Override
    public OfferCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.content_item_offercustomer, parent, false);
        return new OfferCustomerAdapter.OfferCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferCustomerViewHolder holder, final int position) {

        final receiverFromProvider info = reciver.get(position);

        holder.tv_name.setText("Name: "+info.getName_provider());
        holder.tv_price.setText("Price: "+(int) info.getPrice());
        holder.rb_provider.setRating(info.getMunStartRating());

        holder.btn_accpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set boolean accept true in database firebase
                Gson gson = new Gson();
                String json = gson.toJson(info);
                Intent intent = new Intent(context , ViewOfferProviderActivity.class);
                intent.putExtra("reciverFromProvider" , json);
                context.startActivity(intent);
            }
        });

        holder.btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacheJson json = new CacheJson();
                userInfo user = null;
                try {
                  user =(userInfo) json.readObject(context , "userInfo");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                // set boolean accept false in database firebase
                setAcceptCustomerDeclineInData("provider/offer/"+info.getId_provider()+"/"+info.getId_offer());
                setAcceptCustomerDeclineInData("users/offer/"+user.getId()+"/"+info.getId_offer());
                reciver.remove(position);
                notifyItemRangeChanged(position , reciver.size());
                Toast.makeText(context, "Decline Done", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return reciver.size();
    }

    public class OfferCustomerViewHolder extends RecyclerView.ViewHolder{


        TextView tv_name , tv_price ;
        RatingBar rb_provider;
        Button btn_accpet ,btn_decline ;

        public OfferCustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name_offer);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price_offer);
            rb_provider = (RatingBar) itemView.findViewById(R.id.rb_provider_offer);
            btn_accpet = (Button) itemView.findViewById(R.id.btn_accept);
            btn_decline = (Button) itemView.findViewById(R.id.btn_decline);

        }
    }

    private void setAcceptCustomerDeclineInData (String idProvider , String idOffer){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("provider/offer/"+idProvider+"/"+idOffer);
        reference.child("accept_customer").setValue(2);

    }
    private void setAcceptCustomerDeclineInData (String path){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        reference.child("accept_customer").setValue(2);
    }
}
