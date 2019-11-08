package com.example.lenovo.dailyfresh.fruit_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FruitDao {
    private DbHelper dbHelper;

    public FruitDao(Context context) {
        dbHelper =new DbHelper(context);
    }
    public void insert(Fruit fruit){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("name",fruit.getName());
        values.put("balance",fruit.getBalance());
        long id = db.insert("fruit",null,values);
        fruit.setId(id);
        db.close();
    }
    //根据id删除数据
    public  int delete(long id){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        int count=db.delete("fruit","id=?",new String[]{id+""});
        db.close();
        return count;
    }
    public List<Fruit> getAll(){
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        Cursor c =db.query("fruit",null,null,null,null,null,"balance DESC");
        List<Fruit> list =new ArrayList<>();
        while (c.moveToNext()){
            long id =c.getLong(c.getColumnIndex("id"));
            String name = c.getString(1);
            int balance =c.getInt(2);
            list.add(new Fruit(id,name,balance));
        }
        c.close();
        db.close();
        return list;
    }
}

