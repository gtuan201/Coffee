package com.example.coffee.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.BottomSheetShopFragment;
import com.example.coffee.R;
import com.example.coffee.activity.MainActivity;
import com.example.coffee.adapter.CartAdapter;
import com.example.coffee.model.Cart;
import com.example.coffee.model.Shop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment{
    private TextView totalPrice;
    private TextView quantityItem;
    private RecyclerView revCart;
    private LinearLayout layoutEmpty;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    // Thông tin khách hàng
    private String shopName,purchase_method,strNamePay,strPhone,strAddress,strTotalPricePayment;
    //Thông tin đơn hàng
    private String coffeeID,strImg,nameCart,sizeCart,iceCart,strQuantity,strNote,strTotalPrice;
    long quantity_item;
    BottomSheetShopFragment bottomSheetShopFragment;
    BottomSheetDialog bottomSheetDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        TextView btRemoveAll = view.findViewById(R.id.btRemoveAll);
        totalPrice = view.findViewById(R.id.tvTotalPriceInCart);
        quantityItem = view.findViewById(R.id.quantity_item);
        LinearLayout btAddMore = view.findViewById(R.id.btAdd_more_coffee);
        AppCompatButton btGoToCheckOut = view.findViewById(R.id.btGoToCheckOut);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        revCart = view.findViewById(R.id.rev_cart);
        cartList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        revCart.setLayoutManager(manager);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        reference.child(user.getUid()).child("Cart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartList.clear();
                        int sum = 0;
                        quantity_item = snapshot.getChildrenCount();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            coffeeID = ""+dataSnapshot.child("coffeeID").getValue();
                            strImg = "" + dataSnapshot.child("image").getValue();
                            nameCart = "" +dataSnapshot.child("name").getValue();
                            sizeCart = ""+dataSnapshot.child("size").getValue();
                            iceCart = ""+dataSnapshot.child("ice").getValue();
                            strQuantity = ""+dataSnapshot.child("quantity").getValue();
                            strNote = ""+dataSnapshot.child("note").getValue();
                            strTotalPrice = ""+dataSnapshot.child("totalPrice").getValue();
                            Cart cart = new Cart();
                            cart.setCoffeeID(coffeeID);
                            cart.setImgCart(strImg);
                            cart.setNameCart(nameCart);
                            cart.setSizeCart(sizeCart);
                            cart.setIceCart(iceCart);
                            cart.setQuantityCart(strQuantity);
                            cart.setNoteCart(strNote);
                            cart.setTotalPriceCart(strTotalPrice);
                            cart.setStatus("Chưa đánh giá");
                            cartList.add(cart);
                            int totalPrice = Integer.parseInt(strTotalPrice);
                            sum = sum + totalPrice;
                        }
                        if (cartList.isEmpty()){
                            layoutEmpty.setVisibility(View.VISIBLE);
                        }
                        else {
                            layoutEmpty.setVisibility(View.INVISIBLE);
                        }
                        cartAdapter = new CartAdapter(cartList,getContext());
                        revCart.setAdapter(cartAdapter);
                        revCart.setHasFixedSize(true);
                        strTotalPricePayment = String.valueOf(sum);
                        totalPrice.setText(strTotalPricePayment);
                        String strQuantityItem = String.valueOf(quantity_item);
                        quantityItem.setText(String.format("( %s món )", strQuantityItem));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        btRemoveAll.setOnClickListener(v -> openConfirmDeleteDialog(reference,user));
        btAddMore.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            assert activity != null;
            activity.toMenuFragment();
        });
        btGoToCheckOut.setOnClickListener(v -> openPaymentBottomSheet());
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    private void openPaymentBottomSheet() {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_payment,null);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        EditText namePayment = view.findViewById(R.id.namePayment);
        EditText phoneNumberPayment = view.findViewById(R.id.phoneNumberPayment);
        EditText addressPayment = view.findViewById(R.id.addressPayment);
        RadioGroup groupShip = view.findViewById(R.id.group_ship);
        TextView tvSelectedShop = view.findViewById(R.id.select_shop);
        AppCompatButton btCompletePayment = view.findViewById(R.id.btCompletePayment);
        groupShip.check(R.id.ship);
        purchase_method = "ship";
        tvSelectedShop.setVisibility(View.INVISIBLE);
        groupShip.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.ship:
                    tvSelectedShop.setVisibility(View.INVISIBLE);
                    addressPayment.setVisibility(View.VISIBLE);
                    purchase_method = "ship";
                    break;
                case R.id.pickup:
                    tvSelectedShop.setVisibility(View.VISIBLE);
                    addressPayment.setVisibility(View.INVISIBLE);
                    purchase_method = "pick up";
                    break;
            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        reference.child(user.getUid()).child("Address")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String isDefault = ""+dataSnapshot.child("isDefault").getValue();
                            if (isDefault.equals("Yes")){
                                String nameAddress = ""+dataSnapshot.child("nameAddress").getValue();
                                String phone  = ""+dataSnapshot.child("phone").getValue();
                                String address = ""+dataSnapshot.child("address").getValue();
                                namePayment.setText(nameAddress);
                                phoneNumberPayment.setText(phone);
                                addressPayment.setText(address);
                            }
                        };
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        tvSelectedShop.setOnClickListener(v -> {
            List<Shop> shopList = new ArrayList<>();
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Shop");
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    shopList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String imgShop = ""+dataSnapshot.child("ImgUrl").getValue();
                        String nameShop = ""+dataSnapshot.child("name").getValue();
                        String addressShop =""+dataSnapshot.child("address").getValue();
                        Shop shop = new Shop();
                        shop.setImgUrlShop(imgShop);
                        shop.setName(nameShop);
                        shop.setAddress(addressShop);
                        shopList.add(shop);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bottomSheetShopFragment = new BottomSheetShopFragment(shopList, shop -> {
                shopName = shop.getName();
                bottomSheetShopFragment.dismiss();
                tvSelectedShop.setText(shopName);
                tvSelectedShop.setTextColor(getResources().getColor(R.color.black));
            });
            assert getFragmentManager() != null;
            bottomSheetShopFragment.show(getFragmentManager(),bottomSheetShopFragment.getTag());
        });
        btCompletePayment.setOnClickListener(v -> {
            strNamePay = namePayment.getText().toString().trim();
            strPhone = phoneNumberPayment.getText().toString().trim();
            if (purchase_method.equals("ship")){
                strAddress = addressPayment.getText().toString().trim();
                if (TextUtils.isEmpty(strNamePay)){
                    Toast.makeText(getContext(),"Vui lòng điền tên nhận hàng !",Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(strPhone)) {

                    Toast.makeText(getContext(),"Vui lòng nhập số điện thoại nhận hàng !",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(strAddress)){
                    Toast.makeText(getContext(),"Vui lòng nhập địa chỉ giao hàng !",Toast.LENGTH_SHORT).show();
                }
                else putOrderDataToDataBase(bottomSheetDialog);
            }
            else if (purchase_method.equals("pick up")) {
                if (TextUtils.isEmpty(strNamePay)){
                    Toast.makeText(getContext(),"Vui lòng điền thông tin nhận hàng !",Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(strPhone)) {

                    Toast.makeText(getContext(),"Vui lòng nhập số điện thoại nhận hàng !",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(shopName)){
                    Toast.makeText(getContext(),"Vui lòng chọn cửa hàng đến lấy !",Toast.LENGTH_SHORT).show();
                }
                else {
                    putOrderDataToDataBase(bottomSheetDialog);
                    deleteAllCart();
                }
            }
        });
    }

    private void deleteAllCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
        assert user != null;
        reference.child(user.getUid()).child("Cart")
                .removeValue()
                .addOnSuccessListener(unused -> {
                });
    }

    private void putOrderDataToDataBase(BottomSheetDialog bottomSheetDialog) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) throw new AssertionError();
        long timestamp = System.currentTimeMillis();
        hashMap.put("id",""+timestamp);
        hashMap.put("uid",user.getUid());
        hashMap.put("fullname",strNamePay);
        hashMap.put("address",""+strAddress);
        hashMap.put("phone",strPhone);
//        hashMap.put("coffee",cartList);
        hashMap.put("purchase_method",purchase_method);
        hashMap.put("shopName",""+shopName);
        hashMap.put("totalPrice",strTotalPricePayment);
        hashMap.put("status","Đang chuẩn bị thức uống");
        hashMap.put("date",""+saveCurrentDate);
        hashMap.put("time",""+saveCurrentTime);
        hashMap.put("timeComplete","");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bill");
        reference.child("customer").child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(),"Đặt hàng thành công !",Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.cancel();
                        deleteAllCart();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Đặt hàng không thành công !",Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.cancel();
                        deleteAllCart();
                    }
                });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Order");
        reference1.child(user.getUid()).child("Cart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            coffeeID = ""+dataSnapshot.child("coffeeID").getValue();
                            strImg = "" + dataSnapshot.child("image").getValue();
                            nameCart = "" +dataSnapshot.child("name").getValue();
                            sizeCart = ""+dataSnapshot.child("size").getValue();
                            iceCart = ""+dataSnapshot.child("ice").getValue();
                            strQuantity = ""+dataSnapshot.child("quantity").getValue();
                            strNote = ""+dataSnapshot.child("note").getValue();
                            strTotalPrice = ""+dataSnapshot.child("totalPrice").getValue();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("coffeeID",coffeeID);
                            map.put("image",strImg);
                            map.put("name",nameCart);
                            map.put("size",sizeCart);
                            map.put("ice",iceCart);
                            map.put("quantity",strQuantity);
                            map.put("note",strNote);
                            map.put("totalPrice",strTotalPrice);
                            map.put("status","Chưa đánh giá");
                            reference.child("customer").child(""+timestamp).child("coffee").child(nameCart)
                                    .setValue(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void openConfirmDeleteDialog(DatabaseReference reference, FirebaseUser user) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_delete_dialog);
        Window window = dialog.getWindow();
        if (window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);
        AppCompatButton btCancel = dialog.findViewById(R.id.btCancel);
        AppCompatButton btConfirm = dialog.findViewById(R.id.btConfirm);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btConfirm.setOnClickListener(v -> reference.child(user.getUid()).child("Cart")
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Xóa thành công!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Lỗi không thể xóa!",Toast.LENGTH_SHORT).show();
                    }
                }));
        dialog.show();
    }
}