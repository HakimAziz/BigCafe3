package com.example.zric7.bigcafe3;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
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
    private RadioButton radioKetButton;
    private ProgressDialog progress;

    @BindView(R.id.edt_id_produk)EditText editTextIdProduk;
    @BindView(R.id.edt_nama)EditText editTextNama;
    @BindView(R.id.img_foto_edit)ImageView imgFoto;
    @BindView(R.id.edt_harga_modal)EditText editTextHargaModal;
    @BindView(R.id.edt_harga_jual)EditText editTextHargaJual;

    @BindView(R.id.radio_g_kategori)RadioGroup radioGroupKategori;
    @BindView(R.id.radio_makanan)RadioButton radioMakanan;
    @BindView(R.id.radio_minuman)RadioButton radioMinuman;

    @BindView(R.id.radio_g_ket)RadioGroup radioGroupKet;
    @BindView(R.id.radio_tersedia)RadioButton radioTersedia;
    @BindView(R.id.radio_tidak_tersedia)RadioButton radioTidakTersedia;

//==================
    @OnClick(R.id.btn_add) void addMenu() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data dari edittext
//        String id_produk    = editTextIdProduk.getText().toString();
        String nama         = editTextNama.getText().toString().trim();
        String foto         = "ini foto".trim();
        String harga_modal  = editTextHargaModal.getText().toString().trim();
        String harga_jual   = editTextHargaJual.getText().toString().trim();

        int selectedkategori= radioGroupKategori.getCheckedRadioButtonId();
        radioKategoriButton = (RadioButton) findViewById(selectedkategori);
        String kategori     = radioKategoriButton.getText().toString().trim();

        int selectedKet     = radioGroupKategori.getCheckedRadioButtonId();
        radioKetButton      = (RadioButton) findViewById(selectedKet);
        String ket          = radioKetButton.getText().toString().trim();

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        Call<MenuValue> jsonData = apiInterface.addMenu(
                kategori,
                nama,
//                foto,
                harga_modal,
                harga_jual,
                ket);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<MenuValue>() {
            @Override
            public void onResponse(Call<MenuValue> call, Response<MenuValue> response) {
                Integer status = response.body().getStatus();
                progress.dismiss();
                if (status==1) {
                    Toast.makeText(beMenuAddActivity.this, "Menu item has been added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(beMenuAddActivity.this, "Menu item failed to add", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MenuValue> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Toast.makeText(beMenuAddActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
