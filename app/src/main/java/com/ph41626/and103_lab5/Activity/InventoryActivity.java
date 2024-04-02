package com.ph41626.and103_lab5.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ph41626.and103_lab5.Adapter.ViewPagerInventoryBottomNavigationAdapter;
import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Fruit;
import com.ph41626.and103_lab5.Model.Response;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InventoryActivity extends AppCompatActivity {

    private static final int FRAGMENT_PRODUCTS = 0;
    private static final int FRAGMENT_DISTRIBUTORS = 1;
    private int mCurrentFragment = FRAGMENT_PRODUCTS;
    private ViewPager2 viewPager2_inventory;
    private BottomNavigationView bottomNavigationView;
    public ArrayList<Fruit> listFruits = new ArrayList<>();
    public ArrayList<Distributor> listDistributors = new ArrayList<>();
    private HttpRequest httpRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_management);

        initUI();
        BottomNavigation();
        GetDataFromAPI();

    }
    public void GetDataFromAPI() {
        httpRequest.callAPI().getListDistributors().enqueue(getDistributorsFromAPI);
    }
    Callback<Response<ArrayList<Fruit>>> getFruitsFromAPI = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listFruits = response.body().getData();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
            Log.e("Err",t.getMessage());
        }
    };
    Callback<Response<ArrayList<Distributor>>> getDistributorsFromAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listDistributors = response.body().getData();
                    httpRequest.callAPI().getListFruits().enqueue(getFruitsFromAPI);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {

        }
    };
    private void BottomNavigation() {
        ViewPagerInventoryBottomNavigationAdapter bottomNavigationAdapter = new ViewPagerInventoryBottomNavigationAdapter(this);
        viewPager2_inventory.setAdapter(bottomNavigationAdapter);
        viewPager2_inventory.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0: bottomNavigationView.getMenu().findItem(R.id.products).setChecked(true);
                        break;
                    case 1: bottomNavigationView.getMenu().findItem(R.id.distributors).setChecked(true);
                        break;
                }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.products) {
                    if (mCurrentFragment != FRAGMENT_PRODUCTS) {
                        viewPager2_inventory.setCurrentItem(0);
                        mCurrentFragment = FRAGMENT_PRODUCTS;
                    }
                } else if (item.getItemId() == R.id.distributors) {
                    if (mCurrentFragment != FRAGMENT_DISTRIBUTORS) {
                        viewPager2_inventory.setCurrentItem(1);
                        mCurrentFragment = FRAGMENT_DISTRIBUTORS;
                    }
                }
                return true;
            }
        });
    }
    private void initUI() {
        httpRequest = new HttpRequest();
        viewPager2_inventory = findViewById(R.id.viewPager2_inventory);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewInventory);
    }
}