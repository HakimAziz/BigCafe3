package com.example.zric7.bigcafe3.Model;

/*
inisiasikan token2 dari data table
*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuModel {
    @SerializedName("id_produk")
    @Expose
    private String id_produk;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("harga_modal")
    @Expose
    private String harga_modal;
    @SerializedName("harga_jual")
    @Expose
    private String harga_jual;
    @SerializedName("ket")
    @Expose
    private String ket;

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getHarga_modal() {
        return harga_modal;
    }

    public void setHarga_modal(String harga_modal) {
        this.harga_modal = harga_modal;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

}

