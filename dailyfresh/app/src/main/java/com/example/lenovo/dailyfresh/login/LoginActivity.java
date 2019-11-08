package com.example.lenovo.dailyfresh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dailyfresh.MainActivity;
import com.example.lenovo.dailyfresh.R;

import java.sql.Connection;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class LoginActivity extends AppCompatActivity {
    private final String URL = "jdbc:mysql://192.168.43.77:3306/loguser";
    private final String USER = "master";
    private final String PASSWORD = "123456";
    logCalThread calThread;
    TextView userName;
    TextView pwd;
    Button logIn;
    Button Reg;
    CheckBox remPwd;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // 背景类所需变量
    public static final String VIDEO_NAME = "welcome_video_fruit.mp4";

    private VideoView mVideoView;

    private InputType inputType = InputType.NONE;

    private ViewGroup contianer;

    private TextView appName;

    class logCalThread extends Thread {
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
                        Connection conn = MySqlUtil.openConnection(URL, USER, PASSWORD);
                        if(conn==null)
                            Toast.makeText(LoginActivity.this,"服务器连接失败了！",Toast.LENGTH_SHORT).show();
                        //Log.e("MAINSQL", "All users info:");
                        boolean sighInres = MySqlUtil.execSQL(conn,"insert into logu values('"+urn+"','"+pwd+"')");
                        if(sighInres==false)
                            Toast.makeText(LoginActivity.this,"该用户名已经存在，请重新注册！",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                        //Log.e("MAINSQL", "after insert:"+"insert into logu values("+urn+","+pwd+")")
                    }
                    else if(msg.what==0x124) {
                        String urn = msg.getData().getString("USER");
                        String pwd = msg.getData().getString("PWD");
                        Connection conn = MySqlUtil.openConnection(URL, USER, PASSWORD);
                        if(conn==null)
                            Toast.makeText(LoginActivity.this,"服务器连接失败了！",Toast.LENGTH_SHORT).show();
                        Log.e("MAINSQL", "All users info:");
                        int loginRes = MySqlUtil.userLogin(conn, urn, pwd);     //登录结果
                        switch (loginRes){
                            case -1: Toast.makeText(LoginActivity.this, "服务器连接失败了！",Toast.LENGTH_SHORT).show(); break;
                            case 0: Toast.makeText(LoginActivity.this, "登陆成功！",Toast.LENGTH_SHORT).show();  break;//后面怎么办呢？
                            case 1: Toast.makeText(LoginActivity.this, "没有此用户，请重新登录！",Toast.LENGTH_SHORT).show(); break;
                            case 3: Toast.makeText(LoginActivity.this, "密码错误，请重新输入！",Toast.LENGTH_SHORT).show(); break;
                            default: break;
                        }
                        if(loginRes == 0) {
                            String[] result_str = MySqlUtil.query(conn, "select * from userlist where username =" + urn);
                            Bundle data = new Bundle();
                            if(remPwd.isChecked()){
                                sharedPreferences = getSharedPreferences("newpwd",MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putBoolean("iskeep",true);
                                editor.putString("username",result_str[0]);
                                editor.putString("password",pwd);
                                editor.commit();
                            }
                            else{
                                sharedPreferences = getSharedPreferences("newpwd",MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putBoolean("iskeep",false);
                                editor.putString("username","");
                                editor.putString("password","");
                                editor.commit();
                            }
                            data.putString("username", result_str[0]);
                            data.putString("phonenum", result_str[1]);
                            data.putString("address", result_str[2]);
                            data.putString("password",pwd);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtras(data);
                            startActivity(intent);
                        }
                    }
                }
            };
            Looper.loop();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_login);
        // 隐藏头顶actionbar
        getSupportActionBar().hide();

        File videoFile = getFileStreamPath(VIDEO_NAME);
        if (true/*!videoFile.exists()*/) {
            videoFile = copyVideoFile();
        }
        mVideoView = (VideoView) findViewById(R.id.videoView);
        contianer = (ViewGroup) findViewById(R.id.login_container);
        appName = (TextView) findViewById(R.id.appName);

        playVideo(videoFile);

        playAnim();

        // 登录变量的获取
        logIn = (Button)findViewById(R.id.btn_login);
        Reg = (Button)findViewById(R.id.new_usr);
        userName =  (TextView)findViewById(R.id.et_username);
        pwd = (TextView)findViewById(R.id.et_password);
        remPwd = (CheckBox)findViewById(R.id.chb_rempwd);
        calThread = new logCalThread();
        calThread.start();

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,0);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getEditableText().toString().trim();
                String password = pwd.getEditableText().toString().trim();
                /*String[] result_str = {"Dj","15611775298","sjs","dfsf"};
                Bundle data = new Bundle();
                data.putString("username", result_str[0]);
                data.putString("phonenum", result_str[1]);
                data.putString("address", result_str[2]);
                data.putString("password", result_str[3]);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtras(data);
                startActivity(intent);*/
                if(username.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "请输入用户名或密码！",Toast.LENGTH_SHORT).show();
                }
                else{
                    Message msg = new Message();
                    msg.what = 0x124;
                    Bundle bundle = new Bundle();
                    bundle.putString("USER",username);
                    bundle.putString("PWD",password);
                    msg.setData(bundle);
                    calThread.mHandler.sendMessage(msg);
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        sharedPreferences = getSharedPreferences("newpwd",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("iskeep",false)){
            userName.setText(sharedPreferences.getString("username",""));
            pwd.setText(sharedPreferences.getString("password",""));
        }
        else{
            userName.setText("");
            pwd.setText("");
        }
    }

    private void playVideo(File videoFile) {
        mVideoView.setVideoPath(videoFile.getPath());
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
    }
    @NonNull
    private File copyVideoFile() {
        File videoFile;
        try {
            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
            InputStream in = getResources().openRawResource(R.raw.welcome_video_fruit);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists())
            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
        return videoFile;
    }
    private void playAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(appName, "alpha", 0,1,1);
        anim.setDuration(4000);
        anim.setRepeatCount(1);
        //anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                appName.setVisibility(View.VISIBLE);
            }
        });
    }

    enum InputType {
        NONE, LOGIN, SIGN_UP;
    }
}































