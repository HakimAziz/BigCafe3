package com.example.zric7.bigcafe3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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

import com.example.zric7.bigcafe3.Adapter.MenuAdapter;
import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.Model.MenuValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.androidhive.fontawesome.FontDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class beMenuActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<MenuModel> menuModelList = new ArrayList<>();
    MenuAdapter menuAdapter;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    // inisialisasi fab
    private FloatingActionButton fab;


    //   =======> Saat halaman jalan... (onCreate)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_menu);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar menu");

        menuAdapter = new MenuAdapter(this, menuModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(menuAdapter);

//        Inisiasi Tombol FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        FontDrawable drawable = new FontDrawable(this, R.string.fa_plus_solid, true, false);
        drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        fab.setImageDrawable(drawable);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(beMenuActivity.this, beMenuAddActivity.class));
            }
        });

//        Panggil method untuk nampilin daftar menu
        getMenu();
    }
    //   ==================================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getMenu();
//    }

    private void getMenu() {
        Call<MenuValue> jsonData = apiInterface.getMenu();  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<MenuValue>() {
            @Override
            public void onResponse(Call<MenuValue> call, Response<MenuValue> response) {
                int status = response.body().getStatus();
                progressBar.setVisibility(View.GONE);
                if (status==1) {
                    menuModelList = response.body().getResult();
                    menuAdapter = new MenuAdapter(beMenuActivity.this, menuModelList);
                    recyclerView.setAdapter(menuAdapter);
//                    Toast.makeText(beMenuActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(beMenuActivity.this, "eror", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MenuValue> call, Throwable t) {
                Toast.makeText(beMenuActivity.this, "gagal response", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
