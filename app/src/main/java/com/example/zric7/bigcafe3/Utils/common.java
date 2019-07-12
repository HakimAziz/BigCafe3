package com.example.zric7.bigcafe3.Utils;

import com.example.zric7.bigcafe3.RetrofitApi.ApiClient;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;


//Builder API

public class common {

    private static final String BASE_URL = "http://192.168.0.107/big-cafe/";
    public static ApiInterface getAPI()
    {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
