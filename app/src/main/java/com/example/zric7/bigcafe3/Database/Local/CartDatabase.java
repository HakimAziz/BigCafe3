package com.example.zric7.bigcafe3.Database.Local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.zric7.bigcafe3.Database.ModelDB.Cart;

@Database(entities = {Cart.class}, version = 1) /*<- ubah*/
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();
    private static CartDatabase instance;

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("DROP TABLE Cart");
//        }
//    };
//
//    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("" +
//                    "CREATE TABLE Cart" +
//                    " (id int, " +
//                    "name String," +
//                    " foto String, " +
//                    " qty int, " +
//                    " price_item int, " +
//                    " price_total int, " +
//                    "PRIMARY KEY(id))");
//        }
//    };

    //It will create this instance one time, to create the database one time.
    public static CartDatabase getInstance(Context context) {

        if(instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class, "BigCafe_CartOrderDB")
                    .allowMainThreadQueries()
//                    .addMigrations(MIGRATION_1_2) /*<- ubah*/
//                    .addMigrations(MIGRATION_2_3) /*<- ubah*/
                    .build();

        return instance;
    }
}
