package com.example.lenovo.dailyfresh.fruit_db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.dailyfresh.R;

import java.util.List;

public class FruitActivity extends AppCompatActivity {
    private List<Fruit> list;
    private FruitDao fruidao;
    private TextView eName;
    private TextView eBalance;
    private ListView listView;
    private FruitAdapter fruitAdapter;
    private  Fruit f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        initView();
        fruidao =new FruitDao(this);
        list=fruidao.getAll();
        fruitAdapter =new FruitAdapter(this,R.layout.fruit_item,list);
        listView.setAdapter(fruitAdapter);
        clearlist();
        addd("苹果","10");
        addd("荔枝","15");
        addd("柠檬","8");
        addd("草莓","16");
        addd("桃子","13");
        addd("李子","9");
        addd("榴莲","20");
        addd("猕猴桃","20");
        addd("柿子","11");
        addd("红毛丹","12");
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list);
        eName = (TextView) findViewById(R.id.ed1);
        eBalance = (TextView) findViewById(R.id.ed2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fruit   fruit = (Fruit) adapterView.getItemAtPosition(i);
            }
        });
    }
    public void add(View view){
        String name =eName.getText().toString();
        String balance = eBalance.getText().toString();
        f =new Fruit(name,balance.equals("")?0:Integer.parseInt(balance));
        fruidao.insert(f);
        list.add(f);
        fruitAdapter.notifyDataSetChanged();
    }
    public void addd(String name,String balance){
        f =new Fruit(name,balance.equals("")?0:Integer.parseInt(balance));
        fruidao.insert(f);
        list.add(f);
        fruitAdapter.notifyDataSetChanged();
    }
    public void clearlist(){
        list.clear();
    }
}

