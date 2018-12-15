package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.Service.GetMsgService;
import com.homework.ksing.ksing.controller.MyURL;
import com.homework.ksing.ksing.ui.EditUserInfoActivity;
import com.homework.ksing.ksing.ui.EvaluateActivity;
import com.homework.ksing.ksing.ui.main.CircleImageView;
import com.homework.ksing.ksing.ui.main.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingActivity extends Activity {
    ImageView fanhui;
    LinearLayout userinfo;
    Button logout;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private MyURL myURL=new MyURL();

    private CircleImageView userDp;
    private TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getInChildThread(myURL.getURL()+"/getUserInfo");
        init();
        setListener();
    }

    public void init() {
        fanhui = findViewById(R.id.fanhui);
        userinfo = findViewById(R.id.userinfo);
        userDp=findViewById(R.id.imageView24) ;
        userName=findViewById(R.id.textView42);
        logout = findViewById(R.id.logout);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public void setListener() {
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
                Intent intent=new Intent(SettingActivity.this, EditUserInfoActivity.class);
                startActivity(intent);
            }
        });
        /*将本地的sp中的数据删除，将服务器上的数据删除*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyURL myURL = new MyURL();
                final OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder()
                        .addHeader("cookie", sp.getString("sessionID", ""))
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
                editor.putString("sessionID", "");
                editor.putBoolean("isLogin", false);
                editor.commit();
                /*关闭后台服务*/
                Intent service=new Intent(SettingActivity.this,GetMsgService.class);
                stopService(service);
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void getInChildThread(String url) {

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        RequestBody body = new FormBody.Builder()
                .build();
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
                String result = response.body().string();
                JSONObject data = null;
                try {
                    data = new JSONObject(result);
                    System.out.println(data);
                    userDp.setImageURL(myURL.getURL()+data.getString("user_dp"));
                    userName.setText(data.getString("user_name")+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
