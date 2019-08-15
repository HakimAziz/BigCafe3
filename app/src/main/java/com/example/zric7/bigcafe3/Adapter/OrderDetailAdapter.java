package com.example.zric7.bigcafe3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zric7.bigcafe3.Database.ModelDB.Cart;
import com.example.zric7.bigcafe3.Model.CartModel_toView;
import com.example.zric7.bigcafe3.OrderDetailActivity;
import com.example.zric7.bigcafe3.R;
import com.example.zric7.bigcafe3.Utils.IitemClickListner;
import com.example.zric7.bigcafe3.Utils.common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    Context context;
    List<CartModel_toView> cartList;

    public OrderDetailAdapter(Context context) {
        this.context    = context;
        this.cartList   = new Gson().fromJson(
                common.orderClicked.getDetail(),
                new TypeToken<List<CartModel_toView>>(){}.getType()
        );
    }

    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_orderdetail, parent, false);
        OrderDetailAdapter.ViewHolder holder = new OrderDetailAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderDetailAdapter.ViewHolder holder, int position) {
//        final Cart result = cartList.get(position);
        holder.TextViewId.setText(new StringBuilder("#ID-").append(cartList.get(position).getId()).toString());
        holder.TextViewNama.setText(cartList.get(position).getName());
        holder.TextViewPriceItem.setText(new StringBuilder("@Rp ").append(cartList.get(position).getPrice_item()).append(" x "));
        holder.TextViewPriceTotal.setText(new StringBuilder("Rp ").append(cartList.get(position).getPrice_total()));
        holder.TextViewQty.setText(String.valueOf(cartList.get(position).getQty()));


        if (cartList.get(position).getFoto().isEmpty()) {
            Picasso.get()
                    .load(R.drawable.img_holder)
                    .into(holder.ImageViewFoto);
        } else{
            Picasso.get()
                    .load(cartList.get(position).getFoto())
                    .into(holder.ImageViewFoto);
        }

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

//    ================

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Inisiasikan view2 utk menampilkan data
        @BindView(R.id.txt_id) TextView TextViewId;
        @BindView(R.id.txt_nama) TextView TextViewNama;
        @BindView(R.id.txt_price_item) TextView TextViewPriceItem;
        @BindView(R.id.txt_price_total) TextView TextViewPriceTotal;
        @BindView(R.id.img_foto) ImageView ImageViewFoto;
        @BindView(R.id.txt_qty) TextView TextViewQty;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
        }
    }
}
