package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationActivity extends AppCompatActivity {

    private CircleImageView img;
    private EditText name;
    private TextView changePass;
    private AppCompatButton btConfirm,btBack;
    public static final int PICK_IMG = 1;
    private Uri imgUri;
    private String uploadImgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        img = findViewById(R.id.img_user);
        name = findViewById(R.id.fullname);
        TextView email = findViewById(R.id.tvEmail);
        changePass = findViewById(R.id.tvChangePassword);
        btConfirm = findViewById(R.id.btConfirmUpdateProfile);
        btBack = findViewById(R.id.btBackInfor);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String strEmail = ""+user.getEmail();
        email.setText(String.format("Email : %s", strEmail));
        reference.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String imgUrl = ""+snapshot.child("imgUser").getValue();
                        String username = ""+snapshot.child("user_name").getValue();
                        if (imgUrl.equals("")){
                            img.setImageResource(R.drawable.user);
                        }
                        else {
                            Glide.with(img).load(imgUrl).error(R.drawable.error_img).into(img);
                        }
                        name.setText(username);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        changePass.setOnClickListener(v -> startActivity(new Intent(InformationActivity.this,ChangePasswordActivity.class)));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Cập nhật hồ sơ cá nhân");
                progressDialog.setMessage("Vui lòng đợi");
                progressDialog.show();
                String strName = name.getText().toString().trim();
                uploadToRealtime(progressDialog,reference,user,strName);
            }
        });
        btBack.setOnClickListener(v -> onBackPressed());
    }
    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImgPicker();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(InformationActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    @SuppressLint("IntentReset")
    private void openImgPicker() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, PICK_IMG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMG){
            if (resultCode==RESULT_OK){
                imgUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                    img.setImageBitmap(bitmap);
                    uploadImgToDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(this,"Không có ảnh được chọn",Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImgToDB() {
        long timestamp = System.currentTimeMillis();
        String filePath = "ImgReview/"+timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePath);
        storageReference.putFile(Uri.parse(""+imgUri))
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    uploadImgUrl = ""+uriTask.getResult();
                    uploadImgToRealTime(uploadImgUrl);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InformationActivity.this,"Hãy thêm 1 tấm ảnh",Toast.LENGTH_SHORT).show();
                    }
                });
}

    private void uploadImgToRealTime(String uploadImgUrl) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("imgUser",uploadImgUrl);
        reference.child(user.getUid())
                .updateChildren(hashMap);

    }

    private void uploadToRealtime(ProgressDialog progressDialog, DatabaseReference reference, FirebaseUser user, String strName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("user_name",strName);
        reference.child(user.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(InformationActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(InformationActivity.this,"Lỗi! Vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                });

    }
}