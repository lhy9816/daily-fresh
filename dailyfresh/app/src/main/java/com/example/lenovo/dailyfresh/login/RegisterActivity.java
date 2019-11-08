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

public class RegisterActivity extends AppCompatActivity {
    private final String URL = "jdbc:mysql://192.168.43.77:3306/loguser";
    private final String USER = "master";
    private final String PASSWORD = "123456";
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
                    if(msg.what==0x123) {
                        String urn = msg.getData().getString("USER");
                        String pwd = msg.getData().getString("PWD");
                        String phonenum = msg.getData().getString("PHONUM");
                        String addre = msg.getData().getString("ADDR");
                        Connection conn = MySqlUtil.openConnection(URL, USER, PASSWORD);
                        if(conn==null)
                            Toast.makeText(RegisterActivity.this,"服务器连接失败了！",Toast.LENGTH_SHORT).show();
                        //Log.e("MAINSQL", "All users info:");
                        boolean sighInres = MySqlUtil.execSQL(conn,"insert into userlist values('"+urn+"','"+pwd+"','"+phonenum+"','"+addre+"')");
                        if(sighInres==false)
                            Toast.makeText(RegisterActivity.this,"该用户名已经存在，请重新注册！",Toast.LENGTH_SHORT).show();
                        else{
                            Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        //Log.e("MAINSQL", "after insert:"+"insert into logu values("+urn+","+pwd+")");
                        MySqlUtil.query(conn, "select * from logu");
                    }
                    else if(msg.what==0x124) {
                        String urn = msg.getData().getString("USER");
                        String pwd = msg.getData().getString("PWD");
                        String phonenum = msg.getData().getString("PHONUM");
                        String addre = msg.getData().getString("ADDR");
                        Connection conn = MySqlUtil.openConnection(URL, USER, PASSWORD);
                        if(conn==null)
                            Toast.makeText(RegisterActivity.this,"服务器连接失败了！",Toast.LENGTH_SHORT).show();
                        Log.e("MAINSQL", "All users info:");
                        int loginRes = MySqlUtil.userLogin(conn, urn, pwd);     //登录结果
                        switch (loginRes){
                            case -1: Toast.makeText(RegisterActivity.this, "服务器连接失败了！",Toast.LENGTH_SHORT).show(); break;
                            case 0: {
                                Toast.makeText(RegisterActivity.this, "登陆成功！",Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            }//后面怎么办呢？
                            case 1: Toast.makeText(RegisterActivity.this, "没有此用户，请重新登录！",Toast.LENGTH_SHORT).show(); break;
                            case 3: Toast.makeText(RegisterActivity.this, "密码错误，请重新输入！",Toast.LENGTH_SHORT).show(); break;
                            default: break;
                        }
                        Log.e("MAINSQL", "after insert:"+"insert into logu values("+urn+","+pwd+")");
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
        setContentView(R.layout.activity_reg);
        //logIn = (Button)findViewById(R.id.btn_login);
        Reg = (Button)findViewById(R.id.rg_register);
        userName =  (TextView)findViewById(R.id.rg_username);
        pwd = (TextView)findViewById(R.id.rg_password);
        phonum = (TextView)findViewById(R.id.rg_phonenum);
        addr = (TextView)findViewById(R.id.rg_addr);
        //remPwd = (CheckBox)findViewById(R.id.chb_rempwd);
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
                    Toast.makeText(RegisterActivity.this, "请输入用户名或密码！",Toast.LENGTH_SHORT).show();
                }
                else{
                    Message msg = new Message();
                    msg.what = 0x123;
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
