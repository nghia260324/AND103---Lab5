package com.ph41626.and103_lab5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ph41626.and103_lab5.Activity.InventoryActivity;
import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.Item_Distributor_Handle;

import java.util.ArrayList;

public class RecyclerViewDistributorInventoryAdapter extends RecyclerView.Adapter<RecyclerViewDistributorInventoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Distributor> listDistributors;
    private InventoryActivity inventoryActivity;
    private Item_Distributor_Handle item_distributor_handle;
    public void Update(ArrayList<Distributor> distributors) {
        listDistributors = distributors;
        notifyDataSetChanged();
    }
    public RecyclerViewDistributorInventoryAdapter(Context context, ArrayList<Distributor> listDistributors, Item_Distributor_Handle distributor_handle, InventoryActivity inventoryActivity) {
        this.context = context;
        this.listDistributors = listDistributors;
        this.item_distributor_handle = distributor_handle;
        this.inventoryActivity = inventoryActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distributor,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
