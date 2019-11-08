package com.example.lenovo.dailyfresh.shop_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShopDao {
    private com.example.lenovo.dailyfresh.shop_db.DbHelper dbHelper;

    public ShopDao(Context context) {
        dbHelper =new com.example.lenovo.dailyfresh.shop_db.DbHelper(context);
    }
    public void insert(Shop shop){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("name",shop.getName());
        values.put("balance",shop.getBalance());
        long id = db.insert("shop",null,values);
        shop.setId(id);
        db.close();
    }
    //根据id删除数据
    public  int delete(long id){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        int count=db.delete("shop","id=?",new String[]{id+""});
        db.close();
        return count;
    }
    public List<Shop> getAll(){
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        Cursor c =db.query("shop",null,null,null,null,null,"balance DESC");
        List<Shop> list =new ArrayList<>();
        while (c.moveToNext()){
            long id =c.getLong(c.getColumnIndex("id"));
            String name = c.getString(1);
            String balance =c.getString(2);
            list.add(new Shop(id,name,balance));
        }
        c.close();
        db.close();
        return list;
    }
}

