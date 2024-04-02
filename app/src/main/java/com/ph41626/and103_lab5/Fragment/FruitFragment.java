package com.ph41626.and103_lab5.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ph41626.and103_lab5.Activity.AddFruitActivity;
import com.ph41626.and103_lab5.Activity.InventoryActivity;
import com.ph41626.and103_lab5.Activity.UpdateFruitActivity;
import com.ph41626.and103_lab5.Adapter.RecyclerViewFruitInventoryAdapter;
import com.ph41626.and103_lab5.Model.Fruit;
import com.ph41626.and103_lab5.Model.Response;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.HttpRequest;
import com.ph41626.and103_lab5.Services.Item_Fruit_Handle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FruitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FruitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FruitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FruitFragment newInstance(String param1, String param2) {
        FruitFragment fragment = new FruitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView rcv_productInventory;
    private RecyclerViewFruitInventoryAdapter fruitInventoryAdapter;
    private InventoryActivity inventoryActivity;
    private FloatingActionButton btn_addFruit;
    private HttpRequest httpRequest;

    @Override
    public void onResume() {
        super.onResume();
        httpRequest.callAPI().getListFruits().enqueue(getFruitsFromAPI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fruit, container, false);

        initUI(view);
        RecyclerViewManagement();
        AddProduct();
        return view;
    }

    private void AddProduct() {
        btn_addFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFruitActivity.class);
                intent.putExtra("distributors",inventoryActivity.listDistributors);
                startActivity(intent);
            }
        });
    }
    private void RecyclerViewManagement() {
        fruitInventoryAdapter = new RecyclerViewFruitInventoryAdapter(getContext(), null, new Item_Fruit_Handle() {
            @Override
            public void Delete(String id,String name) {
                DialogShowMessenger(id,name);
            }

            @Override
            public void Update(Fruit fruit) {
                Intent intent = new Intent(getActivity(), UpdateFruitActivity.class);
                intent.putExtra("distributors",inventoryActivity.listDistributors);
                intent.putExtra("fruit",fruit);
                startActivity(intent);
            }
        }, inventoryActivity);
        rcv_productInventory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcv_productInventory.setAdapter(fruitInventoryAdapter);
    }
    private void DialogShowMessenger (String id,String name) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete the product '" + name + "' ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpRequest.callAPI().deleteFruit(id).enqueue(deleteProduct);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    Callback<Response<ArrayList<Fruit>>> getFruitsFromAPI = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    fruitInventoryAdapter.UpdateData(response.body().getData());
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
            Log.e("Err",t.getMessage());
        }
    };
    Callback<Response<Fruit>> deleteProduct = new Callback<Response<Fruit>>() {
        @Override
        public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
            if (response.body().getStatus() == 200) {
                Toast.makeText(inventoryActivity, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                httpRequest.callAPI().getListFruits().enqueue(getFruitsFromAPI);
            }
        }

        @Override
        public void onFailure(Call<Response<Fruit>> call, Throwable t) {

        }
    };
    private void initUI(View view) {
        httpRequest = new HttpRequest();
        inventoryActivity = (InventoryActivity) getActivity();
        rcv_productInventory = view.findViewById(R.id.rcv_productInventory);
        btn_addFruit = view.findViewById(R.id.btn_addFruit);
    }
}