/*
public class LoginActivity extends AppCompatActivity {
    private final String URL = "jdbc:mysql://192.168.43.77:3306/loguser";
    private final String USER = "master";
    private final String PASSWORD = "123456";
    logCalThread calThread;
    TextView userName;
    TextView pwd;
    Button logIn;
    Button Reg;
    CheckBox remPwd;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    class logCalThread extends Thread {
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
                        Connection conn = MySqlUtil.openConnection(URL, USER, PASSWORD);
                        if(conn==null)
                            Toast.makeText(LoginActivity.this,"服务器连接失败了！",Toast.LENGTH_SHORT).show();
                        //Log.e("MAINSQL", "All users info:");
                        boolean sighInres = MySqlUtil.execSQL(conn,"insert into logu values('"+urn+"','"+pwd+"')");
                        if(sighInres==false)
                            Toast.makeText(LoginActivity.this,"该用户名已经存在，请重新注册！",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                        //Log.e("MAINSQL", "after insert:"+"insert into logu values("+urn+","+pwd+")")
                    }
                    else if(msg.what==0x124) {
                        String urn = msg.getData().getString("USER");
                        String pwd = msg.getData().getString("PWD");
                        Connection conn = MySqlUtil.openConnection(URL, USER, PASSWORD);
                        if(conn==null)
                            Toast.makeText(LoginActivity.this,"服务器连接失败了！",Toast.LENGTH_SHORT).show();
                        Log.e("MAINSQL", "All users info:");
                        int loginRes = MySqlUtil.userLogin(conn, urn, pwd);     //登录结果
                        switch (loginRes){
                            case -1: Toast.makeText(LoginActivity.this, "服务器连接失败了！",Toast.LENGTH_SHORT).show(); break;
                            case 0: Toast.makeText(LoginActivity.this, "登陆成功！",Toast.LENGTH_SHORT).show();  break;//后面怎么办呢？
                            case 1: Toast.makeText(LoginActivity.this, "没有此用户，请重新登录！",Toast.LENGTH_SHORT).show(); break;
                            case 3: Toast.makeText(LoginActivity.this, "密码错误，请重新输入！",Toast.LENGTH_SHORT).show(); break;
                            default: break;
                        }
                        if(loginRes == 0) {
                            String[] result_str = MySqlUtil.query(conn, "select * from userlist where username =" + urn);
                            Bundle data = new Bundle();
                            data.putString("username", result_str[0]);
                            data.putString("phonenum", result_str[1]);
                            data.putString("address", result_str[2]);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtras(data);
                            startActivity(intent);
                        }
                    }
                }
            };
            Looper.loop();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logIn = (Button)findViewById(R.id.btn_login);
        Reg = (Button)findViewById(R.id.new_usr);
        userName =  (TextView)findViewById(R.id.et_username);
        pwd = (TextView)findViewById(R.id.et_password);
        remPwd = (CheckBox)findViewById(R.id.chb_rempwd);
        calThread = new logCalThread();
        calThread.start();

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,0);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getEditableText().toString().trim();
                String password = pwd.getEditableText().toString().trim();
                if(username.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "请输入用户名或密码！",Toast.LENGTH_SHORT).show();
                }
                else{
                    Message msg = new Message();
                    msg.what = 0x124;
                    Bundle bundle = new Bundle();
                    bundle.putString("USER",username);
                    bundle.putString("PWD",password);
                    msg.setData(bundle);
                    calThread.mHandler.sendMessage(msg);
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        sharedPreferences = getSharedPreferences("newpwd",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("iskeep",false)){
            userName.setText(sharedPreferences.getString("username",""));
            pwd.setText(sharedPreferences.getString("password",""));
        }
        else{
            userName.setText("");
            pwd.setText("");
        }
    }
}
*/
