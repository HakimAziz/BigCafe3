package com.example.zric7.bigcafe3;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Adapter.CartAdapter;
import com.example.zric7.bigcafe3.Adapter.OrderMainAdapter;
import com.example.zric7.bigcafe3.Database.ModelDB.Cart;
import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.Model.MenuValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.RecyclerItemTouchHelper;
import com.example.zric7.bigcafe3.Utils.RecyclerItemTouchHelperListner;
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

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner{

//    ApiInterface apiInterface;
//    List<Cart> cartList = new ArrayList<>();
//    CartAdapter cartAdapter;

    //Rxjava -> Collection of the disposables

    List<Cart> cartList = new ArrayList<>();
    CartAdapter cartAdapter;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
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

        ItemTouchHelper.SimpleCallback simpleCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
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
        cartAdapter = new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }


    //    ==============================
//    Method Submit Order Cart ke database server
//    ==============================
    private void placeOrder() {
        //show alert dialog
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
        if(viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // Delete item from adapter
            cartAdapter.removeItem(deletedIndex);

            //Delete item from Room Database
            common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" removed from favorites list").toString(),
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
