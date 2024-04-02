package com.ph41626.and103_lab5.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ph41626.and103_lab5.Activity.InventoryActivity;
import com.ph41626.and103_lab5.Adapter.RecyclerViewDistributorInventoryAdapter;
import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Response;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.HttpRequest;
import com.ph41626.and103_lab5.Services.Item_Distributor_Handle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DistributorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DistributorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DistributorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DistributorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DistributorFragment newInstance(String param1, String param2) {
        DistributorFragment fragment = new DistributorFragment();
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
    private RecyclerView rcv_distributorInventory;
    private RecyclerViewDistributorInventoryAdapter distributorInventoryAdapter;
    private InventoryActivity inventoryActivity;
    private FloatingActionButton btnAddDistributor;
    private Dialog dialogAddDistributor;
    private HttpRequest httpRequest;
    private Distributor distributorUpdate;
    private Distributor distributorDelete;
    private String messenger = "";
    private EditText edt_name;
    private boolean typeHandle = true; //True = Add-Distributor,False = Update-Distributor
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_distributor, container, false);

        initUI(view);
        RecyclerViewManagement();
        AddDistributor();
        return view;
    }
    private void AddDistributor() {
        btnAddDistributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeHandle = true;
                OpenDialogAddDistributor();
            }
        });
    }
    private boolean ValidateDistributorForm(String name) {
        boolean isValid = true;

        if (name.isEmpty()) {
            isValid = false;
            edt_name.setError("Name is required!");
        }

        return isValid;
    }
    private void OpenDialogAddDistributor() {
        final View dialogView = View.inflate(getContext(), R.layout.dialog_add_distributor, null);
        dialogAddDistributor = new Dialog(getContext());

        dialogAddDistributor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddDistributor.setContentView(dialogView);

        Window window = dialogAddDistributor.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAddDistributor.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogAddDistributor.getWindow().setAttributes(layoutParams);

        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        ImageButton btn_back = dialogView.findViewById(R.id.btn_back);
        Button btn_addCategoryDialog = dialogView.findViewById(R.id.btn_addCategoryDialog);
        edt_name = dialogView.findViewById(R.id.edt_name);

        if (!typeHandle) {
            tv_title.setText("Update Distributor");
            btn_addCategoryDialog.setText("Update Distributor");
            edt_name.setText(distributorUpdate.getName());
        } else {
            tv_title.setText("Add Distributor");
            btn_addCategoryDialog.setText("Add Distributor");
        }


        btn_addCategoryDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                if (!ValidateDistributorForm(name)) {
                    return;
                }
                if (typeHandle) {
                    messenger = "Add Distributor Successfully!";
                    httpRequest.callAPI().addDistributor(new Distributor("",name,"","")).enqueue(addDistributor);
                } else {
                    messenger = "Update Distributor Successfully!";
                    distributorUpdate.setName(name);
                    httpRequest.callAPI().updateDistributor(distributorUpdate.getId(),distributorUpdate).enqueue(addDistributor);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddDistributor.dismiss();
            }
        });

        dialogAddDistributor.show();
    }
    Callback<Response<ArrayList<Distributor>>> getDistributorsFromAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    distributorInventoryAdapter.Update(response.body().getData());
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {

        }
    };
    Callback<Response<Distributor>> addDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getContext(), messenger, Toast.LENGTH_SHORT).show();
                    dialogAddDistributor.dismiss();
                    httpRequest.callAPI().getListDistributors().enqueue(getDistributorsFromAPI);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

        }
    };
    Callback<Response<Distributor>> checkDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() != 200) {
                    DialogShowMessengerCantDelete();
                } else {
                    messenger = "Delete Distributor Successfully!";
                    DialogShowMessenger(distributorDelete.getId(),distributorDelete.getName());
                }
            } else {
                Toast.makeText(inventoryActivity, "Err", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

        }
    };
    private void DialogShowMessenger (String id,String name) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete the distributor '" + name + "' ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpRequest.callAPI().deleteDistributor(id).enqueue(addDistributor);
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
    private void DialogShowMessengerCantDelete () {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("The following item can't be deleted because they have dependent items!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void RecyclerViewManagement() {
        distributorInventoryAdapter = new RecyclerViewDistributorInventoryAdapter(getContext(), inventoryActivity.listDistributors, new Item_Distributor_Handle() {
            @Override
            public void Delete(Distributor distributor) {
                distributorDelete = distributor;
                httpRequest.callAPI().checkDistributor(distributor.getId()).enqueue(checkDistributor);
            }

            @Override
            public void Update(Distributor distributor) {
                typeHandle = false;
                distributorUpdate = distributor;
                OpenDialogAddDistributor();
            }
        },inventoryActivity);
        rcv_distributorInventory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcv_distributorInventory.setLayoutManager(new GridLayoutManager(getContext(),1));
        rcv_distributorInventory.setAdapter(distributorInventoryAdapter);
    }
    private void initUI(View view) {
        rcv_distributorInventory = view.findViewById(R.id.rcv_distributorInventory);
        inventoryActivity = (InventoryActivity) getActivity();
        btnAddDistributor = view.findViewById(R.id.btn_addDistributor);
        httpRequest = new HttpRequest();
    }
}