package com.example.shater.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shater.R;
import com.example.shater.activity.MakeOfferActivity;
import com.example.shater.models.requestCustomerToService;
import com.google.gson.Gson;

public class MakeCustomerAdapter extends RecyclerView.Adapter<MakeCustomerAdapter.MakeCustomerAdapterViewHolder> {

    private Context context;
    private List <requestCustomerToService> customerToServiceList ;

    public MakeCustomerAdapter(Context context, List<requestCustomerToService> customerToServiceList) {
        this.context = context;
        this.customerToServiceList = customerToServiceList;
    }

    @NonNull
    @Override
    public MakeCustomerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_item_makecustomer, parent, false);
        return new MakeCustomerAdapter.MakeCustomerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakeCustomerAdapterViewHolder holder, int position) {

        final requestCustomerToService  toService = customerToServiceList.get(position);
        holder.tv_nameCust.setText(toService.getName_customer());
        holder.tv_cateory.setText(toService.getCategory());

        holder.btn_makeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = gson.toJson(toService);
                Intent intent = new Intent(context , MakeOfferActivity.class);
                intent.putExtra("fromCustomerActivity", true);
                intent.putExtra("RequestCustomer" , json);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerToServiceList.size();
    }

    public  class MakeCustomerAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tv_nameCust , tv_cateory ;
        Button btn_makeOffer ;

        public MakeCustomerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameCust = (TextView) itemView.findViewById(R.id.tv_namecust);
            tv_cateory = (TextView) itemView.findViewById(R.id.tv_catagory);
            btn_makeOffer = (Button) itemView.findViewById(R.id.btn_makeoffer);

        }
    }
}
