package com.example.lenovo.dailyfresh.fruit_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "fruit.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("OnCreate");
        //db.execSQL("drop table fruit");
        db.execSQL("Create TABLE fruit(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20), balance INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("OnUpgrade");
    }
}

