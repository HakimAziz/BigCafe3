package com.example.zric7.bigcafe3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderModel {

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getStatus_order() {
        return status_order;
    }

    public void setStatus_order(String status_order) {
        this.status_order = status_order;
    }

    public String getPemesan() {
        return pemesan;
    }

    public void setPemesan(String pemesan) {
        this.pemesan = pemesan;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    @SerializedName("id_order")
    @Expose
    private String id_order;
    @SerializedName("status_order")
    @Expose
    private String status_order;
    @SerializedName("pemesan")
    @Expose
    private String pemesan;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("total_harga")
    @Expose
    private String total_harga;

}