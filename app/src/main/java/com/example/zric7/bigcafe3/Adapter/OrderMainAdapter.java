package com.example.zric7.bigcafe3.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.zric7.bigcafe3.Database.ModelDB.Cart;
import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.OrderMainActivity;
import com.example.zric7.bigcafe3.R;
import com.example.zric7.bigcafe3.Utils.common;
import com.example.zric7.bigcafe3.beMenuEditActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.zric7.bigcafe3.Utils.common.BASE_URL;

/*
==> untuk menampilkan data ke dalam RecyclerView
*/

public class OrderMainAdapter extends RecyclerView.Adapter<OrderMainAdapter.ViewHolder>{

    Context context;
    List<MenuModel> menuModelList;


    public OrderMainAdapter(Context context, List<MenuModel> vmenuModelList) {
        this.context = context;
        this.menuModelList = vmenuModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_ordermain_menu, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuModel result = menuModelList.get(position);
        holder.TextViewIdProduk.setText(result.getId_produk());
        holder.TextViewNama.setText(result.getNama());
        holder.TextViewHargaJual.setText(new StringBuilder("Rp ").append(result.getHarga_jual()).toString());
        holder.TextViewStok.setText(result.getStok());

        Picasso.get()
                .load(BASE_URL+"foto/"+result.getFoto())
                .into(holder.ImageViewFoto);

        holder.TextViewFotoMenu.setText(result.getFoto());
        holder.TextViewHargaJual_polos.setText(result.getHarga_jual());

        holder.TextViewHargaModal.setText(result.getHarga_modal());
        holder.TextViewDeskripsi.setText(result.getDeskripsi());
        holder.TextViewId_kategori.setText(result.getId_kategori());
    }


    @Override
    public int getItemCount() {
        return menuModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//Inisiasikan view2 utk menampilkan data
        @BindView(R.id.txt_id_produk) TextView TextViewIdProduk;
        @BindView(R.id.txt_nama) TextView TextViewNama;
        @BindView(R.id.txt_deskripsi) TextView TextViewDeskripsi;
        @BindView(R.id.img_foto_menu) ImageView ImageViewFoto;
        @BindView(R.id.txt_harga_modal) TextView TextViewHargaModal;
        @BindView(R.id.txt_harga_jual) TextView TextViewHargaJual;
        @BindView(R.id.txt_stok) TextView TextViewStok;
        @BindView(R.id.txt_kategori) TextView TextViewId_kategori;

        @BindView(R.id.txt_foto_menu) TextView TextViewFotoMenu;
        @BindView(R.id.txt_harga_jual_polos) TextView TextViewHargaJual_polos;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

// --> Ketika menu di klik... maka Buka activty selanjutnya dgn mengirimkan parameter id

        @Override
        public void onClick(View view) {
            final String id_produk    = TextViewIdProduk.getText().toString();
            String nama         = TextViewNama.getText().toString();
            String deskripsi    = TextViewDeskripsi.getText().toString();
            String foto         = TextViewFotoMenu.getText().toString();
            String harga_modal  = TextViewHargaModal.getText().toString();
            final String harga_jual   = TextViewHargaJual_polos.getText().toString();
            String stok         = TextViewStok.getText().toString();
            String id_kategori  = TextViewId_kategori.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_add_to_cart,null);

            ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_cart_product);
            final TextView txt_product_name = (TextView)itemView.findViewById(R.id.txt_cart_product_name);
            TextView txt_product_harga = (TextView)itemView.findViewById(R.id.txt_cart_product_harga_jual);
            final ElegantNumberButton txt_count = (ElegantNumberButton)itemView.findViewById(R.id.txt_count);

            //Set data
            Picasso.get()
                    .load(BASE_URL+"foto/"+foto)
                    .into(img_product_dialog);
            txt_product_name.setText(nama);
            txt_product_harga.setText(harga_jual);


            builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    final String numberQty = txt_count.getNumber();
                    final double total_harga = (Double.parseDouble(harga_jual) * Double.parseDouble(numberQty));

                    //Add to SQLite
                    try {
                        Cart cartItem = new Cart();
//                        cartItem.id     = id_produk;
                        cartItem.foto   = TextViewFotoMenu.getText().toString();
                        cartItem.name   = txt_product_name.getText().toString();
                        cartItem.qty    = Integer.parseInt(numberQty);
                        cartItem.price  = total_harga;

                        //Add to DB
                        common.cartRepository.insertToCart(cartItem);

                        Log.d("BigCafe_DEBUG", new Gson().toJson(cartItem));

                        Toast.makeText(context, "Save item to cart success", Toast.LENGTH_LONG).show();
                    }catch (Exception ex) {
                        Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

            builder.setView(itemView);
            builder.show();
        }
    }

}