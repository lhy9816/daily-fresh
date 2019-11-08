package com.example.lenovo.dailyfresh.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.lenovo.dailyfresh.R;
import com.example.lenovo.dailyfresh.fruit_db.FruitActivity;
import com.example.lenovo.dailyfresh.map.CustomerGetActivity;
import com.example.lenovo.dailyfresh.shop_db.ShopActivity;


public class HomeFragment extends Fragment {
    private LinearLayout llModule1;
    private LinearLayout llModule2;
    private LinearLayout llModule3;
    private LinearLayout llModule4;
    final private String aboutUs = "这是北航安卓课程的小组课程设计：每日新鲜 1.0，这款APP专为北航内苦于买不到水果的学生所做，虽然距离完整的APP还有些差距，不过我们在设计与制作APP的过程中体会到了开发的乐趣，相信今后可能的话就将这个APP进行一定完善，敬请期待！";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        llModule1 = (LinearLayout) view.findViewById(R.id.ll_module1);
        llModule2 = (LinearLayout) view.findViewById(R.id.ll_module2);
        llModule3 = (LinearLayout) view.findViewById(R.id.ll_module3);
        llModule4 = (LinearLayout) view.findViewById(R.id.ll_module4);
        llModule1.setOnClickListener(new ModuleClickListener());
        llModule2.setOnClickListener(new ModuleClickListener());
        llModule3.setOnClickListener(new ModuleClickListener());
        llModule4.setOnClickListener(new ModuleClickListener());


        return view;
    }


    class ModuleClickListener implements View.OnClickListener {
        Intent intent=new Intent();
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_module1:

                    Toast.makeText(getActivity(), "所有店铺", Toast.LENGTH_SHORT).show();
                    intent.setClass(HomeFragment.this.getActivity(), ShopActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_module2:
                    Toast.makeText(getActivity(), "所有水果", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(HomeFragment.this.getActivity(), FruitActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.ll_module3:
                    Toast.makeText(getActivity(), "用户自取", Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(HomeFragment.this.getActivity(), CustomerGetActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.ll_module4:
                    Toast.makeText(getActivity(), "关于我们", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                    builder.setTitle("关于我们" ) ;
                    builder.setMessage(aboutUs) ;
                    builder.setPositiveButton("确认" ,  null );
                    builder.show();
                    break;
            }
        }
    }
}
