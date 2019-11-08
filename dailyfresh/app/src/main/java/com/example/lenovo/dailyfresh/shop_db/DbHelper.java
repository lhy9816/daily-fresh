package com.example.lenovo.dailyfresh.shop_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "shop.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("OnCreate");
        //db.execSQL("drop table shop");
        db.execSQL("Create TABLE shop(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20), balance VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("OnUpgrade");
    }
}

