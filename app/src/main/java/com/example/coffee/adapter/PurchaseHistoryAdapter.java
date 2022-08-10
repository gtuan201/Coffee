package com.example.coffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.activity.RateActivity;
import com.example.coffee.model.Order;

import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.PurchaseHistoryViewHolder>{

    private final List<Order> list;
    private final Context context;

    public PurchaseHistoryAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PurchaseHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_purchase_history,parent,false);
        return new PurchaseHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseHistoryViewHolder holder, int position) {
        Order order = list.get(position);
        if (order == null){
            return;
        }
        holder.name.setText(order.getFullname());
        holder.sdt.setText(String.format("| SĐT: %s", order.getPhoneNumber()));
        holder.address.setText(order.getAddress());
        holder.status.setText(String.format("Trạng thái: %s", order.getStatus()));
        holder.timeComplete.setText(String.format("Thời gian đơn hàng hoàn thành là : %s .", order.getTimeCompleteOrder()));
        if (order.getPurchaseMethod().equals("ship")){
            holder.purchase_method.setText(R.string.purchase_method_ship);
            holder.img.setImageResource(R.drawable.delivery);
        }
        else if (order.getPurchaseMethod().equals("pick up")){
            holder.purchase_method.setText(R.string.purchase_method_pick_up);
            holder.img.setImageResource(R.drawable.shopcoffee);
        }
        holder.btRate.setOnClickListener(v -> {
            Intent intent = new Intent(context, RateActivity.class);
            intent.putExtra("id",order.getId());
            context.startActivities(new Intent[]{intent});
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public static class PurchaseHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,sdt,address,purchase_method,status,timeComplete;
        AppCompatButton btRate;
        public PurchaseHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_purchase_method_complete);
            name = itemView.findViewById(R.id.name_order_complete);
            sdt = itemView.findViewById(R.id.sdt_order_complete);
            address = itemView.findViewById(R.id.address_order_complete);
            purchase_method = itemView.findViewById(R.id.purchase_method_complete);
            status = itemView.findViewById(R.id.status_order_complete);
            timeComplete = itemView.findViewById(R.id.timeCompleteOrder);
            btRate = itemView.findViewById(R.id.btRate);
        }
    }
}
