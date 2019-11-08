package com.example.lenovo.dailyfresh.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lenovo.dailyfresh.MainActivity;
import com.example.lenovo.dailyfresh.R;
import com.example.lenovo.dailyfresh.login.ChangeInfoActivity;
import com.lyp.settingbar.SettingBar;
import com.lyp.settingbar.SettingBarModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


public class MeFragment extends Fragment {

	private LinearLayout container_x;
	private List<SettingBar> views;
	private List<SettingBarModel> models;
	private View view;
	private View headerView;

	private String[] titles = {"个人信息","其它选项"};
	private String[] transac_record = new String[100];
	//private String[] rightTextColors = {"#995EAADE", "#992DC100", "#99E6162D"};
	private String usn="";		private String phn="";		private String addr="";
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	final String FILE_NAME = "/dailyfresh_history.bin";
	FileInputStream fis;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		preferences = getActivity().getSharedPreferences("dailyfresh", MODE_PRIVATE);
		editor = preferences.edit();
		usn = preferences.getString("username",null);
		phn = preferences.getString("phonenum",null);
		addr = preferences.getString("address",null);
		//passwd = preferences.getString("password",null);
		for (int i = 0; i < views.size(); i++) {
			final SettingBar view0 = views.get(i);
			if(i==0){
				view0.setRightText(usn);
			}
			else if(i==1){
				view0.setRightText(phn);
			}
			else if(i==2){
				view0.setRightText(addr);
			}
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 接收loginactivity的消息
		Bundle bundle = this.getArguments();
		if(bundle==null) {
			usn = bundle.getString("username");
			phn = bundle.getString("phonenum");
			addr = bundle.getString("address");
		}
		preferences = getActivity().getSharedPreferences("dailyfresh", MODE_PRIVATE);
		editor = preferences.edit();
		usn = preferences.getString("username",null);
		phn = preferences.getString("phonenum",null);
		addr = preferences.getString("address",null);
		// 获取布局文件
		view = inflater.inflate(R.layout.fragment_me,null);
		container_x = (LinearLayout) view.findViewById(R.id.container);


		views = new ArrayList<>();
		for (int i = 0; i < container_x.getChildCount(); i++) {
			views.add((SettingBar) container_x.getChildAt(i));
		}


		// 添加内容
		for (int i = 0; i < views.size(); i++) {

			final SettingBar view0 = views.get(i);
			// 分隔信息
			if (i % 3 == 0) {
				view0.setTopTitle(titles[i / 3]);
			}
			if (i % 3 == 2) {
				view0.setBottomDividerHeight(0);
				// 或view.setBottomDividerVisibility(false);
			}
			//view0.setupModel(models.get(i%3));
			if (i == 0) {
				view0.setLeftIconVisibility(false);
				view0.setLeftText("用户名");
				String usna = usn;
				view0.setRightText(usna);
				if(usn == null)
					Log.e("usnana", "username is "+usn);
				else
					Log.e("bunana", "username is "+usn);

				//view.setRightImage(R.drawable.ic_icon_twitter);
			}

			if (i == 1) {
				view0.setLeftText("手机号码");
				String phone = phn;
                Log.e("phonana", "username is "+usn);
				view0.setRightText(phone);

			}

			if (i == 2) {
				view0.setLeftText("地址");
				String addre = addr;
				view0.setRightText(addre);
			}

			if (i == 3) {
				view0.setLeftText("交易记录");
			}

			view0.setOnBarClickListener(new SettingBar.OnBarClickListener() {
				@Override
				public void onBarClick() {
					// 可以加修改界面
					Toast.makeText(getActivity(), "点击了 " + view0.getLeftText(), Toast.LENGTH_SHORT).show();
					if(view0.getLeftText()!="交易记录"){
						// 进行用户资料的修改
						Intent intent = new Intent(getActivity(),ChangeInfoActivity.class);
						startActivityForResult(intent,1);
					}
					else{
						// searchTradeHistoory()
						// 查看历史交易记录
						File sdCardDir = Environment.getExternalStorageDirectory();
						try {
							String path = "/"+usn;
							File dir = new File(sdCardDir+path);
							if (!dir.exists()) {
								dir.mkdirs();
								File s_file = new File(sdCardDir+path+FILE_NAME);
								if(!s_file.exists()){
									Toast.makeText(getActivity(),"还没买水果呢，去所有水果那里购买吧！", Toast.LENGTH_SHORT).show();

								}
							}
							fis = new FileInputStream(
									sdCardDir.getCanonicalPath()+'/'+usn+FILE_NAME
							);
						} catch (IOException e) {
							e.printStackTrace();
						}
						// 将输入流包装成BufferedReader
						BufferedReader br = new BufferedReader(new InputStreamReader(fis));
						//StringBuilder sb = new StringBuilder();
						String line = null;
						int ii=0;
						// 循环读取文件内容
						try {
							while(!((line = br.readLine()) == null)) {
								transac_record[ii++] = line.replace("#","  ");
								/*if(transac_record[0].equals(usn))
									sb.append(line);*/
							}
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						// 显示
						String toShow = "";
						AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
						for(int i=0;i<ii;i++){
							toShow += transac_record[i];
							toShow += "\n";
						}
						builder.setTitle("交易历史" ) ;
						builder.setMessage(toShow) ;
						builder.setPositiveButton("确认" ,  null );
						builder.setNeutralButton("删除", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								File sdCardDir = Environment.getExternalStorageDirectory();
								File targetFile;
								try {
									fis.close();
									FileOutputStream out = null;
									targetFile = new File(sdCardDir.getCanonicalPath()+'/'+usn+FILE_NAME);
									out = new FileOutputStream(targetFile);
									out.write("".getBytes());
									out.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
						builder.show();
					}
				}
			});
		}
		return view;
	}
}


