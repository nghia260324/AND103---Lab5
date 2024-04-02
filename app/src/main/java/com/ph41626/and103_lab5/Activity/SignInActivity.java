package com.ph41626.and103_lab5.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ph41626.and103_lab5.R;

public class SignInActivity extends AppCompatActivity {
    private Button btn_signIn;
    private TextView btn_goToSignUp,btn_forgotPassword;
    private EditText edt_password,edt_email;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initUI();
        GoToSignUp();
        SignIn();
        ForgotPassword();
    }
    private void ForgotPassword() {
        btn_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();

                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Please wait for seconds");
                progressDialog.setCancelable(false);
                progressDialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            DialogShowMessenger(email);
                        } else {
                            Toast.makeText(SignInActivity.this, "Failed! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void DialogShowMessenger (String email) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Please check the email address " + email +" for instruction to reset your password");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void SignIn() {
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Please wait for seconds");
                progressDialog.setCancelable(false);
                progressDialog.show();
                SignInWithFirebase(email,password);
            }
        });
    }
    private void SignInWithFirebase(String email,String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                                startActivity(new Intent(SignInActivity.this,InventoryActivity.class));
                                finishAffinity();
                        } else {
                            Toast.makeText(SignInActivity.this, "Invalid email or password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void GoToSignUp() {
        btn_goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }
    private void initUI() {
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_forgotPassword = findViewById(R.id.btn_forgotPassword);
        btn_goToSignUp = findViewById(R.id.btn_goToSignUp);

        edt_password = findViewById(R.id.edt_password);
        edt_email = findViewById(R.id.edt_email);
    }
}