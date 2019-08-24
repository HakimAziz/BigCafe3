package com.example.zric7.bigcafe3;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Adapter.CartAdapter;
import com.example.zric7.bigcafe3.Database.ModelDB.Cart;
import com.example.zric7.bigcafe3.Model.MenuValue;
import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.Model.OrderValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.RecyclerItemTouchHelper;
import com.example.zric7.bigcafe3.Utils.RecyclerItemTouchHelperListner;
import com.example.zric7.bigcafe3.Utils.common;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner {

    ApiInterface apiInterface;
    List<Cart> cartList = new ArrayList<>();
    CartAdapter cartAdapter;

    //Rxjava -> Collection of the disposables

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.btn_place_order)
    Button btn_place_order;
    //
//    @BindView(R.id.radio_g_pemesan)RadioGroup radioGroupPemesan;
//    private RadioButton radioPemesanButton;
//    String pemesan;
    private RadioButton radioPemesanButton;
    private String pemesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");

        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });

//        Panggil method untuk nampilin daftar menu
        loadCartItems();

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

    //    ==============================
//    Method Menampilkan List Cart
//    ==============================

    private void loadCartItems() {
        compositeDisposable.add(
                common.cartRepository.getCartItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> cartList) throws Exception {
                                displayCartItem(cartList);
                            }
                        }));
    }

    private void displayCartItem(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
    }


    //    ==============================
//    Method Submit Order Cart ke database server
//    ==============================
    private void placeOrder() {
        //show alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");

        View submitOrderLayout = LayoutInflater.from(this).inflate(R.layout.layout_submit_order, null);

        final RadioButton rdi_other         = submitOrderLayout.findViewById(R.id.rdi_other);
        final EditText edt_other            = submitOrderLayout.findViewById(R.id.edt_other);
        final RadioGroup radioGroupPemesan  = submitOrderLayout.findViewById(R.id.radio_g_pemesan);

        //Event
        radioGroupPemesan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdi_other.isChecked()){
                    edt_other.setEnabled(true);
                }else {
                    edt_other.setEnabled(false);
                }
            }
        });

        builder.setView(submitOrderLayout);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                 Submit Order
                if (rdi_other.isChecked()){
                    pemesan = edt_other.getText().toString();
                }else {
                    int checkedId     = radioGroupPemesan.getCheckedRadioButtonId();
                    RadioButton checkedRadioButton = (RadioButton) radioGroupPemesan.findViewById(checkedId);
                    pemesan = checkedRadioButton.getText().toString();
                }

                compositeDisposable.add(
                        common.cartRepository.getCartItems()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<List<Cart>>() {
                                    @Override
                                    public void accept(List<Cart> carts) throws Exception {
                                        if (!TextUtils.isEmpty(pemesan))
                                            sendOrderToServer(
                                                    pemesan,
                                                    carts,
                                                    common.cartRepository.sumPrice()
                                                    );
                                        else
                                            Toast.makeText(CartActivity.this, "Pemesan Cant Be Empty", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {

                                    }
                                })
                );


            }
        });
        builder.show();
    }

    private void sendOrderToServer(final String pemesan, List<Cart> carts, int sumPrice) {
        if (carts.size() > 0) {

            final String detail         = new Gson().toJson(carts);
            final String status_order   = "ordered";
            final Integer total_harga   = sumPrice;

            Call<OrderModel> jsonData =apiInterface.addOrder(
                    pemesan,
                    detail,
                    status_order,
                    total_harga);
                    jsonData.enqueue(new Callback<OrderModel>() {
                        @Override
                        public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CartActivity.this, "Order Submitted", Toast.LENGTH_SHORT).show();
                                //Clear cart
                                common.cartRepository.emptyCart();
                                finish();
                            }else { Toast.makeText(CartActivity.this, "Order gak sukses", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderModel> call, Throwable t) {
                            Log.i("ERROR_submitOrder", t.getMessage()+pemesan+status_order+detail+total_harga);
                            Toast.makeText(CartActivity.this, "Order Failed ", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    //    ==============================
//    Method pada item list
//    ==============================
    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // Delete item from adapter
            cartAdapter.removeItem(deletedIndex);

            //Delete item from Room Database
            common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" removed from cart list").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deletedItem, deletedIndex);
                    common.cartRepository.insertToCart(deletedItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
