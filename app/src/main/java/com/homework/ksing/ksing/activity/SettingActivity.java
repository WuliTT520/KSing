package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.controller.MyURL;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingActivity extends Activity {
    ImageView fanhui;
    LinearLayout userinfo;
    Button logout;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        setListener();
    }
    public void init(){
        fanhui=findViewById(R.id.fanhui);
        userinfo=findViewById(R.id.userinfo);
        logout=findViewById(R.id.logout);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }
    public void setListener(){
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*展示具体内容并提供修改*/
        userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*将本地的sp中的数据删除，将服务器上的数据删除*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyURL myURL=new MyURL();
                final OkHttpClient client = new OkHttpClient();
                final Request request=new Request.Builder()
                        .addHeader("cookie",sp.getString("sessionID",""))
                        .url(myURL.logout())
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
                editor = sp.edit();
                editor.putString("sessionID","");
                editor.putBoolean("isLogin",false);
                editor.commit();
                Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
