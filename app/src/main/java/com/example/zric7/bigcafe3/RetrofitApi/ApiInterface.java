package com.example.zric7.bigcafe3.RetrofitApi;

import com.example.zric7.bigcafe3.Model.MenuValue;
import com.example.zric7.bigcafe3.Model.OrderModel;
import com.example.zric7.bigcafe3.Model.OrderValue;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

/*
API Interface for manage endpoint
Panggil method request yang akan dijalankan oleh web service
*/

public interface ApiInterface {

    //    Tampilin Data
    @GET("getmenu.php")         /*method request ke web service, lalu Model mana yg menangkapnya*/
    Call<MenuValue> getMenu();

    @FormUrlEncoded
    @POST("getmenubykategori.php")
    Call<MenuValue> getMenuByKategori(
            @Field("kategori") String kategori
    );

    @FormUrlEncoded
    @POST("addmenu.php")
    Call<MenuValue> addMenu(
            @Field("kategori") String kategori,
            @Field("nama") String nama,
//            @Field("foto") String foto,
            @Field("harga_modal") String harga_modal,
            @Field("harga_jual") String harga_jual,
            @Field("ket") String ket
    );

    @FormUrlEncoded
    @POST("updatemenu.php")
    Call<MenuValue> updateMenu(
            @Field("id_produk") String id_produk,
            @Field("kategori") String kategori,
            @Field("nama") String nama,
            @Field("foto") String foto,
            @Field("harga_modal") String harga_modal,
            @Field("harga_jual") String harga_jual,
            @Field("ket") String ket
    );

    //    Edit Data
//    @FormUrlEncoded
//    @POST("updatemenu.php")
//    Call<MenuValue> updateMenu(
//            @Field("id_produk") String id_produk,
//            @Field("id_kategori") String id_kategori,
//            @Field("nama") String nama,
//            @Field("deksripsi") String deskripsi,
//            @Field("foto") String foto,
//            @Field("harga_modal") String harga_modal,
//            @Field("harga_jual") String harga_jual,
//            @Field("stok") String stok
//    );

    //    @Headers("Content-Type: application/json")
//    @FormUrlEncoded
//    @POST("addmenu.php")
//    Call<MenuValue> addMenu(
//            @Field("id_kategori") String id_kategori,
//            @Field("nama") String nama,
//            @Field("deksripsi") String deskripsi,
//            @Field("foto") String foto,
//            @Field("harga_modal") String harga_modal,
//            @Field("harga_jual") String harga_jual,
//            @Field("stok") String stok
//    );



    //    Hapus Data
    @FormUrlEncoded
    @POST("deletemenu.php")
    Call<MenuValue> deleteMenu(@Field("id_produk") String id_produk);

    @FormUrlEncoded
    @POST("searchmenu.php")
    Call<MenuValue> searchMenu(@Field("search") String search);


    //    //    Tampilin Data berdasarkan id yg di klik
//    @POST("getmenubyid.php")         /*method request ke web service, lalu Model mana yg menangkapnya*/
//    Call<MenuValue> getMenuById(@Field("id_produk") String id_produk);

    @FormUrlEncoded
    @POST("addorder.php")
    Call<OrderModel> addOrder(
            @Field("pemesan") String pemesan,
            @Field("detail") String detail,
            @Field("status_order") String status_order,
            @Field("total_harga") Integer total_harga
            );

    //Order_tb api --------------------------

    @FormUrlEncoded
    @POST("getorder.php")
    Call<OrderValue> getOrder(
            @Field("status_order") String status_order
    );

    @FormUrlEncoded
    @POST("updateorderstatus.php")
    Call<OrderValue> updateOrderStatus(
            @Field("id_order") String id_order,
            @Field("status_order") String status_order
    );

    @FormUrlEncoded
    @POST("updateorderpay.php")
    Call<OrderValue> updateOrderPay(
            @Field("id_order") String id_order,
            @Field("u_bayar") String u_bayar,
            @Field("u_kembali") String u_kembali
    );
}