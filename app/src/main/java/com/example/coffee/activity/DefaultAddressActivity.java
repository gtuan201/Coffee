package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.adapter.AddressAdapter;
import com.example.coffee.model.Address;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DefaultAddressActivity extends AppCompatActivity {
    private TextView addNewAddress;
    private AppCompatButton btBack;
    private RecyclerView rev_address;
    private List<Address> list;
    private AddressAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaul_address);
        rev_address = findViewById(R.id.rev_address);
        addNewAddress = findViewById(R.id.addNewAddress);
        btBack = findViewById(R.id.btBackAddressDefault);
        LinearLayoutManager manager = new LinearLayoutManager(DefaultAddressActivity.this,LinearLayoutManager.VERTICAL,false);
        rev_address.setLayoutManager(manager);
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference.child(user.getUid()).child("Address")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String name = ""+dataSnapshot.child("nameAddress").getValue();
                            String phone = ""+dataSnapshot.child("phone").getValue();
                            String strAddress = ""+dataSnapshot.child("address").getValue();
                            String isDefault = ""+dataSnapshot.child("isDefault").getValue();
                            Address address = new Address();
                            address.setNameAddress(name);
                            address.setAddress(strAddress);
                            address.setPhoneAddress(phone);
                            address.setIsDefault(isDefault);
                            list.add(address);
                        }
                        adapter = new AddressAdapter(list,DefaultAddressActivity.this);
                        rev_address.setAdapter(adapter);
                        rev_address.setHasFixedSize(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        btBack.setOnClickListener(v -> onBackPressed());
        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAdd();
            }
        });
    }

    private void openDialogAdd() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_address);
        Window window = dialog.getWindow();
        if (window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);
        EditText newName = dialog.findViewById(R.id.newName);
        EditText newPhone = dialog.findViewById(R.id.newPhone);
        EditText newAddress = dialog.findViewById(R.id.newAddress);
        AppCompatButton btAddAddress = dialog.findViewById(R.id.btAddAddress);
        btAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNewName = newName.getText().toString().trim();
                String strNewPhone = newPhone.getText().toString().trim();
                String strNewAddress = newAddress.getText().toString().trim();
                if (TextUtils.isEmpty(strNewName)){
                    Toast.makeText(DefaultAddressActivity.this,"Bạn chưa nhập họ tên",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(strNewPhone)){
                    Toast.makeText(DefaultAddressActivity.this,"Bạn chưa nhập số điện thoại",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(strNewAddress)){
                    Toast.makeText(DefaultAddressActivity.this,"Bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }
                else {
                    addAddressToDB(strNewName,strNewPhone,strNewAddress,dialog);
                }
            }
        });
        dialog.show();
    }

    private void addAddressToDB(String strNewName, String strNewPhone, String strNewAddress, Dialog dialog) {
        HashMap<String,Object> hashMap = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        hashMap.put("id",""+timestamp);
        hashMap.put("nameAddress",strNewName);
        hashMap.put("phone",strNewPhone);
        hashMap.put("address",strNewAddress);
        hashMap.put("isDefault","No");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(user.getUid()).child("Address").child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(DefaultAddressActivity.this,"Đã thêm 1 địa chỉ mới",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(DefaultAddressActivity.this,"Lỗi! Vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}