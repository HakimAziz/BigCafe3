package com.example.zric7.bigcafe3.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.zric7.bigcafe3.Database.ModelDB.Cart;

@Database(entities = {Cart.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();
    private static CartDatabase instance;

    //It will create this instance one time, to create the database one time.
    public static CartDatabase getInstance(Context context) {

        if(instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class, "BigCafe_CartOrderDB")
                    .allowMainThreadQueries()
                    .build();

        return instance;
    }
}
