package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText newPass,reNewPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AppCompatButton btBack = findViewById(R.id.btBackNewPass);
        AppCompatButton btConfirm = findViewById(R.id.btChangePass);
        newPass = findViewById(R.id.etNewPass);
        reNewPass = findViewById(R.id.etReNewPass);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        btBack.setOnClickListener(v -> onBackPressed());
        btConfirm.setOnClickListener(v -> {
            String strNewPass = newPass.getText().toString().trim();
            String strReNewPass = reNewPass.getText().toString().trim();
            if (TextUtils.isEmpty(strNewPass)){
                Toast.makeText(ChangePasswordActivity.this,"Hãy nhập mật khẩu mới",Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(strReNewPass)){
                Toast.makeText(ChangePasswordActivity.this,"Hãy nhập lại mật khẩu mới",Toast.LENGTH_SHORT).show();
            }
            else if (!strNewPass.equals(strReNewPass)){
                Toast.makeText(ChangePasswordActivity.this,"Mật khẩu không khớp!",Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                progressDialog.setMessage("Vui lòng đợi");
                progressDialog.show();
                assert user != null;
                user.updatePassword(strNewPass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ChangePasswordActivity.this,"Thay đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(ChangePasswordActivity.this, "Vui lòng đăng nhập lại để đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(ChangePasswordActivity.this,LoginActivity.class));
                                        finish();
                                    }
                                },1200);
                            }
                        });
            }
        });
    }
}