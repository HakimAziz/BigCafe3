package com.example.zric7.bigcafe3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.Model.OrderValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<MenuModel> menuModelList = new ArrayList<>();

    String selectedClass;

    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
    @BindView(R.id.spinner_income)
    Spinner spinnerIncome;

    @BindView(R.id.txt_income)
    TextView textIncome;
    @BindView(R.id.txt_count)
    TextView textCount;

    //   =======> Saat halaman jalan... (onCreate)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Order menu");

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        spinnerIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get Selected Class name from the list
                selectedClass = parentView.getItemAtPosition(position).toString();

                switch (selectedClass) {
                    case "This Month":
                        selectedClass="month";
                        break;
                    default:
                        selectedClass="day";
                        break;
                }
                getincome(selectedClass);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    protected void onResume() {
        super.onResume();
        getincome(selectedClass);
    }

    private void getincome(String scope) {
        Call<OrderValue> jsonData = apiInterface.getIncome(scope);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<OrderValue>() {
            @Override
            public void onResponse(@NonNull Call<OrderValue> call, @NonNull Response<OrderValue> response) {
                OrderValue orderModel = response.body();
                String string;
                Integer income_value = Integer.parseInt(orderModel.getResult().get(0).getIncome());
                Integer count_value = Integer.parseInt(orderModel.getResult().get(0).getCount());
                textIncome.setText(common.rupiahFormatter(income_value));
                textCount.setText(count_value + " Transactions");
          }

            @Override
            public void onFailure(Call<OrderValue> call, Throwable t) {
                Log.i("ERROR_LoadIncome", t.getMessage());
                Toast.makeText(MainActivity.this, "Load Income Failed ", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    ===========================

    public void on_kelola_menu(View view) {
        startActivity(new Intent(this, beMenuActivity.class));
    }

    public void on_order_menu(View view) {
        startActivity(new Intent(this, OrderMainActivity.class));
    }

    public void on_list_order(View view) {
        startActivity(new Intent(this, OrderListActivity.class));
    }

}
