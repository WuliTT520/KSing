package com.homework.ksing.ksing.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.homework.ksing.ksing.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateActivity extends AppCompatActivity {
    private TextView back;
    private TextView finish;
    private TextView titleText;
    private EditText editText;

    private SharedPreferences sp;

    private String title;
    private String nr;
    private String url;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
        setListener();

    }
    public void init() {
        back=findViewById(R.id.textView26);
        titleText=findViewById(R.id.textView28);
        finish=findViewById(R.id.textView43);
        editText=findViewById(R.id.editText2);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        title=intent.getExtras().getString("title");
        nr=intent.getExtras().getString("nr");
        url=intent.getExtras().getString("url");
        key=intent.getExtras().getString("key");

        titleText.setText(title);
        editText.setText(nr);
    }
    public void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateActivity.this,EditUserInfoActivity.class);
                startActivity(intent);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updataUserInfo(url,key,editText.getText().toString());
                Intent intent=new Intent(UpdateActivity.this,EditUserInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updataUserInfo(String url,String key,String value) {
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        RequestBody body = new FormBody.Builder()
                .add(key,value).build();
        //创建一个请求对象，传入URL地址和相关数据的键值对的对象
        final Request request = new Request.Builder().addHeader("cookie", sp.getString("sessionID", ""))
                .url(url)
                .post(body).build();

        //创建一个能处理请求数据的操作类
        Call call = client.newCall(request);

        //使用异步任务的模式请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "错误信息：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {



            }
        });
    }
}
