package com.ph41626.and103_lab5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ph41626.and103_lab5.MainActivity;
import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.Item_Distributor_Handle;

import java.util.ArrayList;

public class RecyclerViewDistributorAdapter extends RecyclerView.Adapter<RecyclerViewDistributorAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Distributor> listDistributors;
    private MainActivity mainActivity;
    private Item_Distributor_Handle item_distributor_handle;
    public void Update(ArrayList<Distributor> distributors) {
        listDistributors = distributors;
        notifyDataSetChanged();
    }

    public RecyclerViewDistributorAdapter(Context context, ArrayList<Distributor> listDistributors, MainActivity mainActivity, Item_Distributor_Handle item_distributor_handle) {
        this.context = context;
        this.listDistributors = listDistributors;
        this.mainActivity = mainActivity;
        this.item_distributor_handle = item_distributor_handle;
    }

    @NonNull
    @Override
    public RecyclerViewDistributorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dstributor,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDistributorAdapter.ViewHolder holder, int position) {
        Distributor distributor = listDistributors.get(position);
        holder.tv_rank.setText(String.valueOf(position + 1));
        holder.tv_name.setText(distributor.getName());
        holder.btn_deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_distributor_handle.Delete(distributor);
            }
        });
        holder.btn_updateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_distributor_handle.Update(distributor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDistributors != null ? listDistributors.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_rank,tv_name;
        RelativeLayout btn_updateCategory,btn_deleteCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_name = itemView.findViewById(R.id.tv_name);
            btn_updateCategory = itemView.findViewById(R.id.btn_updateCategory);
            btn_deleteCategory = itemView.findViewById(R.id.btn_deleteCategory);
        }
    }
}
