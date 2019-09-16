package com.example.zric7.bigcafe3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class beMenuEditActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<MenuModel> menuModelList = new ArrayList<>(); /*(gak di pake)*/

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_menu_edit);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Menu");

        Intent intent = getIntent();
        String id_produk    = intent.getStringExtra("id_produk");
        String nama         = intent.getStringExtra("nama");
//        String foto         = intent.getStringExtra("foto");
        String harga_modal  = intent.getStringExtra("harga_modal");
        String harga_jual   = intent.getStringExtra("harga_jual");
        String ket          = intent.getStringExtra("ket");
        String kategori     = intent.getStringExtra("kategori");

        editTextIdProduk.setText(id_produk);
        editTextNama.setText(nama);
//        editTextFoto.setText(foto);
        editTextHargaModal.setText(harga_modal);
        editTextHargaJual.setText(harga_jual);

        if (kategori.equals("Makanan")) {
            radioMakanan.setChecked(true);
        } else {
            radioMinuman.setChecked(true);
        }
        if (ket.equals("Tersedia")) {
            radioTersedia.setChecked(true);
        } else {
            radioTidakTersedia.setChecked(true);
        }
    }

    @OnClick(R.id.btn_edit) void updateMenu() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data dari edittext
        String id_produk = editTextIdProduk.getText().toString();
        String nama = editTextNama.getText().toString();
        String foto = "ini foto";
        String harga_modal = editTextHargaModal.getText().toString();
        String harga_jual = editTextHargaJual.getText().toString();

        int selectedkategori= radioGroupKategori.getCheckedRadioButtonId();
        radioKategoriButton = (RadioButton) findViewById(selectedkategori);
        String kategori     = radioKategoriButton.getText().toString();

        int selectedKet     = radioGroupKategori.getCheckedRadioButtonId();
        radioKetButton      = (RadioButton) findViewById(selectedKet);
        String ket          = radioKetButton.getText().toString();

        Call<MenuValue> jsonData = apiInterface.updateMenu(
                id_produk,
                kategori,
                nama,
                foto,
                harga_modal,
                harga_jual,
                ket);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<MenuValue>() {
            @Override
            public void onResponse(Call<MenuValue> call, Response<MenuValue> response) {
                Integer status = response.body().getStatus();
                progress.dismiss();
                if (status==1) {
                    Toast.makeText(beMenuEditActivity.this, "Success to edit", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(beMenuEditActivity.this, "Failed to edit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MenuValue> call, Throwable t) {
                t.printStackTrace();
                progress.dismiss();
                Toast.makeText(beMenuEditActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("");
                alertDialogBuilder
                        .setMessage("Delete menu item ?")
                        .setCancelable(false)
                        .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                String id_produk = editTextIdProduk.getText().toString();

                                Call<MenuValue> call = apiInterface.deleteMenu(id_produk);
                                call.enqueue(new Callback<MenuValue>() {
                                    @Override
                                    public void onResponse(Call<MenuValue> call, Response<MenuValue> response) {
                                        Integer status = response.body().getStatus();
                                        if (status==1) {
                                            Toast.makeText(beMenuEditActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(beMenuEditActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<MenuValue> call, Throwable t) {
                                        progress.dismiss();
                                        Log.i("ERROR_LoadMenu", t.getMessage());
                                        Toast.makeText(beMenuEditActivity.this, "Load Menu Failed ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }




 }
