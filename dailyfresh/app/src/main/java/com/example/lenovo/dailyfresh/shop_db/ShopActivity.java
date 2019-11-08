package com.example.lenovo.dailyfresh.shop_db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.dailyfresh.R;

import java.util.List;

public class ShopActivity extends AppCompatActivity {
    private List<Shop> list;
    private ShopDao shopdao;
    private TextView eName;
    private TextView eBalance;
    private ListView listView;
    private ShopAdapter shopAdapter;
    private  Shop f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initView();
        shopdao =new ShopDao(this);
        list=shopdao.getAll();
        shopAdapter =new ShopAdapter(this,R.layout.shop_item,list);
        listView.setAdapter(shopAdapter);
        clearlist();
        addd("北航合一", "东");
        addd("北航大运村", "南");
        addd("北航博彦", "西");
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list1);
        eName = (TextView) findViewById(R.id.ed11);
        eBalance = (TextView) findViewById(R.id.ed21);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Shop   shop = (Shop) adapterView.getItemAtPosition(pos);
            }
        });
    }
    public void add(View view){
        String name =eName.getText().toString();
        String balance = eBalance.getText().toString();
        f =new Shop(name,balance);
        shopdao.insert(f);
        list.add(f);
        shopAdapter.notifyDataSetChanged();
    }
    public void addd(String name,String balance){
        f =new Shop(name,balance);
        shopdao.insert(f);
        list.add(f);
        shopAdapter.notifyDataSetChanged();
    }
    public void clearlist(){
        list.clear();
    }
}


