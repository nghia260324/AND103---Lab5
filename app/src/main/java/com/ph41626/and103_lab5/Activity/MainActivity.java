package com.ph41626.and103_lab5.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ph41626.and103_lab5.Adapter.RecyclerViewDistributorAdapter;
import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Response;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.HttpRequest;
import com.ph41626.and103_lab5.Services.Item_Distributor_Handle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcv_distributors;
    private RecyclerViewDistributorAdapter distributorAdapter;
    private ArrayList<Distributor> listDistributors = new ArrayList<>();
    private HttpRequest httpRequest;
    private Distributor distributorDelete;
    private Distributor distributorUpdate;
    private String messenger = "";
    private Dialog dialogAddDistributor;
    private EditText edt_name;
    private boolean typeHandle = false;
    private FloatingActionButton btnAddDistributor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddDistributor = findViewById(R.id.btn_addDistributor);
        httpRequest = new HttpRequest();
        rcv_distributors = findViewById(R.id.rcv_distributors);
        distributorAdapter = new RecyclerViewDistributorAdapter(this, listDistributors, this, new Item_Distributor_Handle() {
            @Override
            public void Delete(Distributor distributor) {
                messenger = "Xóa thành công!";
                distributorDelete = distributor;
                httpRequest.callAPI().deleteDistributor(distributorDelete.getId()).enqueue(addDistributor);
            }

            @Override
            public void Update(Distributor distributor) {
                messenger = "Cập nhật thành công!";
                typeHandle = false;
                distributorUpdate = distributor;
                OpenDialogAddDistributor();
            }
        });
        rcv_distributors.setLayoutManager(new GridLayoutManager(this,1));
        rcv_distributors.setAdapter(distributorAdapter);
        httpRequest.callAPI().getListDistributors().enqueue(getDistributors);
        btnAddDistributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeHandle = true;
                OpenDialogAddDistributor();
            }
        });
    }
    Callback<Response<Distributor>> addDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(MainActivity.this, messenger, Toast.LENGTH_SHORT).show();
                    httpRequest.callAPI().getListDistributors().enqueue(getDistributors);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

        }
    };
    Callback<Response<ArrayList<Distributor>>> getDistributors = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listDistributors = response.body().getData();
                    distributorAdapter.Update(listDistributors);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.e("Err Distributor", t.getMessage());
        }
    };
    private void OpenDialogAddDistributor() {
        final View dialogView = View.inflate(this, R.layout.dialog_add_distributor, null);
        dialogAddDistributor = new Dialog(this);

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

                if (typeHandle) {
                    messenger = "Add Distributor Successfully!";
                    httpRequest.callAPI().addDistributor(new Distributor("",name,"","")).enqueue(addDistributor);
                } else {
                    messenger = "Update Distributor Successfully!";
                    distributorUpdate.setName(name);
                    httpRequest.callAPI().updateDistributor(distributorUpdate.getId(),distributorUpdate).enqueue(addDistributor);
                }
                dialogAddDistributor.dismiss();
            }
        });
        dialogAddDistributor.show();
    }
}