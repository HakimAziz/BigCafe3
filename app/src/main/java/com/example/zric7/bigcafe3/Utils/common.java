package com.example.zric7.bigcafe3.Utils;

import com.example.zric7.bigcafe3.Database.DataSource.CartRepository;
import com.example.zric7.bigcafe3.Database.Local.CartDatabase;
import com.example.zric7.bigcafe3.RetrofitApi.ApiClient;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;


//Builder API

public class common {

    public static final String BASE_URL = "https://big-cafe.000webhostapp.com/coba-rest-server/";
    public static ApiInterface getAPI()
    {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }

//    Database Local fort Cart
    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;

    //Hold variabel

}
