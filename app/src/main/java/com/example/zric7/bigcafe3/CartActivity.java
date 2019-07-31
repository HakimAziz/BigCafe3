package com.example.zric7.bigcafe3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Adapter.CartAdapter;
import com.example.zric7.bigcafe3.Adapter.OrderMainAdapter;
import com.example.zric7.bigcafe3.Database.ModelDB.Cart;
import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.Model.MenuValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.common;

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

public class CartActivity extends AppCompatActivity {

//    ApiInterface apiInterface;
//    List<Cart> cartList = new ArrayList<>();
//    CartAdapter cartAdapter;

    //Rxjava -> Collection of the disposables
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.btn_place_order)
    Button btn_place_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");

        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

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

    private void displayCartItem(List<Cart> cartList) {
        CartAdapter cartAdapter = new CartAdapter(this,cartList);
        recycler_cart.setAdapter(cartAdapter);
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
}
