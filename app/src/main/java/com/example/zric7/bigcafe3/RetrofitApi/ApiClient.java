package com.example.zric7.bigcafe3.RetrofitApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Class dgn method Koneksi ke Web Service
//Inisiasikan URL target
//
//Selanjutnya..
// pada Activity, Objek ApiInterface -> jalankan ApiClient.GetClient()
//                                  -> create ApiInterface.. utk panggil method request

public class ApiClient {

//    Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();

    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl)
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
