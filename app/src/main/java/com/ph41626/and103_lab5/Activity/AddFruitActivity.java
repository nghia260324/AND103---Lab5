package com.ph41626.and103_lab5.Activity;

import static com.ph41626.and103_lab5.Services.Services.PICK_IMAGE_REQUEST;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ph41626.and103_lab5.Adapter.SpinnerDistributorAdapter;
import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Fruit;
import com.ph41626.and103_lab5.Model.Response;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddFruitActivity extends AppCompatActivity {
    private EditText edt_name,edt_price,edt_description,edt_quantity;
    private RadioButton rdo_available,rdo_soldOut;
    private Spinner spinner_category,spinner_distributor;
    private ImageButton img_thumbnail;
    private ImageButton btn_back;
    private Button btn_addProduct;
    public ArrayList<Distributor> listDistributors = new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
        GetDataFromInventoryActivity();
    }
    private SpinnerDistributorAdapter spinnerDistributorAdapter;
    private String idDistributorSelected;
    private File file;
    private HttpRequest httpRequest;
    private void GetDataFromInventoryActivity() {
        Intent intent = getIntent();
        listDistributors = (ArrayList<Distributor>) intent.getSerializableExtra("distributors");

        spinnerDistributorAdapter = new SpinnerDistributorAdapter(this, R.layout.item_view_spinner,listDistributors);
        spinner_distributor.setAdapter(spinnerDistributorAdapter);
        spinner_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idDistributorSelected = listDistributors.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fruit);

        initUI();
        CloseForm();
        AddProduct();
        SelectedThumbnail();
    }
    private void AddProduct() {
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                String quantity = edt_quantity.getText().toString().trim();
                String price = edt_price.getText().toString().trim();
                int status = rdo_available.isChecked() ? 0 : 1;
                String description = edt_description.getText().toString().trim();

                if (file == null) {
                    Toast.makeText(AddFruitActivity.this, "Thumbnail is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
                RequestBody _quantity = RequestBody.create(MediaType.parse("multipart/form-data"),quantity);
                RequestBody _price = RequestBody.create(MediaType.parse("multipart/form-data"),price);
                RequestBody _status = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(status));
                RequestBody _description = RequestBody.create(MediaType.parse("multipart/form-data"),description);
                RequestBody _id_distributor = RequestBody.create(MediaType.parse("multipart/form-data"),idDistributorSelected);

                MultipartBody.Part multipartBody;
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                multipartBody = MultipartBody.Part.createFormData("thumbnail",file.getName(),requestFile);
                httpRequest.callAPI().addFruit(
                        _name,
                        _quantity,
                        _price,
                        _status,
                        multipartBody,
                        _description,
                        _id_distributor).enqueue(addFruit);
            }
        });
    }
    private void SelectedThumbnail() {
        img_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
    }
    private void CloseForm() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    Callback<Response<Fruit>> addFruit = new Callback<Response<Fruit>>() {
        @Override
        public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
            if(response.body().getStatus() == 200) {
                Toast.makeText(AddFruitActivity.this, "Add Fruit Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onFailure(Call<Response<Fruit>> call, Throwable t) {

        }
    };
    private void ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            file = CreateFileFormUri(imageUri,"fruit");
            Glide.with(AddFruitActivity.this)
                    .load(file)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img_thumbnail);
        }
    }
    private File CreateFileFormUri (Uri path, String name) {
        File _file = new File(AddFruitActivity.this.getCacheDir(),name + ".png");
        try {
            InputStream in = AddFruitActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len=in.read(buf)) > 0) {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
            return _file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void initUI() {
        rdo_available = findViewById(R.id.rdo_available);
        rdo_soldOut = findViewById(R.id.rdo_soldOut);
        edt_name = findViewById(R.id.edt_name);
        edt_quantity = findViewById(R.id.edt_quantity);
        edt_price = findViewById(R.id.edt_price);
        edt_description = findViewById(R.id.edt_description);
        spinner_distributor = findViewById(R.id.spinner_distributor);
        img_thumbnail = findViewById(R.id.img_thumbnail);
        btn_back = findViewById(R.id.btn_back);
        btn_addProduct = findViewById(R.id.btn_addFruit);
        httpRequest = new HttpRequest();
    }
}