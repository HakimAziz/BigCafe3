package com.example.zric7.bigcafe3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Adapter.OrderDetailAdapter;
import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.Model.OrderValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.SharedPrefManager;
import com.example.zric7.bigcafe3.Utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.abhinay.input.CurrencyEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<OrderModel> orderModelList = new ArrayList<>();
    OrderDetailAdapter orderDetailAdapter;

    SharedPrefManager sharedPrefManager;
    String role_user ;

    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    @BindView(R.id.btn_checkout)Button ButtonViewCheckout;
    @BindView(R.id.txt_id_order)TextView TextViewIdOrder;
    @BindView(R.id.txt_vpemesan)TextView TextViewPemesan;
    @BindView(R.id.txt_total_harga)TextView TextViewTotalHarga;
    @BindView(R.id.txt_time)TextView TextViewTimeStamp;
    @BindView(R.id.txt_u_bayar)TextView TextViewUBayar;
    @BindView(R.id.txt_u_kembali)TextView TextViewUKembali;
    @BindView(R.id.rl_u_bayar)RelativeLayout RLuBayar;
    @BindView(R.id.rl_u_kembalian)RelativeLayout RLuKembalian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        sharedPrefManager = new SharedPrefManager(this);
        role_user = sharedPrefManager.getspRole().trim();

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Detail");

        orderDetailAdapter = new OrderDetailAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderDetailAdapter);

        TextViewIdOrder.setText("ID-"+common.orderClicked.getId_order());
        TextViewPemesan.setText(common.orderClicked.getPemesan());
        TextViewTimeStamp.setText(common.orderClicked.getTime_stamp());

        TextViewTotalHarga.setText(common.rupiahFormatter(Integer.valueOf(common.orderClicked.getTotal_harga())));
        TextViewUBayar.setText(common.rupiahFormatter(Integer.valueOf(common.orderClicked.getU_bayar())));
        TextViewUKembali.setText(common.rupiahFormatter(Integer.valueOf(common.orderClicked.getU_kembali())));

        String id_btmNavItemActive = common.bottomNavItemActive;
        switch (id_btmNavItemActive) {
            case "served":
                if((role_user.equals("waiter"))||(role_user.equals("admin"))){
                    ButtonViewCheckout.setVisibility(View.VISIBLE);
                }else{
                    ButtonViewCheckout.setVisibility(View.GONE);
                }
                RLuBayar.setVisibility(View.GONE);
                RLuKembalian.setVisibility(View.GONE);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(OrderDetailActivity.this, R.color.yellow)));
                break;
            case "paid":
                ButtonViewCheckout.setVisibility(View.GONE);
                RLuBayar.setVisibility(View.VISIBLE);
                RLuKembalian.setVisibility(View.VISIBLE);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(OrderDetailActivity.this, R.color.green)));
                break;
            case "canceled":
                ButtonViewCheckout.setVisibility(View.GONE);
                RLuBayar.setVisibility(View.GONE);
                RLuKembalian.setVisibility(View.GONE);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(OrderDetailActivity.this, R.color.red)));
                break;
            default:
                ButtonViewCheckout.setVisibility(View.GONE);
                RLuBayar.setVisibility(View.GONE);
                RLuKembalian.setVisibility(View.GONE);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(OrderDetailActivity.this, R.color.primary)));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((common.bottomNavItemActive.equals("ordered")) && ((role_user.equals("chef")) || (role_user.equals("admin")))) {
            getMenuInflater().inflate(R.menu.menu_orderdetail_cancel_order, menu);
            getMenuInflater().inflate(R.menu.menu_orderdetail_edit_status, menu);
        } else {
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
                dialogConfirmMenu(common.orderClicked.getId_order(), "canceled");
                break;

            //=========event btn centang di klik
            case R.id.action_edit_statusorder:
//                if (common.bottomNavItemActive == "ordered") {
                dialogConfirmMenu(common.orderClicked.getId_order(), "served");
//                } else {
//                    dialogConfirmMenu(common.orderClicked.getId_order(), "paid");
//                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogConfirmMenu(final String id_order, final String status_order) {
        if (status_order == "canceled") {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Cancel order ?");
            alertDialogBuilder
                    .setMessage("")
                    .setCancelable(false)
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Call<OrderValue> call = apiInterface.updateOrderStatus(id_order, status_order);
                            call.enqueue(new Callback<OrderValue>() {
                                @Override
                                public void onResponse(Call<OrderValue> call, Response<OrderValue> response) {
                                    Integer status = response.body().getStatus();
                                    if (status == 1) {
                                        Toast.makeText(OrderDetailActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(OrderDetailActivity.this, "Failed to cancel", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<OrderValue> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.i("ERROR", t.getMessage());
                                    Toast.makeText(OrderDetailActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {
            Call<OrderValue> call = apiInterface.updateOrderStatus(id_order, status_order);
            call.enqueue(new Callback<OrderValue>() {
                @Override
                public void onResponse(Call<OrderValue> call, Response<OrderValue> response) {
                    Integer status = response.body().getStatus();
                    if (status == 1) {
                        if (status_order == "served") {
                            Toast.makeText(OrderDetailActivity.this, "Served", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(OrderDetailActivity.this, status_order, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderValue> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.i("ERROR", t.getMessage());
                    Toast.makeText(OrderDetailActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //    ----------
    @OnClick(R.id.btn_checkout)
    void payCheckout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View itemView = LayoutInflater.from(this)
                .inflate(R.layout.layout_checkout_order, null);

        TextView textViewGrandTotal = (TextView) itemView.findViewById(R.id.txt_grand_total);
        final CurrencyEditText edt_Cash = (CurrencyEditText) itemView.findViewById(R.id.edt_cash);

        edt_Cash.setCurrency("Rp");
        edt_Cash.setSpacing(true);
        edt_Cash.setDecimals(false);
        edt_Cash.setSeparator(".");

        //Set data
//        textViewGrandTotal.setText(common.formatRupiah.format(Integer.parseInt(common.orderClicked.getTotal_harga())));
        textViewGrandTotal.setText(common.rupiahFormatter(Integer.valueOf(common.orderClicked.getTotal_harga())));


        builder.setView(itemView);

        Button btn_pay = (Button) itemView.findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grand_total = Integer.parseInt(common.orderClicked.getTotal_harga());
                int cash = edt_Cash.getCleanIntValue();
                if (cash < grand_total) {
                    edt_Cash.setError("Not enough cash !");
                } else {
                    int change = cash - grand_total;
                    showDialogPaySum(grand_total, cash, change);
                }
            }
        });
        builder.show();
    }

    private void showDialogPaySum(int total, final int cash, final int change) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View itemView = LayoutInflater.from(this)
                .inflate(R.layout.layout_paysum_order, null);

        TextView textViewGrandTotal = itemView.findViewById(R.id.txt_grand_total);
        final TextView textViewCash = itemView.findViewById(R.id.txt_cash);
        final TextView textViewChange = itemView.findViewById(R.id.txt_change);

        //Set data
        textViewGrandTotal.setText(common.rupiahFormatter(total));
        textViewCash.setText(common.rupiahFormatter(cash));
        textViewChange.setText(common.rupiahFormatter(change));

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String id_order = common.orderClicked.getId_order();
                String u_bayar = String.valueOf(cash);
                String u_kembali = String.valueOf(change);

                Call<OrderValue> call = apiInterface.updateOrderPay(id_order, u_bayar, u_kembali);
                call.enqueue(new Callback<OrderValue>() {
                    @Override
                    public void onResponse(Call<OrderValue> call, Response<OrderValue> response) {
                        Integer status = response.body().getStatus();
                        if (status == 1) {
                            Toast.makeText(OrderDetailActivity.this, "Paid", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(OrderDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderValue> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("ERROR", t.getMessage());
                        Toast.makeText(OrderDetailActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setView(itemView);
        builder.show();
    }


}




















































