package com.ph41626.and103_lab5.Activity;


import static com.ph41626.and103_lab5.Services.Services.PICK_IMAGE_REQUEST;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ph41626.and103_lab5.Model.Response;
import com.ph41626.and103_lab5.Model.User;
import com.ph41626.and103_lab5.R;
import com.ph41626.and103_lab5.Services.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {
    private EditText edt_password,edt_confirmPassword,edt_email,edt_name;
    private ImageButton img_avatar;
    private Button btn_signUp;
    private TextView btn_goToSignIn;
    private File file;
    private HttpRequest httpRequest;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initUI();
        GoToSignIn();
        SelectedAvatar();
        SignUp();
    }
    private void SignUp() {
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                String confirmPassword = edt_confirmPassword.getText().toString().trim();
                String name = edt_name.getText().toString().trim();
                if (file == null) {
                    Toast.makeText(SignUpActivity.this, "Please choose a avatar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Please wait for seconds");
                progressDialog.setCancelable(false);
                progressDialog.show();
                CheckEmailExists(email);
            }
        });
    }
    private void SelectedAvatar() {
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
    }
    private void CheckEmailExists(String email) {
        Call<Response<Boolean>> call = httpRequest.callAPI().checkEmailExists(email);
        call.enqueue(new Callback<Response<Boolean>>() {
            @Override
            public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(SignUpActivity.this, "Email has already been registered!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"),edt_email.getText().toString().trim());
                    RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"),edt_password.getText().toString().trim());
                    RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"),edt_name.getText().toString().trim());
                    MultipartBody.Part multipartBody;
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                    multipartBody = MultipartBody.Part.createFormData("avatar",file.getName(),requestFile);
                    httpRequest.callAPI().registerAccount(_email,_password,_name,multipartBody).enqueue(registerAccount);
                }
            }

            @Override
            public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                Log.e("onFailure",t.getMessage());
                progressDialog.dismiss();
            }
        });

    }
    Callback<Response<User>> registerAccount = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    RegisterUserToFireBase(edt_email.getText().toString().trim(),edt_password.getText().toString().trim());
                } else {
                    Toast.makeText(SignUpActivity.this, "Failed! Please try again.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Failed! Please try again.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.e("onFailure",t.getMessage());
        }
    };
    private void RegisterUserToFireBase(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Thanks for signing up!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
    private void ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private File CreateFileFormUri (Uri path, String name) {
        File _file = new File(SignUpActivity.this.getCacheDir(),name + ".png");
        try {
            InputStream in = SignUpActivity.this.getContentResolver().openInputStream(path);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            file = CreateFileFormUri(imageUri,"avatar");
            Glide.with(SignUpActivity.this)
                    .load(file)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img_avatar);
        }
    }

    private void GoToSignIn() {
        btn_goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finishAffinity();
            }
        });
    }
    private void initUI() {
        FirebaseApp.initializeApp(SignUpActivity.this);
        mAuth = FirebaseAuth.getInstance();
        btn_goToSignIn = findViewById(R.id.btn_goToSignIn);
        edt_password = findViewById(R.id.edt_password);
        edt_confirmPassword = findViewById(R.id.edt_confirmPassword);
        edt_email = findViewById(R.id.edt_email);
        edt_name = findViewById(R.id.edt_name);
        img_avatar = findViewById(R.id.img_avatar);
        btn_signUp = findViewById(R.id.btn_signUp);
        httpRequest = new HttpRequest();
    }
}