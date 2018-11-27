package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.ui.WeiboActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KgLoginActivity extends Activity {
    private EditText usercode;
    private EditText password;
    private Button login;
    private String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kg_login);
        usercode=findViewById(R.id.usercode);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(KgLoginActivity.this, WeiboActivity.class);
                startActivity(intent);
                /*
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(5, TimeUnit.SECONDS)
                        .build();

                //post请求来获得数据
                //创建一个RequestBody，存放重要数据的键值对
                RequestBody body = new FormBody.Builder()
                        .add("uid", usercode.getText().toString())
                        .add("pwd", password.getText().toString()).build();
                //创建一个请求对象，传入URL地址和相关数据的键值对的对象
                Request request = new Request.Builder()
                        .url("http://192.168.88.1:8080/jqueryWork/CheckUser")
                        .post(body).build();

                //创建一个能处理请求数据的操作类
                Call call = client.newCall(request);

                //使用异步任务的模式请求数据
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("TAG","错误信息：" + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        flag=response.body().string();
                        if(flag.equals("1")){
                            Intent intent=new Intent(KgLoginActivity.this,LoginActivity.class);
                            startActivity(intent);
                            Looper.prepare();
                            Toast.makeText(KgLoginActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                            Looper.loop();


                        }else{
                            Looper.prepare();
                            Toast.makeText(KgLoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
                */
            }
        });
    }
}
