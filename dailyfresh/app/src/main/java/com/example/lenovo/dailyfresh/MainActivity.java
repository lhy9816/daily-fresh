package com.example.lenovo.dailyfresh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dailyfresh.fragment.HomeFragment;
import com.example.lenovo.dailyfresh.fragment.MeFragment;
import com.example.lenovo.dailyfresh.fragment.SearchFragment;
import com.example.lenovo.dailyfresh.fragment.ShopCarFragment;
import com.example.lenovo.dailyfresh.fragment.SortFragment;
import com.example.lenovo.dailyfresh.shopcar.ShoppingCartAdapter;
import com.example.lenovo.dailyfresh.shopcar.ShoppingCartBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener, ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {

    public static MainActivity instance;

    private ViewPager main_viewPager;
    //RadioGroup???
    private RadioGroup main_tab_RadioGroup;
    //RadioButton???
    private RadioButton radio_home, radio_shopcar,
            radio_sort, radio_me, radio_search;
    RadioButton[] rb = new RadioButton[5];
    Drawable[] drawables;
    Button back, showlist;
    //
    private ArrayList<Fragment> fragmentList;

    // 保存传入的用户名和密码
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    private String usn;
    private String phn;
    private String adr;
    private String pwd;

   // shopping car
    public ShopCarFragment s_instance;


    @Override
    public void checkGroup(int position, boolean isChecked) {
        s_instance = ShopCarFragment.instance;
        s_instance.shoppingCartBeanList.get(position).setChoosed(isChecked);

        if (isAllCheck())
            s_instance.ck_all.setChecked(true);
        else
            s_instance.ck_all.setChecked(false);

        s_instance.shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }


    private boolean isAllCheck() {
        s_instance = ShopCarFragment.instance;
        for (ShoppingCartBean group : s_instance.shoppingCartBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }


    public void statistics() {
        s_instance = ShopCarFragment.instance;
        s_instance.totalCount = 0;
        s_instance.totalPrice = 0.00;
        for (int i = 0; i < s_instance.shoppingCartBeanList.size(); i++) {
            ShoppingCartBean shoppingCartBean = s_instance.shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                s_instance.totalCount++;
                s_instance.totalPrice += shoppingCartBean.getPrice() * shoppingCartBean.getCount();
            }
        }
        s_instance.tv_show_price.setText("合计:" + s_instance.totalPrice);
        s_instance.tv_settlement.setText("结算(" + s_instance.totalCount + ")");
    }

    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        s_instance = ShopCarFragment.instance;
        ShoppingCartBean shoppingCartBean = s_instance.shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        currentCount++;
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        s_instance.shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }


    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        s_instance = ShopCarFragment.instance;
        ShoppingCartBean shoppingCartBean = s_instance.shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        s_instance.shoppingCartAdapter.notifyDataSetChanged();
        statistics();

    }


    @Override
    public void childDelete(int position) {
        s_instance = ShopCarFragment.instance;
        s_instance.shoppingCartBeanList.remove(position);
        s_instance.shoppingCartAdapter.notifyDataSetChanged();
        statistics();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        instance = this;
        s_instance = ShopCarFragment.instance;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 界面初始函数，用来获取定义的各控件对应的ID
        InitView();
// ViewPager初始化函数
        InitViewPager();

        // 获取LoginActivity传过来的intent
        SaveUserInfo();


    }
    public void InitView() {
        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.main_tab_RadioGroup);
        radio_home = (RadioButton) findViewById(R.id.radio_home);
        radio_shopcar = (RadioButton) findViewById(R.id.radio_shopcar);
        radio_sort = (RadioButton) findViewById(R.id.radio_sort);
        radio_search = (RadioButton) findViewById(R.id.radio_search);
        radio_me = (RadioButton) findViewById(R.id.radio_me);
        rb[0]=radio_home;
        rb[1]=radio_shopcar;
        rb[2]=radio_sort;
        rb[3]=radio_search;
        rb[4]=radio_me;
        back = (Button) findViewById(R.id.button_backward);
        showlist = (Button) findViewById(R.id.button_list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"功能仍需完善，请点击其他按钮！", Toast.LENGTH_SHORT).show();
            }
        });
        for(int i=0;i<5;i++){
            drawables = rb[i].getCompoundDrawables();
            Rect r = new Rect(0,0,drawables[1].getMinimumWidth()*3/5,drawables[1].getMinimumHeight()*3/5);
            drawables[1].setBounds(r);
            rb[i].setCompoundDrawables(null,drawables[1],null,null);
        }
        main_tab_RadioGroup.setOnCheckedChangeListener(this);
    }

    public void InitViewPager() {
        main_viewPager = (ViewPager) findViewById(R.id.main_ViewPager);
        fragmentList = new ArrayList<Fragment>();
        Fragment homeFragment = new HomeFragment();
        Fragment sortFragment = new SortFragment();
        Fragment shopCarFragment = new ShopCarFragment();
        Fragment searchFragment = new SearchFragment();
        Fragment meFragment = new MeFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(shopCarFragment);
        fragmentList.add(sortFragment);
        fragmentList.add(searchFragment);
        fragmentList.add(meFragment);
        main_viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        main_viewPager.setCurrentItem(0);
        main_viewPager.addOnPageChangeListener(new MyListner());
        s_instance = ShopCarFragment.instance;
    }
    public void SaveUserInfo(){
        // 获取本地preferences
        preferences = getSharedPreferences("dailyfresh",MODE_PRIVATE);
        editor = preferences.edit();
        Intent intent = getIntent();
        usn = intent.getExtras().getString("username");
        phn = intent.getExtras().getString("phonenum");
        adr = intent.getExtras().getString("address");
        pwd = intent.getExtras().getString("password");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("username", usn);
        bundle.putString("phonenum", phn);
        bundle.putString("address", adr);
        editor.putString("username",usn);
        editor.putString("phonenum",phn);
        editor.putString("address",adr);
        editor.putString("password",pwd);
        editor.commit();

        fragmentList.get(4).setArguments(bundle);
        //ft.add(R.id.main_ViewPager, fragmentList.get(4));
        //ft.commit();
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }

    public class MyListner implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            int current = main_viewPager.getCurrentItem();
            switch (current) {
                case 0:
                    main_tab_RadioGroup.check(R.id.radio_home);
                    break;
                case 1:
                    main_tab_RadioGroup.check(R.id.radio_shopcar);
                    break;
                case 2:
                    main_tab_RadioGroup.check(R.id.radio_sort);
                    break;
                case 3:
                    main_tab_RadioGroup.check(R.id.radio_search);
                    break;
                case 4:
                    main_tab_RadioGroup.check(R.id.radio_me);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        int current = 0;
        switch (checkId) {
            case R.id.radio_home:
                current = 0;
                break;
            case R.id.radio_shopcar:
                current = 1;
                break;
            case R.id.radio_sort:
                current = 2;
                break;
            case R.id.radio_search:
                current = 3;
                break;
            case R.id.radio_me:
                current = 4;
                break;
        }
        if (main_viewPager.getCurrentItem() != current) {
            main_viewPager.setCurrentItem(current);
        }
    }

}



