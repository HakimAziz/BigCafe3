package com.example.zric7.bigcafe3.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zric7.bigcafe3.Model.MenuModel;
import com.example.zric7.bigcafe3.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

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
        holder.TextViewNama.setText(result.getNama());
        holder.TextViewHargaJual.setText(result.getHarga_jual());
        holder.TextViewFoto.setText(result.getFoto());
    }

    @Override
    public int getItemCount() {
//        //your problem is here because you are printing size of list which is null,
//        //so instead of that check the null first, then use the list object
//        if (menuModelList == null)
//            Log.d(TAG, "null list");
//        else
//            Log.d(TAG, "getItemCount: list DATA-----" + menuModelList.size());
//
//        //**Here I'm getting Null Pointer exception**
//        return menuModelList == null ? 0 : menuModelList.size();

        return menuModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_nama) TextView TextViewNama;
        @BindView(R.id.txt_harga) TextView TextViewHargaJual;
        @BindView(R.id.txt_foto) TextView TextViewFoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}