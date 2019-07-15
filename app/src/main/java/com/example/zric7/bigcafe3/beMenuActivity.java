package com.example.zric7.bigcafe3;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class beMenuActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<MenuModel> menuModelList = new ArrayList<>();
    MenuAdapter menuAdapter;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    //   =======> Saat halaman jalan... (onCreate)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_menu);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar menu");

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        menuAdapter = new MenuAdapter(this, menuModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(menuAdapter);

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
                    Toast.makeText(beMenuActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
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
