package com.example.zric7.bigcafe3.RetrofitApi;

import com.example.zric7.bigcafe3.Model.MenuValue;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/*
API Interface for manage endpoint
Panggil method request yang akan dijalankan oleh web service
*/

public interface ApiInterface {

//    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("addmenu.php")
    Call<MenuValue> addMenu(
            @Field("id_kategori") String id_kategori,
            @Field("nama") String nama,
            @Field("deksripsi") String deskripsi,
            @Field("foto") String foto,
            @Field("harga_modal") String harga_modal,
            @Field("harga_jual") String harga_jual,
            @Field("stok") String stok
    );

    //    Tampilin Data
    @GET("getmenu.php")         /*method request ke web service, lalu Model mana yg menangkapnya*/
    Call<MenuValue> getMenu();

    //    Edit Data
    @FormUrlEncoded
    @POST("updatemenu.php")
    Call<MenuValue> updateMenu(
            @Field("id_produk") String id_produk,
            @Field("id_kategori") String id_kategori,
            @Field("nama") String nama,
            @Field("deksripsi") String deskripsi,
            @Field("foto") String foto,
            @Field("harga_modal") String harga_modal,
            @Field("harga_jual") String harga_jual,
            @Field("stok") String stok
    );

    //    Hapus Data
    @FormUrlEncoded
    @POST("deletemenu.php")
    Call<MenuValue> deleteMenu(@Field("id_produk") String id_produk);


    //    //    Tampilin Data berdasarkan id yg di klik
//    @POST("getmenubyid.php")         /*method request ke web service, lalu Model mana yg menangkapnya*/
//    Call<MenuValue> getMenuById(@Field("id_produk") String id_produk);
}