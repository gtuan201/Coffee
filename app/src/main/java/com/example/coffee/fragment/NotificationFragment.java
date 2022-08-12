package com.example.coffee.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffee.R;
import com.example.coffee.activity.InformationActivity;
import com.example.coffee.activity.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class NotificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        TextView infor = view.findViewById(R.id.information_user);
        TextView address = view.findViewById(R.id.address_user);
        TextView deleteAccount = view.findViewById(R.id.delete_account);
        TextView logOut = view.findViewById(R.id.log_out);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InformationActivity.class));
            }
        });
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Chúng tôi rất lấy làm tiếc khi bạn muốn rời Tuna Coffee Shop, nhưng xin lưu ý các tài khoản đã bị xóa sẽ không được mở trở lại")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(firebaseAuth);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Bạn có muốn đăng xuất không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logOutUser(firebaseAuth);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
        return view;
    }

    private void logOutUser(FirebaseAuth firebaseAuth) {
        firebaseAuth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finishAffinity();
    }

    private void deleteUser(FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Đã xóa tài khoản",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(),LoginActivity.class));
                getActivity().finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Lỗi! Vui lòng thử lại",Toast.LENGTH_SHORT).show();
            }
        });
    }
}