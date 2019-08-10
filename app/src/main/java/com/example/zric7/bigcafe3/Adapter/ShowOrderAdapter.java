package com.example.zric7.bigcafe3.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.zric7.bigcafe3.Database.ModelDB.Cart;
import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.R;
import com.example.zric7.bigcafe3.Utils.common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowOrderAdapter extends RecyclerView.Adapter<ShowOrderAdapter.ViewHolder> {
    Context context;
    List<OrderModel> orderModelList;

    public ShowOrderAdapter(Context context, List<OrderModel> vorderModelList) {
        this.context = context;
        this.orderModelList = vorderModelList;
    }

    @Override
    public ShowOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_show_order, parent, false);
        ShowOrderAdapter.ViewHolder holder = new ShowOrderAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ShowOrderAdapter.ViewHolder holder, int position) {
        OrderModel result = orderModelList.get(position);
        holder.TextViewIdOrder.setText(new StringBuilder("#ID-").append(result.getId_order()).toString());
        holder.TextViewPemesan.setText(result.getPemesan());
        holder.TextViewStatusOrder.setText(result.getStatus_order());
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

//    ================
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    //Inisiasikan view2 utk menampilkan data
    @BindView(R.id.txt_id_order) TextView TextViewIdOrder;
    @BindView(R.id.txt_vpemesan) TextView TextViewPemesan;
    @BindView(R.id.txt_detail) TextView TextViewDetail;
    @BindView(R.id.txt_status_order) TextView TextViewStatusOrder;
    @BindView(R.id.txt_total_harga) TextView TextViewTotalHarga;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
//        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        final String id_produk      = TextViewIdProduk.getText().toString();
//        final String nama           = TextViewNama.getText().toString();
//        final String foto           = TextViewFoto.getText().toString();
//        final String harga_modal    = TextViewHargaModal.getText().toString();
//        final String harga_jual     = TextViewHargaJual_polos.getText().toString();
//        final String ket            = TextViewKet.getText().toString();
//        final String kategori       = TextViewKategori.getText().toString();

    }
}
}
