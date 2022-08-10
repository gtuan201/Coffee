package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class RatingActivity extends AppCompatActivity {
    private ImageView imgCoffee,imgReview;
    private TextView nameCoffee, sizeCoffee,iceCoffee,quantityCoffee;
    private AppCompatRatingBar ratingBar;
    private EditText stringRate;
    private AppCompatButton btAddImg,btBack,btReview;
    private CheckBox cb1,cb2,cb3,cb4,cb5;
    private String strCb = "",uploadImgUrl,strReview,name;
    private Uri imgUri;
    private float rate;
    public static final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        imgCoffee = findViewById(R.id.imgCoffeeRating);
        nameCoffee = findViewById(R.id.nameCoffeeRating);
        sizeCoffee = findViewById(R.id.sizeRating);
        iceCoffee = findViewById(R.id.iceRating);
        quantityCoffee = findViewById(R.id.quantityRating);
        ratingBar = findViewById(R.id.ratingbar);
        stringRate = findViewById(R.id.etRating);
        imgReview = findViewById(R.id.imgReview);
        btAddImg = findViewById(R.id.btAddImg);
        btReview = findViewById(R.id.btReview);
        btBack = findViewById(R.id.btBackRating);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        String img = intent.getStringExtra("imgUrl");
        String size = intent.getStringExtra("size");
        String ice = intent.getStringExtra("ice");
        String quantity = intent.getStringExtra("quantity");
        nameCoffee.setText(name);
        Glide.with(imgCoffee).load(img).error(R.drawable.error_img).into(imgCoffee);
        sizeCoffee.setText(String.format("Size : %s | ", size));
        iceCoffee.setText(String.format("Phần trăm đá :%s", ice));
        quantityCoffee.setText(String.format("Số lượng : %s", quantity));
        btBack.setOnClickListener(v -> onBackPressed());
        setTextCheckBox();
        btAddImg.setOnClickListener(v -> requestPermission());
        btReview.setOnClickListener(v -> {
            rate  = ratingBar.getRating();
            strReview = stringRate.getText().toString().trim();
            if (rate  < 1){
                Toast.makeText(RatingActivity.this,"Hãy đánh giá chất lượng coffee!",Toast.LENGTH_SHORT).show();
            }
            else {
                uploadImgToDB();
            }
        });

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
                    uploadToRealtime(uploadImgUrl);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RatingActivity.this,"Hãy thêm 1 tấm ảnh",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadToRealtime(String uploadImgUrl) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        ref.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName = ""+snapshot.child("user_name").getValue();
                        String imgUser = ""+snapshot.child("imgUser").getValue();
                        long timestamp = System.currentTimeMillis();
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("id",""+timestamp);
                        hashMap.put("uid",user.getUid());
                        hashMap.put("username",userName);
                        hashMap.put("imgUser",""+imgUser);
                        hashMap.put("rate",String.valueOf(rate));
                        hashMap.put("review",""+strReview);
                        hashMap.put("imgCoffeeReview",""+uploadImgUrl);
                        hashMap.put("time",saveCurrentTime);
                        hashMap.put("date",saveCurrentDate);
                        reference.child(name).child("Review").child(""+timestamp)
                                .setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(RatingActivity.this,CompleteRatingActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RatingActivity.this,"Lỗi! Không gửi được đánh giá",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImgPicker();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(RatingActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE){
            if (resultCode==RESULT_OK){
                imgUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                    imgReview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(this,"Không có ảnh được chọn",Toast.LENGTH_SHORT).show();
        }
    }

    private void setTextCheckBox() {
        String strCb1 = cb1.getText().toString().trim();
        String strCb2 = cb2.getText().toString().trim();
        String strCb3 = cb3.getText().toString().trim();
        String strCb4 = cb4.getText().toString().trim();
        String strCb5 = cb5.getText().toString().trim();
        cb1.setOnClickListener(v -> {
            if (cb1.isChecked()){
                strCb = strCb.replaceAll(strCb1 +" {2}","");
                strCb = strCb + strCb1 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb1+" {2}","");
            }
            stringRate.setText(strCb);
        });
        cb2.setOnClickListener(v -> {
            if (cb2.isChecked()){
                strCb = strCb.replaceAll(strCb2 +" {2}","");
                strCb = strCb + strCb2 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb2+" {2}","");
            }
            stringRate.setText(strCb);
        });
        cb3.setOnClickListener(v -> {
            if (cb3.isChecked()){
                strCb = strCb.replaceAll(strCb3 +" {2}","");
                strCb = strCb + strCb3 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb3+"  ","");
            }
            stringRate.setText(strCb);
        });
        cb4.setOnClickListener(v -> {
            if (cb4.isChecked()){
                strCb = strCb.replaceAll(strCb4 +" {2}","");
                strCb = strCb + strCb4 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb4+"  ","");
            }
            stringRate.setText(strCb);
        });
        cb5.setOnClickListener(v -> {
            if (cb5.isChecked()){
                strCb = strCb.replaceAll(strCb5 +" {2}","");
                strCb = strCb + strCb5 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb5+"  ","");
            }
            stringRate.setText(strCb);
        });
    }
}