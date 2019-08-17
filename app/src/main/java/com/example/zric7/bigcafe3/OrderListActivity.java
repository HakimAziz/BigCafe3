package com.example.zric7.bigcafe3;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Adapter.OrderListAdapter;
import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.Model.OrderValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<OrderModel> orderModelList     = new ArrayList<>();
    OrderListAdapter orderListAdapter;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order List");

        orderListAdapter = new OrderListAdapter(this, orderModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderListAdapter);

        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                progressBar.setVisibility(View.VISIBLE);
                if (item.getItemId()==R.id.ordered)
                {
                    loadOrder("ordered");
                    common.bottomNavItemActive="ordered";
                }else if (item.getItemId()==R.id.served)
                {
                    loadOrder("served");
                    common.bottomNavItemActive="served";
                }else if (item.getItemId()==R.id.paid)
                {
                    loadOrder("paid");
                    common.bottomNavItemActive="paid";
                }else if (item.getItemId()==R.id.canceled)
                {
                    loadOrder("canceled");
                    common.bottomNavItemActive="canceled";
                }
                return true;
            }
        });

        loadOrder("ordered");
        common.bottomNavItemActive="ordered";
    }

    private void loadOrder(String status_order) {
        Call<OrderValue> jsonData = apiInterface.getOrder(status_order);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<OrderValue>() {
            @Override
            public void onResponse(@NonNull Call<OrderValue> call,@NonNull  Response<OrderValue> response) {
                int status = response.body().getStatus();
                progressBar.setVisibility(View.GONE);
                if (status==1) {
                    orderModelList      = response.body().getResult();
                    orderListAdapter = new OrderListAdapter(OrderListActivity.this, orderModelList);
                    recyclerView.setAdapter(orderListAdapter);
                }else{
                    Toast.makeText(OrderListActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<OrderValue> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("ERROR_LoadOrder", t.getMessage());
                Toast.makeText(OrderListActivity.this, "Load Order Failed ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrder(common.bottomNavItemActive);
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
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
