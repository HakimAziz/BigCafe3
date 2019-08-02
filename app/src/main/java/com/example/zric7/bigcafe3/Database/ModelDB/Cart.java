package com.example.zric7.bigcafe3.Database.ModelDB;

//
//Ini Struktur database local... buat nyimpen data2 di cart
//

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Cart")

public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "foto")
    public String foto;

    @ColumnInfo(name = "qty")
    public int qty;

    @ColumnInfo(name = "price_item")
    public int price_item;

    @ColumnInfo(name = "price_total")
    public int price_total;
}
