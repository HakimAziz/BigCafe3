package com.example.zric7.bigcafe3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.example.zric7.bigcafe3.Adapter.MenuAdapter;
import com.example.zric7.bigcafe3.Adapter.OrderMainAdapter;
import com.example.zric7.bigcafe3.Database.DataSource.CartRepository;
import com.example.zric7.bigcafe3.Database.Local.CartDataSource;
import com.example.zric7.bigcafe3.Database.Local.CartDatabase;
import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.Model.MenuValue;
import com.example.zric7.bigcafe3.Model.OrderValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.common;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.androidhive.fontawesome.FontDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderMainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ApiInterface apiInterface;
    List<MenuModel> menuModelList = new ArrayList<>();
    OrderMainAdapter orderMainAdapter;
    NotificationBadge badge; //notif cart
    ImageView cart_icon;
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.progress_bar)ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_main);


        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Take Order");

        orderMainAdapter = new OrderMainAdapter(this, menuModelList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderMainAdapter);

        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                progressBar.setVisibility(View.VISIBLE);
                if (item.getItemId()==R.id.makanan)
                {
                    getMenuByKategori("makanan");
                }else if (item.getItemId()==R.id.minuman)
                {
                    getMenuByKategori("minuman");
                }
                return true;
            }
        });
        getMenuByKategori("makanan");

        //Init Database
        initDB();
    }

    private void initDB() {
        common.cartDatabase = CartDatabase.getInstance(this);
        common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(common.cartDatabase.cartDAO()));
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

    @Override
    public void onResume() {
        super.onResume();
        updateCartCount();
//        getMenu();

    }

    //    ==============================
//    Method Menampilkan daftar data menu
//    ==============================
    private void getMenuByKategori(String kategori) {
        Call<MenuValue> jsonData = apiInterface.getMenuByKategori(kategori);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<MenuValue>() {
            @Override
            public void onResponse(@NonNull Call<MenuValue> call,@NonNull Response<MenuValue> response) {
                int status = response.body().getStatus();
                progressBar.setVisibility(View.GONE);
                if (status==1) {
                    menuModelList = response.body().getResult();
                    orderMainAdapter = new OrderMainAdapter(OrderMainActivity.this, menuModelList);
                    recyclerView.setAdapter(orderMainAdapter);
                }else{
                    Toast.makeText(OrderMainActivity.this, "eror", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MenuValue> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("ERROR_LoadMenu", t.getMessage());
                Toast.makeText(OrderMainActivity.this, "Load Menu Failed ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    ==============================
//    Method Search Bar -> utk nyari menu
//    ==============================
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Menu");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);

        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = (NotificationBadge)view.findViewById(R.id.badge);
        cart_icon = (ImageView) view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderMainActivity.this, CartActivity.class));
            }
        });
        updateCartCount();

        return true;
    }

    public void updateCartCount() {
        if(badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(common.cartRepository.countCartItems() == 0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Call<MenuValue> jsonData = apiInterface.searchMenu(newText);  /*Panggil method request ke webservice*/

        jsonData.enqueue(new Callback<MenuValue>() {
            @Override
            public void onResponse(@NonNull Call<MenuValue> call, @NonNull Response<MenuValue> response) {
                int status = response.body().getStatus();
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if (status==1) {
                    menuModelList = response.body().getResult();
                    orderMainAdapter = new OrderMainAdapter(OrderMainActivity.this, menuModelList);
                    recyclerView.setAdapter(orderMainAdapter);
                }
            }
            @Override
            public void onFailure(Call<MenuValue> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
