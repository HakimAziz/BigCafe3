package com.example.zric7.bigcafe3.RetrofitApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Class dgn method Koneksi ke Web Service
//Inisiasikan URL target
//
//Selanjutnya..
// pada Activity, Objek ApiInterface -> jalankan ApiClient.GetClient()
//                                  -> create ApiInterface.. utk panggil method request

public class ApiClient {


    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl)
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
