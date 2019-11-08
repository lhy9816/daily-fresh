package com.example.lenovo.dailyfresh.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dailyfresh.MainActivity;
import com.example.lenovo.dailyfresh.R;
import com.example.lenovo.dailyfresh.shopcar.ShoppingCartAdapter;
import com.example.lenovo.dailyfresh.shopcar.ShoppingCartBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SHORTCUT_SERVICE;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ShopCarFragment extends Fragment implements View.OnClickListener{

	// 传递历史信息
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String usn="";		private String phn="";		private String addr="";

	private View view;
	Button tmp_button;
	SmsManager sManager;

    final String FILE_NAME = "/dailyfresh_history.bin";
    File targetFile;
    RandomAccessFile raf;
    public String perchase_info;

	// shopping car
	public static ShopCarFragment instance = null;
	public TextView tv_title, tv_settlement, tv_show_price;
	public MainActivity m_instance = MainActivity.instance;
	public TextView tv_all_check;
	public CheckBox ck_all;
	public ListView list_shopping_cart;
	public ShoppingCartAdapter shoppingCartAdapter;
	public TextView tv_edit;
	public boolean flag = false;
	public static List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
	public ShoppingCartBean scb;
	public double totalPrice = 0.00;// 购买的商品总价
	public int totalCount = 0;// 购买的商品总数量
	public String one_save;


	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_shop_car,null);
		initView_shopcar();
		// 获得用户名，地址和手机号
		/*sManager = SmsManager.getDefault();
		preferences = this.getActivity().getSharedPreferences("dailyfresh",MODE_PRIVATE);
		editor = preferences.edit();
		usn = preferences.getString("username",null);
		phn = preferences.getString("phonenum",null);
		addr = preferences.getString("address",null);*/

		// 发送短信
		/*tmp_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PendingIntent pi = PendingIntent.getActivity(getContext(),0, new Intent(),0);
				sManager.sendTextMessage(phn,null,usn+phn+addr,pi,null);
				Toast.makeText(getActivity(),"短信发送成功！",Toast.LENGTH_SHORT).show();

				// 加入向手机中写入交易记录的逻辑
				File sdCardDir = Environment.getExternalStorageDirectory();
				try {
					targetFile = new File(sdCardDir.getCanonicalPath()+FILE_NAME);
					raf = new RandomAccessFile(targetFile,"rw");
					raf.seek(targetFile.length());
					raf.write("123 iji woi\n".getBytes());
					raf.write("baba qaz qaz\n".getBytes());
					raf.write("baba wsx wsx\n".getBytes());
					raf.write("baba edc edc\n".getBytes());
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
		});*/

		return view;

	}


	public void initShop(){}


	/*protected int getLayout() {
		return R.layout.activity_shopcar;
	}*/
	protected <T extends View> T bindView(int id) {
		return (T) view.findViewById(id);
	}

	protected void initView_shopcar() {
		instance = this;
		tv_title = bindView(R.id.tv_title);
		tv_title.setText("购物车");
		list_shopping_cart = bindView(R.id.list_shopping_cart);
//        list_shopping_cart.setOnItemClickListener(this);
		ck_all = bindView(R.id.ck_all);
		ck_all.setOnClickListener(this);
//        ck_all.setOnCheckedChangeListener(this);
		tv_show_price = bindView(R.id.tv_show_price);
		tv_settlement = bindView(R.id.tv_settlement);
		tv_settlement.setOnClickListener(this);
		tv_edit = bindView(R.id.tv_edit);
		tv_edit.setOnClickListener(this);
		sManager = SmsManager.getDefault();

		shoppingCartAdapter = new ShoppingCartAdapter(view.getContext());
		if(m_instance!=null){
			shoppingCartAdapter.setCheckInterface(m_instance);
			shoppingCartAdapter.setModifyCountInterface(m_instance);
		}
		list_shopping_cart.setAdapter(shoppingCartAdapter);
		shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);
	}

	public void add_fruit(String name,int price){
		ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
		shoppingCartBean.setShoppingName(name);
		shoppingCartBean.setFabric("北航");
//        shoppingCartBean.setDressSize(48);
//        shoppingCartBean.setPantsSize(65);
		shoppingCartBean.setPrice(price);
		shoppingCartBean.setCount(1);
		shoppingCartBeanList.add(shoppingCartBean);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//全选按钮
			case R.id.ck_all:
				if (shoppingCartBeanList.size() != 0) {
					if (ck_all.isChecked()) {
						for (int i = 0; i < shoppingCartBeanList.size(); i++) {
							shoppingCartBeanList.get(i).setChoosed(true);
						}
						shoppingCartAdapter.notifyDataSetChanged();
					} else {
						for (int i = 0; i < shoppingCartBeanList.size(); i++) {
							shoppingCartBeanList.get(i).setChoosed(false);
						}
						shoppingCartAdapter.notifyDataSetChanged();
					}
				}
				statistics();
				break;
			case R.id.tv_edit:
				flag = !flag;
				if (flag) {
					tv_edit.setText("完成");
					shoppingCartAdapter.isShow(false);
				} else {
					tv_edit.setText("编辑");
					shoppingCartAdapter.isShow(true);
				}
				break;
			case R.id.tv_settlement:
				sendMessage();

				break;
		}
	}

	public void sendMessage (){
		// 获得用户名及时间
		preferences = this.getActivity().getSharedPreferences("dailyfresh",MODE_PRIVATE);
		editor = preferences.edit();
		usn = preferences.getString("username",null);
		phn = preferences.getString("phonenum",null);
		addr = preferences.getString("address",null);
		scb = new ShoppingCartBean();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

		// message 准备
		int cnt;
		String item;
		one_save = df.format(new Date())+'#';
		for (int i = 0; i < shoppingCartBeanList.size(); i++) {
			cnt = shoppingCartBeanList.get(i).getCount();
			item = shoppingCartBeanList.get(i).getShoppingName();
			one_save += item + ' ' + cnt + '#';
		}
		// 总钱数
		one_save += "共 "+totalPrice+" 元\n";
		// 加入向手机中写入交易记录的逻辑
		String[] deal_info = new String[30];
		String toPhone = new String();
		File sdCardDir = Environment.getExternalStorageDirectory();
		try {
			String path = "/"+usn;
			File dir = new File(sdCardDir+path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			targetFile = new File(sdCardDir.getCanonicalPath()+'/'+usn+FILE_NAME);
			raf = new RandomAccessFile(targetFile,"rw");
			raf.seek(targetFile.length());
			raf.write((one_save).getBytes());
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		deal_info = one_save.split("#");
		for(int i =0;i<deal_info.length;i++){
			toPhone += deal_info[i]+"\n";
		}
		PendingIntent pi = PendingIntent.getActivity(getContext(),0, new Intent(),0);
		sManager.sendTextMessage(phn,null,toPhone,pi,null);
		Toast.makeText(getActivity(),"短信发送成功！",Toast.LENGTH_SHORT).show();
		// 清空购物车
		flag = !flag;
		if (flag||!flag) {
			tv_edit.setText("完成");
			shoppingCartAdapter.isShow(false);
		}

	}
	/*public void checkGroup(int position, boolean isChecked) {

		shoppingCartBeanList.get(position).setChoosed(isChecked);

		if (isAllCheck())
			ck_all.setChecked(true);
		else
			ck_all.setChecked(false);

		shoppingCartAdapter.notifyDataSetChanged();
		statistics();
	}*/


	/*private boolean isAllCheck() {

		for (ShoppingCartBean group : shoppingCartBeanList) {
			if (!group.isChoosed())
				return false;
		}
		return true;
	}*/


	public void statistics() {
		totalCount = 0;
		totalPrice = 0.00;
		for (int i = 0; i < shoppingCartBeanList.size(); i++) {
			ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
			if (shoppingCartBean.isChoosed()) {
				totalCount++;
				totalPrice += shoppingCartBean.getPrice() * shoppingCartBean.getCount();
			}
		}
		tv_show_price.setText("合计:" + totalPrice);
		tv_settlement.setText("结算(" + totalCount + ")");
	}

	/*public void doIncrease(int position, View showCountView, boolean isChecked) {
		ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
		int currentCount = shoppingCartBean.getCount();
		currentCount++;
		shoppingCartBean.setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");
		shoppingCartAdapter.notifyDataSetChanged();
		statistics();
	}




	public ShopCarFragment() {
		
	}
*/
}
