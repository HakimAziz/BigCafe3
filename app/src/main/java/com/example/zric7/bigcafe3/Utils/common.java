package com.example.zric7.bigcafe3.Utils;

import com.example.zric7.bigcafe3.RetrofitApi.ApiClient;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;


//Builder API

public class common {

    public static final String BASE_URL = "https://big-cafe.000webhostapp.com/big-cafe/";
    public static ApiInterface getAPI()
    {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
