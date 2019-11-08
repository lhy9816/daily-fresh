package com.example.lenovo.dailyfresh.fruit_db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dailyfresh.MainActivity;
import com.example.lenovo.dailyfresh.fragment.ShopCarFragment;
import com.example.lenovo.dailyfresh.R;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceID;
    Context context1;
    public FruitAdapter(Context context, int resource, List<Fruit> objects) {
        super(context, resource, objects);
        this.context1 = context;
        resourceID =resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fruit fruit = getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceID,null);
        TextView tv_id = (TextView) view.findViewById(R.id.tv1);
        TextView tv_name = (TextView) view.findViewById(R.id.tv2);
        TextView tv_price = (TextView) view.findViewById(R.id.tv3);
        tv_id.setText("购买");
        final String name = fruit.getName();
        final int price = fruit.getBalance();
        tv_name.setText(fruit.getName());
        tv_price.setText(fruit.getBalance()+"");
        tv_id.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){
                Toast.makeText(context1,"已加入购物车，等亲", Toast.LENGTH_SHORT).show();
                //ShoppingCartActivity.instance.add_fruit(name,price);
                ShopCarFragment.instance.add_fruit(name,price);
                //MainActivity.instance.add_fruit(name,price);
            }
        });
        return view;

    }
}

