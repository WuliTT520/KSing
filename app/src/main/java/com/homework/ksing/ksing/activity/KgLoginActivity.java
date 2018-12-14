package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.controller.MyURL;
import com.homework.ksing.ksing.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KgLoginActivity extends AppCompatActivity {
    private EditText usercode;
    private EditText password;
    private Button login;
    private String result;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = KgLoginActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_kg_login);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        usercode=findViewById(R.id.usercode);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyURL url=new MyURL();
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(5, TimeUnit.SECONDS)
                        .build();

                //post请求来获得数据
                //创建一个RequestBody，存放重要数据的键值对
                RequestBody body = new FormBody.Builder()
                        .add("code", usercode.getText().toString())
                        .add("password", password.getText().toString()).build();
                //创建一个请求对象，传入URL地址和相关数据的键值对的对象
                final Request request = new Request.Builder()
                        .url(url.login())
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
                        result=response.body().string();
                        System.out.println(response.header("cookie"));
                        JSONObject data = null;
                        try {
                            data = new JSONObject(result);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(data!=null){
                            String sessionId=response.header("Set-Cookie");
                            System.out.println("*****************************");
                            Log.w("session",sessionId);
                            System.out.println("*****************************");

                            editor = sp.edit();
                            editor.putString("sessionID",sessionId);
                            editor.putBoolean("isLogin",true);
                            try {
                                editor.putString("code", (String) data.get("code"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            editor.commit();
                            Intent intent=new Intent(KgLoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Looper.prepare();
                            Toast.makeText(KgLoginActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                            finish();

                            Looper.loop();
                        }else{
                            Looper.prepare();
                            Toast.makeText(KgLoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            System.out.print(5644);
                        }
                    }
                });

            }
        });
    }
}
