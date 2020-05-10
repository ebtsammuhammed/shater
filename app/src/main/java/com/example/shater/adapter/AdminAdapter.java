package com.example.shater.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shater.R;
import com.example.shater.activity.ViewAccountOfAdminActivity;
import com.example.shater.models.providerInfo;
import com.google.gson.Gson;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminAdapterViewHolder> {
    Context context ;
    List<providerInfo> infos ;

    public AdminAdapter(Context context, List<providerInfo> infos) {
        this.context = context;
        this.infos = infos;
    }

    @NonNull
    @Override
    public AdminAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_item_admin, parent, false);
        return new AdminAdapter.AdminAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapterViewHolder holder, int position) {

        final providerInfo info = infos.get(position);
        holder.tv_nameProvider.setText(info.getName());
        holder.tv_cateoryProvider.setText(info.getCategory());
        holder.tv_experProvider.setText(info.getExperience()+" year");

        holder.btn_viewProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = new Gson().toJson(info);
                Intent intent = new Intent(context , ViewAccountOfAdminActivity.class);
                intent.putExtra("info" , json);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class AdminAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tv_nameProvider , tv_cateoryProvider , tv_experProvider ;
        Button btn_viewProvider ;

        public AdminAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameProvider = (TextView) itemView.findViewById(R.id.tv_nameprovider);
            tv_cateoryProvider = (TextView) itemView.findViewById(R.id.tv_catagoryprovider);
            tv_experProvider = (TextView) itemView.findViewById(R.id.tv_exper);
            btn_viewProvider = (Button) itemView.findViewById(R.id.btn_viewprovider);
        }
    }
}
