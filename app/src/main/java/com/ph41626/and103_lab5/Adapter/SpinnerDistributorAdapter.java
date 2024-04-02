package com.ph41626.and103_lab5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.R;

import java.util.ArrayList;

public class SpinnerDistributorAdapter extends ArrayAdapter<Distributor> {

    private Context context;
    private ArrayList<Distributor> listDistributors;

    TextView tv_name;

    public SpinnerDistributorAdapter(@NonNull Context context, int resource, ArrayList<Distributor> distributors) {
        super(context, resource,distributors);
        this.context = context;
        this.listDistributors = distributors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Distributor distributor = listDistributors.get(position);

        if (distributor != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(distributor.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Distributor distributor = listDistributors.get(position);

        if (distributor != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(distributor.getName());
        }

        return convertView;
    }
}
