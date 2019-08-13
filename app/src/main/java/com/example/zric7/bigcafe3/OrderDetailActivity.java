package com.example.zric7.bigcafe3;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zric7.bigcafe3.Adapter.OrderDetailAdapter;
import com.example.zric7.bigcafe3.Adapter.OrderListAdapter;
import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class OrderDetailActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<OrderModel> orderModelList     = new ArrayList<>();
    OrderDetailAdapter orderDetailAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;
    @BindView(R.id.txt_id_order) TextView TextViewIdOrder;
    @BindView(R.id.txt_vpemesan) TextView TextViewPemesan;
//    @BindView(R.id.txt_detail) TextView TextViewDetail;
//    @BindView(R.id.txt_status_order) TextView TextViewStatusOrder;
//    @BindView(R.id.txt_total_harga) TextView TextViewTotalHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Detail");

        orderDetailAdapter = new OrderDetailAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderDetailAdapter);

        TextViewIdOrder.setText(common.orderClicked.getId_order());
        TextViewPemesan.setText(common.orderClicked.getPemesan());
        TextViewIdOrder.setText(common.orderClicked.getId_order());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
