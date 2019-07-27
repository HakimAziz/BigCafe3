package com.example.zric7.bigcafe3;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.Model.MenuValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class beMenuAddActivity extends AppCompatActivity {

    ApiInterface apiInterface;

    private RadioButton radioKategoriButton;
    private ProgressDialog progress;
//
//    @BindView(R.id.edit_txt_id_produk)
//    EditText editTextIdProduk;
    @BindView(R.id.edit_txt_nama)
    EditText editTextNama;
    @BindView(R.id.edit_txt_deskripsi)
    EditText editTextDeskripsi;
    @BindView(R.id.edit_txt_foto)
    EditText editTextFoto;
    @BindView(R.id.edit_txt_harga_modal)
    EditText editTextHargaModal;
    @BindView(R.id.edit_txt_harga_jual)
    EditText editTextHargaJual;
    @BindView(R.id.edit_txt_stok)
    EditText editTextStok;
    @BindView(R.id.radio_g_kategori)
    RadioGroup radioGroupKategori;
    @BindView(R.id.radio_makanan)
    RadioButton radioMakanan;
    @BindView(R.id.radio_minuman)
    RadioButton radioMinuman;

    @OnClick(R.id.btn_add) void addMenu() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data dari edittext & radio group
        int selectedId = radioGroupKategori.getCheckedRadioButtonId();
        radioKategoriButton = (RadioButton) findViewById(selectedId);

        String id_kategori = radioKategoriButton.getText().toString();

//        String id_produk = editTextIdProduk.getText().toString();
        String nama         = editTextNama.getText().toString().trim();
        String deskripsi    = editTextDeskripsi.getText().toString();
        String foto         = editTextFoto.getText().toString();
        String harga_modal  = editTextHargaModal.getText().toString();
        String harga_jual   = editTextHargaJual.getText().toString();
        String stok         = editTextStok.getText().toString();
//
//        progress.dismiss();
//        Toast.makeText(beMenuAddActivity.this, nama+" "+deskripsi+foto+harga_jual+harga_modal+stok, Toast.LENGTH_LONG).show();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://big-cafe.000webhostapp.com/big-cafe/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        Call<MenuValue> jsonData = apiInterface.addMenu(
                id_kategori,
                nama,
                deskripsi,
                foto,
                harga_modal,
                harga_jual,
                stok);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<MenuValue>() {
            @Override
            public void onResponse(Call<MenuValue> call, Response<MenuValue> response) {
                Integer status = response.body().getStatus();
                progress.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(beMenuAddActivity.this, "bisa ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(beMenuAddActivity.this, "gk bisa ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MenuValue> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Toast.makeText(beMenuAddActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_menu_add);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Data");

    }

//    method tombol back
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
