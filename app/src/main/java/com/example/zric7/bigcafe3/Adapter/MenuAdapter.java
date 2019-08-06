package com.example.zric7.bigcafe3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.R;
import com.example.zric7.bigcafe3.beMenuEditActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.example.zric7.bigcafe3.Utils.common.BASE_URL;

/*
==> untuk menampilkan data ke dalam RecyclerView
*/

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    Context context;
    List<MenuModel> menuModelList;

    public MenuAdapter(Context context, List<MenuModel> vmenuModelList) {
        this.context = context;
        this.menuModelList = vmenuModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_be_menu, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuModel result = menuModelList.get(position);
        holder.TextViewIdProduk.setText(result.getId_produk());
        holder.TextViewNama.setText(result.getNama());
        holder.TextViewHargaJual.setText(new StringBuilder("Rp ").append(result.getHarga_jual()).toString());
        holder.TextViewKet.setText(result.getKet());

        if (result.getFoto().isEmpty()) {
            Picasso.get()
                    .load(R.drawable.img_holder)
                    .into(holder.ImageViewFoto);
        } else{
            Picasso.get()
                    .load(result.getFoto())
                    .into(holder.ImageViewFoto);
        }

        holder.TextViewFoto.setText(result.getFoto());
        holder.TextViewHargaModal.setText(result.getHarga_modal());
        holder.TextViewKategori.setText(result.getKategori());
    }


    @Override
    public int getItemCount() {
        if(menuModelList == null) return 0;
        return menuModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//Inisiasikan view2 utk menampilkan data
        @BindView(R.id.txt_id_produk) TextView TextViewIdProduk;
        @BindView(R.id.txt_nama) TextView TextViewNama;
        @BindView(R.id.txt_foto_menu) TextView TextViewFoto;
        @BindView(R.id.img_foto_menu) ImageView ImageViewFoto;
        @BindView(R.id.txt_harga_modal) TextView TextViewHargaModal;
        @BindView(R.id.txt_harga_jual) TextView TextViewHargaJual;
        @BindView(R.id.txt_ket) TextView TextViewKet;
        @BindView(R.id.txt_kategori) TextView TextViewKategori;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

// --> Ketika menu di klik... maka Buka activty selanjutnya dgn mengirimkan parameter id

        @Override
        public void onClick(View view) {
            String id_produk    = TextViewIdProduk.getText().toString();
            String nama         = TextViewNama.getText().toString();
            String foto         = TextViewFoto.getText().toString();
            String harga_modal  = TextViewHargaModal.getText().toString();
            String harga_jual   = TextViewHargaJual.getText().toString();
            String ket          = TextViewKet.getText().toString();
            String kategori     = TextViewKategori.getText().toString();

            Intent i = new Intent(context, beMenuEditActivity.class);
            i.putExtra("id_produk", id_produk);
            i.putExtra("nama", nama);
            i.putExtra("foto", foto);
            i.putExtra("harga_modal", harga_modal);
            i.putExtra("harga_jual", harga_jual);
            i.putExtra("ket", ket);
            i.putExtra("kategori", kategori);
            context.startActivity(i);
        }
    }

}