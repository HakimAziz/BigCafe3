package com.example.zric7.bigcafe3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private ProgressDialog progress;

    @BindView(R.id.edit_txt_id_produk)
    EditText editTextIdProduk;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_menu_edit);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Data");

        Intent intent = getIntent();
        String id_produk    = intent.getStringExtra("id_produk");
        String nama         = intent.getStringExtra("nama");
        String deskripsi    = intent.getStringExtra("deskripsi");
        String foto         = intent.getStringExtra("foto");
        String harga_modal  = intent.getStringExtra("harga_modal");
        String harga_jual   = intent.getStringExtra("harga_jual");
        String stok         = intent.getStringExtra("stok");
        String id_kategori  = intent.getStringExtra("id_kategori");


        editTextIdProduk.setText(id_produk);
        editTextNama.setText(nama);
        editTextDeskripsi.setText(deskripsi);
        editTextFoto.setText(foto);
        editTextHargaModal.setText(harga_modal);
        editTextHargaJual.setText(harga_jual);
        editTextStok.setText(stok);

        if (id_kategori.equals("makanan")) {
            radioMakanan.setChecked(true);
        } else {
            radioMinuman.setChecked(true);
        }
    }

    @OnClick(R.id.btn_ubah) void updateMenu() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data dari edittext
        String id_produk = editTextIdProduk.getText().toString();
        String nama = editTextNama.getText().toString();
        String deskripsi = editTextDeskripsi.getText().toString();
        String foto = "ini foto";
        String harga_modal = editTextHargaModal.getText().toString();
        String harga_jual = editTextHargaJual.getText().toString();
        String stok = editTextStok.getText().toString();

        int selectedId = radioGroupKategori.getCheckedRadioButtonId();
        // mencari id radio button
        radioKategoriButton = (RadioButton) findViewById(selectedId);
        String id_kategori = radioKategoriButton.getText().toString();

        Call<MenuValue> jsonData = apiInterface.updateMenu(id_produk,id_kategori,nama,deskripsi,foto,harga_modal,harga_jual,stok);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<MenuValue>() {
            @Override
            public void onResponse(Call<MenuValue> call, Response<MenuValue> response) {
                Integer status = response.body().getStatus();
                progress.dismiss();
                if (status==1) {
                    Toast.makeText(beMenuEditActivity.this, "bisa update", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(beMenuEditActivity.this, "gagal update", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Peringatan");
                alertDialogBuilder
                        .setMessage("Apakah Anda yakin ingin mengapus data ini?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                String id_produk = editTextIdProduk.getText().toString();

                                Call<MenuValue> call = apiInterface.deleteMenu(id_produk);
                                call.enqueue(new Callback<MenuValue>() {
                                    @Override
                                    public void onResponse(Call<MenuValue> call, Response<MenuValue> response) {
                                        Integer status = response.body().getStatus();
                                        if (status==1) {
                                            Toast.makeText(beMenuEditActivity.this, "bisa hapus", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(beMenuEditActivity.this, "gagal hapus", Toast.LENGTH_SHORT).show();
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
                        })
                        .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
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




 }
