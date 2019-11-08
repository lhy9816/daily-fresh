package com.example.lenovo.dailyfresh.shop_db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.dailyfresh.R;

import java.util.List;

public class ShopAdapter extends ArrayAdapter<Shop> {
    private int resourceID;
    public ShopAdapter(Context context, int resource, List<Shop> objects) {
        super(context, resource, objects);
        resourceID =resource;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Shop shop = getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceID,null);
        TextView tv_id = (TextView) view.findViewById(R.id.tv11);
        TextView tv_name = (TextView) view.findViewById(R.id.tv21);
        TextView tv_price = (TextView) view.findViewById(R.id.tv31);
        int index = shop.getId().intValue()%3;
        if(index==0) index=3;
        tv_id.setText(String.valueOf(index));
        tv_name.setText(shop.getName());
        tv_price.setText(shop.getBalance()+"");
        return view;
    }
}

