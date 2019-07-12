package com.example.zric7.bigcafe3.RetrofitApi;

import com.example.zric7.bigcafe3.Model.MenuValue;

import retrofit2.Call;
import retrofit2.http.GET;

/*
API Interface for manage endpoint
Panggil method request yang akan dijalankan oleh web service
*/

public interface ApiInterface {

//    Tampilin Data
    @GET("getmenu.php")         /*method request ke web service, lalu Model mana yg menangkapnya*/
    Call<MenuValue> getMenu();

}