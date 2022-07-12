package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;

public class RegisterActivity extends AppCompatActivity {
    private EditText email,password,rePassword;
    private AppCompatButton btRegister;
    private TextView login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.etEmailRegister);
        password = findViewById(R.id.etPasswordRegister);
        rePassword = findViewById(R.id.etRePasswordRegister);
        btRegister = findViewById(R.id.btRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.tvLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = email.getText().toString().trim();
                String strPass = password.getText().toString().trim();
                String strRepass = rePassword.getText().toString().trim();
                if (TextUtils.isEmpty(strEmail))
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập email của bạn!",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(strPass))
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(strRepass))
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
                else {

                }
            }
        });
    }
}