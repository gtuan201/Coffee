package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private AppCompatButton btLogin;
    private TextView tvRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.etEmailLogin);
        password = findViewById(R.id.etPasswordLogin);
        btLogin = findViewById(R.id.btLogin);
        tvRegister = findViewById(R.id.tvRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmailLogin = email.getText().toString().trim();
                String strPasswordLogin = password.getText().toString().trim();
                if (TextUtils.isEmpty(strEmailLogin)){
                    Toast.makeText(LoginActivity.this,"Vui lòng nhập email",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(strPasswordLogin)){
                    Toast.makeText(LoginActivity.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Vui lòng đợi!");
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(strEmailLogin,strPasswordLogin)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.dismiss();
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    assert user != null;
                                    if (user.isEmailVerified()){
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        finish();
                                    }
                                    else {
                                        user.sendEmailVerification();
                                        Toast.makeText(LoginActivity.this,"Vui lòng xác thực email của bạn",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}