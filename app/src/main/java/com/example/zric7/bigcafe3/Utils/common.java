package com.example.zric7.bigcafe3.Utils;

import com.example.zric7.bigcafe3.Database.DataSource.CartRepository;
import com.example.zric7.bigcafe3.Database.Local.CartDatabase;
import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.R;
import com.example.zric7.bigcafe3.RetrofitApi.ApiClient;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;

import java.text.NumberFormat;
import java.util.Locale;

import me.abhinay.input.CurrencyEditText;


//Builder API

public class common {

    public static final String BASE_URL = "https://big-cafe.000webhostapp.com/coba-rest-server/";

    public static ApiInterface getAPI() {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }

    //    Database Local fort Cart
    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;

    //Format display Uang
    public static Locale localeID = new Locale("in", "ID");
    public static NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);


    //Hold variabel
    public static OrderModel orderClicked;
    public static String bottomNavItemActive;

}
