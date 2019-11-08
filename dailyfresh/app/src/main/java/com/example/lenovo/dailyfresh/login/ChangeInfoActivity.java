package com.example.lenovo.dailyfresh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dailyfresh.R;

import java.sql.Connection;

import static android.content.Context.MODE_PRIVATE;

public class ChangeInfoActivity extends AppCompatActivity {
    private final String URL = "jdbc:mysql://192.168.43.77:3306/loguser";
    private final String USER = "master";
    private final String PASSWORD = "123456";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String usn="";		private String phn="";		private String addre="";    private String passwd = "";

    regCalThread calThread;
    TextView userName;
    TextView pwd;
    TextView phonum;
    TextView addr;
    Button Reg;


    class regCalThread extends Thread {
        public Handler mHandler;
        public void run(){
            Looper.prepare();
            mHandler = new Handler(){
                // 定义处理消息的方法
                @Override
                public void handleMessage(Message msg){
                    if(msg.what==0x125) {
                        String urn = msg.getData().getString("USER");
                        String pwd = msg.getData().getString("PWD");
                        String phonenum = msg.getData().getString("PHONUM");
                        String addre = msg.getData().getString("ADDR");
                        Connection conn = MySqlUtil.openConnection(URL, USER, PASSWORD);
                        if (conn == null)
                            Toast.makeText(ChangeInfoActivity.this, "服务器连接失败了！", Toast.LENGTH_SHORT).show();
                        //Log.e("MAINSQL", "All users info:");
                        boolean sighInres = MySqlUtil.execSQL(conn, "update userlist set password = \"" + pwd +"\", phonenum = \"" + phonenum +"\", address = \"" + addre + "\" where username = \""+urn+"\"");
                        if (sighInres == false)
                            Toast.makeText(ChangeInfoActivity.this, "修改失败，请确认您输入有效的用户名！", Toast.LENGTH_SHORT).show();
                        else {
                            // 修改成功
                            editor.putString("username",urn);
                            editor.putString("phonenum",phonenum);
                            editor.putString("address",addre);
                            editor.putString("password",pwd);
                            editor.commit();
                            Toast.makeText(ChangeInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        //Log.e("MAINSQL", "after insert:"+"insert into logu values("+urn+","+pwd+")");
                        MySqlUtil.query(conn, "select * from logu");
                    }
                }
            };
            Looper.loop();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfo);
        //logIn = (Button)findViewById(R.id.btn_login);
        Reg = (Button)findViewById(R.id.cg_register);
        userName =  (TextView)findViewById(R.id.cg_username);
        pwd = (TextView)findViewById(R.id.cg_password);
        phonum = (TextView)findViewById(R.id.cg_phonenum);
        addr = (TextView)findViewById(R.id.cg_addr);

        preferences = getSharedPreferences("dailyfresh",MODE_PRIVATE);
        editor = preferences.edit();
        usn = preferences.getString("username",null);
        phn = preferences.getString("phonenum",null);
        addre = preferences.getString("address",null);
        passwd = preferences.getString("password",null);

        userName.setText(usn); pwd.setText(passwd); phonum.setText(phn);  addr.setText(addre);

        calThread = new regCalThread();
        calThread.start();
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getEditableText().toString().trim();
                String password = pwd.getEditableText().toString().trim();
                String phonumber = phonum.getEditableText().toString().trim();
                String address = addr.getEditableText().toString().trim();
                if(username.equals("") || password.equals("")|| phonumber.equals("")|| address.equals("")){
                    Toast.makeText(ChangeInfoActivity.this, "请输入用户名或密码！",Toast.LENGTH_SHORT).show();
                }
                else{
                    Message msg = new Message();
                    msg.what = 0x125;
                    Bundle bundle = new Bundle();
                    bundle.putString("USER",username);
                    bundle.putString("PWD",password);
                    bundle.putString("PHONUM",phonumber);
                    bundle.putString("ADDR",address);
                    msg.setData(bundle);
                    calThread.mHandler.sendMessage(msg);
                }
            }
        });
    }
}

