package com.example.zric7.bigcafe3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.zric7.bigcafe3.Database.ModelDB.Cart;
import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.R;
import com.example.zric7.bigcafe3.Utils.common;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.zric7.bigcafe3.Utils.common.BASE_URL;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        Cart result = cartList.get(position);
//        holder.TextViewIdProduk.setText(result.getId_produk());
        holder.TextViewNama.setText(result.name);
        holder.TextViewHargaJual.setText("@"+common.rupiahFormatter(result.price_item));
        holder.ENBQty.setNumber(String.valueOf(result.qty));

        if (result.foto.isEmpty()) {
            Picasso.get()
                    .load(R.drawable.img_holder)
                    .into(holder.ImageViewFoto);
        } else{
            Picasso.get()
                    .load(result.foto)
                    .into(holder.ImageViewFoto);
        }

        holder.ENBQty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

//                klo Qty nya jadi 0.. maka delete item dari cart
                if (newValue==0){
                    Cart result = cartList.get(position);
                    String name = result.name;

                    final Cart deletedItem = result;
                    final int deletedIndex = position;

                    // Delete item from adapter
                    removeItem(deletedIndex);

                    //Delete item from Room Database
                    common.cartRepository.deleteCartItem(deletedItem);

                    Snackbar snackbar = Snackbar.make(view, new StringBuilder(name).append(" removed from cart list").toString(),
                            Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restoreItem(deletedItem, deletedIndex);
                            common.cartRepository.insertToCart(deletedItem);

                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

//                else --------------
                }else {
                    Cart result = cartList.get(position);
                    result.qty  = newValue;

                    common.cartRepository.updateCart(result);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


//    View Holder

    public class CartViewHolder extends RecyclerView.ViewHolder {

        //Inisiasikan view2 utk menampilkan data
        @BindView(R.id.txt_nama)TextView TextViewNama;
        @BindView(R.id.txt_harga_jual)TextView TextViewHargaJual;
        @BindView(R.id.img_foto_menu)ImageView ImageViewFoto;
        @BindView(R.id.txt_qty)ElegantNumberButton ENBQty;

        public @BindView(R.id.view_background)
        RelativeLayout view_background;
        public @BindView(R.id.view_foreground)
        RelativeLayout view_foreground;


        public CartViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
    public void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}
