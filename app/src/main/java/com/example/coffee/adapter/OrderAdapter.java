package com.example.coffee.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.activity.OrderDetailActivity;
import com.example.coffee.model.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{


    private final List<Order> list;
    private final Context context;

    public OrderAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rev_infor_order,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = list.get(position);
        if (order == null){
            return;
        }
        String id = order.getId();
        String date = order.getDate();
        String time = order.getTime();
        holder.nameOrder.setText(order.getFullname());
        holder.phoneNumberOrder.setText(String.format("| SĐT: %s", order.getPhoneNumber()));
        holder.addressOrder.setText(order.getAddress());
        holder.totalPrice.setText(String.format("Thành tiền: %s VNĐ", order.getTotalprice()));
        holder.status.setText(String.format("Trạng thái: %s", order.getStatus()));
        if (order.getPurchaseMethod().equals("ship")){
            holder.purchaseMethod.setText(R.string.purchase_method_ship);
            holder.imgOrder.setImageResource(R.drawable.delivery);
        }
        else if (order.getPurchaseMethod().equals("pick up")){
            holder.purchaseMethod.setText(R.string.purchase_method_pick_up);
            holder.imgOrder.setImageResource(R.drawable.shopcoffee);
        }
        holder.btDetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context,OrderDetailActivity.class);
               intent.putExtra("id",id);
               intent.putExtra("date",date);
               intent.putExtra("time",time);
               context.startActivities(new Intent[]{intent});

            }
        });
        if (order.getStatus().equals("Đang chuẩn bị thức uống")){
            holder.btCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Bạn có muốn hủy đơn hàng không?")
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeOrder(id);
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
        }
        else {
            holder.btCancelOrder.setBackgroundResource(R.drawable.button_custom6);
        }
    }

    private void removeOrder(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bill");
        reference.child("customer").child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Đã hủy đơn hàng thành công!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Hủy đơn hàng không thành công!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView nameOrder,addressOrder,purchaseMethod,status,totalPrice,phoneNumberOrder;
        ImageView imgOrder;
        AppCompatButton btDetailOrder,btCancelOrder;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOrder = itemView.findViewById(R.id.name_order);
            addressOrder = itemView.findViewById(R.id.address_order);
            purchaseMethod = itemView.findViewById(R.id.purchase_method);
            phoneNumberOrder = itemView.findViewById(R.id.sdt_order);
            status = itemView.findViewById(R.id.status_order);
            totalPrice = itemView.findViewById(R.id.totalPriceOrder);
            imgOrder = itemView.findViewById(R.id.img_purchase_method);
            btDetailOrder = itemView.findViewById(R.id.btDetailOrder);
            btCancelOrder = itemView.findViewById(R.id.btCancelOrder);
        }
    }
}
