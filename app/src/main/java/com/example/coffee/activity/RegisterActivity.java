package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText email,password,rePassword,fullname;
    private AppCompatButton btRegister;
    private TextView login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.etEmailRegister);
        fullname = findViewById(R.id.etFullnameRegister);
        password = findViewById(R.id.etPasswordRegister);
        rePassword = findViewById(R.id.etRePasswordRegister);
        btRegister = findViewById(R.id.btRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.tvLogin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
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
                String strFullname = fullname.getText().toString().trim();
                String strPass = password.getText().toString().trim();
                String strRepass = rePassword.getText().toString().trim();
                if (TextUtils.isEmpty(strEmail))
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập email của bạn!",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(strFullname))
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập họ và tên",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(strPass))
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(strRepass))
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
                else if ( !strPass.equals(strRepass)){
                    Toast.makeText(RegisterActivity.this,"Mật khẩu không khớp ",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Vui lòng đợi");
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(strEmail,strPass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.dismiss();
                                    uploadUserProfile(strFullname);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                            finish();
                                        }
                                    },1200);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this,"Đăng ký không thành công",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void uploadUserProfile(String strFullname) {
        String uid = firebaseAuth.getUid();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("user_name",strFullname);
        hashMap.put("imgUser","");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(""+uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,"Đăng ký không thành công!",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}