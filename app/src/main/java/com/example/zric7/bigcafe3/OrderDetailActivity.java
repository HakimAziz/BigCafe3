package com.example.zric7.bigcafe3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Adapter.OrderDetailAdapter;
import com.example.zric7.bigcafe3.Adapter.OrderListAdapter;
import com.example.zric7.bigcafe3.Model.MenuValue;
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

public class OrderDetailActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<OrderModel> orderModelList     = new ArrayList<>();
    OrderDetailAdapter orderDetailAdapter;

    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.progress_bar)ProgressBar progressBar;

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
    protected void onResume() {
        super.onResume();
//        getMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(common.bottomNavItemActive=="ordered"){
            getMenuInflater().inflate(R.menu.menu_orderdetail_cancel_order,menu);
            getMenuInflater().inflate(R.menu.menu_orderdetail_edit_status,menu);
        }else if(common.bottomNavItemActive=="ready"){
            getMenuInflater().inflate(R.menu.menu_orderdetail_edit_status,menu);
        }else if(common.bottomNavItemActive=="served"){
            getMenuInflater().inflate(R.menu.menu_orderdetail_edit_status,menu);
        }else{
//            gak nampilin menu
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //===========event saat btn back di klik
            case android.R.id.home:
                finish();
                break;
            //=========event btn cancel di klik
            case R.id.action_cancel_order:
                dialogConfirmMenu(common.orderClicked.getId_order(),"canceled");
                break;

            //=========event btn centang di klik
            case R.id.action_edit_statusorder:
                if(common.bottomNavItemActive=="ordered"){
                    dialogConfirmMenu(common.orderClicked.getId_order(),"ready");
                }else if(common.bottomNavItemActive=="ready"){
                    dialogConfirmMenu(common.orderClicked.getId_order(),"served");
                }else{
                    dialogConfirmMenu(common.orderClicked.getId_order(),"paid");
                }break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogConfirmMenu(final String id_order, final String status_order) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin mengapus data ini?")
                .setCancelable(false)
                .setPositiveButton("Hapus",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        Call<OrderValue> call = apiInterface.updateOrderStatus(id_order,status_order);
                        call.enqueue(new Callback<OrderValue>() {
                            @Override
                            public void onResponse(Call<OrderValue> call, Response<OrderValue> response) {
                                Integer status = response.body().getStatus();
                                if (status==1) {
                                    Toast.makeText(OrderDetailActivity.this, "bisa cancel", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(OrderDetailActivity.this, "gagal cancel", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<OrderValue> call, Throwable t) {
                                t.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(OrderDetailActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
