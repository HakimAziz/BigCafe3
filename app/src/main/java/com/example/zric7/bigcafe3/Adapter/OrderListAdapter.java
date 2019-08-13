package com.example.zric7.bigcafe3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.OrderDetailActivity;
import com.example.zric7.bigcafe3.OrderMainActivity;
import com.example.zric7.bigcafe3.R;
import com.example.zric7.bigcafe3.Utils.IitemClickListner;
import com.example.zric7.bigcafe3.Utils.common;
import com.example.zric7.bigcafe3.beMenuEditActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    Context context;
    List<OrderModel> orderModelList;

    IitemClickListner iitemClickListner;

    public OrderListAdapter(Context context, List<OrderModel> vorderModelList) {
        this.context = context;
        this.orderModelList = vorderModelList;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_orderlist, parent, false);
        OrderListAdapter.ViewHolder holder = new OrderListAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
        final OrderModel result = orderModelList.get(position);
        holder.TextViewIdOrder.setText(new StringBuilder("#ID-").append(result.getId_order()).toString());
        holder.TextViewPemesan.setText(result.getPemesan());
        holder.TextViewStatusOrder.setText(result.getStatus_order());
//        holder.TextViewDetail.setText(result.getDetail());
        holder.TextViewTotalHarga.setText(result.getTotal_harga());

        holder.setIitemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                common.orderClicked = result;
                context.startActivity(new Intent(context, OrderDetailActivity.class));
            }
        });
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
//    @BindView(R.id.txt_detail) TextView TextViewDetail;
    @BindView(R.id.txt_status_order) TextView TextViewStatusOrder;
    @BindView(R.id.txt_total_harga) TextView TextViewTotalHarga;

    IitemClickListner iitemClickListner;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void setIitemClickListner(IitemClickListner iitemClickListner) {
        this.iitemClickListner = iitemClickListner;
    }

    @Override
    public void onClick(View view) {
        iitemClickListner.onClick(view);
//        final String id_order       = TextViewIdOrder.getText().toString();
//        final String pemesan        = TextViewPemesan.getText().toString();
//        final String detail         = TextViewDetail.getText().toString();
//        final String status_order   = TextViewStatusOrder.getText().toString();
//        final String total_harga    = TextViewTotalHarga.getText().toString();
//
//        Intent i = new Intent(context, OrderDetailActivity.class);
//        i.putExtra("id_order", id_order);
//        i.putExtra("pemesan", pemesan);
//        i.putExtra("detail", detail);
//        i.putExtra("status_order", status_order);
//        i.putExtra("total_harga", total_harga);
//        context.startActivity(i);

    }
}
}
