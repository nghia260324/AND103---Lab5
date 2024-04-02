package com.ph41626.and103_lab5.Adapter;

import static com.ph41626.and103_lab5.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_lab5.Services.Services.findObjectById;
import static com.ph41626.and103_lab5.Services.Services.formatPrice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph41626.and103_lab5.Activity.InventoryActivity;
import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Fruit;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.Item_Fruit_Handle;

import java.util.ArrayList;

public class RecyclerViewFruitInventoryAdapter extends RecyclerView.Adapter<RecyclerViewFruitInventoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Fruit> listFruits;
    private InventoryActivity inventoryActivity;
    private Item_Fruit_Handle item_fruit_handle;
    public void UpdateData(ArrayList<Fruit> fruits) {
        listFruits = fruits;
        notifyDataSetChanged();
    }
    public RecyclerViewFruitInventoryAdapter(Context context, ArrayList<Fruit> listProducts, Item_Fruit_Handle fruit_handle, InventoryActivity inventoryActivity) {
        this.context = context;
        this.listFruits = listProducts;
        this.item_fruit_handle = fruit_handle;
        this.inventoryActivity = inventoryActivity;
    }

    @NonNull
    @Override
    public RecyclerViewFruitInventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fruit_inventory,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewFruitInventoryAdapter.ViewHolder holder, int position) {
        Fruit fruit = listFruits.get(position);
        Distributor distributor = findObjectById(inventoryActivity.listDistributors,fruit.getId_distributor());
        holder.tv_name.setText(fruit.getName());
        holder.tv_price.setText("Price: " + formatPrice(fruit.getPrice(),"₫"));
        holder.tv_description.setText("Description: " + fruit.getDescription());
        holder.tv_distributor.setText(distributor.getName());
        Glide.with(context).load(convertLocalhostToIpAddress(fruit.getThumbnail())).into(holder.img_thumbnail);

        holder.btn_deleteFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_fruit_handle.Delete(fruit.get_id(),fruit.getName());
            }
        });
        holder.btn_updateFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_fruit_handle.Update(fruit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFruits != null ? listFruits.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_thumbnail;
        TextView tv_name,tv_price,tv_distributor,tv_description;
        RelativeLayout btn_deleteFruit, btn_updateFruit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_distributor = itemView.findViewById(R.id.tv_distributor);
            tv_description = itemView.findViewById(R.id.tv_description);
            btn_updateFruit = itemView.findViewById(R.id.btn_updateFruit);
            btn_deleteFruit = itemView.findViewById(R.id.btn_deleteFruit);
        }
    }
}
