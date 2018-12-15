package com.homework.ksing.ksing.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.activity.KgLoginActivity;
import com.homework.ksing.ksing.controller.MyURL;
import com.homework.ksing.ksing.ui.main.CircleImageView;

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

public class EditUserInfoActivity extends AppCompatActivity {
    private LinearLayout userDpLayout;
    private LinearLayout nameLayout;
    private LinearLayout codeLayout;
    private LinearLayout sexLayout;
    private LinearLayout livingPlceLayout;
    private LinearLayout signatureLayout;
    private LinearLayout schoolLayout;
    private LinearLayout occupationLayout;
    private LinearLayout hometownLayout;
    private LinearLayout heightLayout;

    private CircleImageView userDpImage;
    private ImageView isShowIn1Image;
    private ImageView isShowIn2Image;

    private TextView nameTextView;
    private TextView codeTextView;
    private TextView sexTextView;
    private TextView livingPlceTextView;
    private TextView signatureView;
    private TextView schoolView;
    private TextView occupationView;
    private TextView hometownView;
    private TextView heightView;

    private SharedPreferences sp;
    private MyURL myURL=new MyURL();

    private boolean isShowIn1;
    private boolean isShowIn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        init();
        getInChildThread(myURL.getURL()+"/getuserinfo");
        setListener();




    }

    public void init() {
        userDpLayout=findViewById(R.id.user_dpl);
        nameLayout =findViewById(R.id.namel);
        codeLayout =findViewById(R.id.codel);
        sexLayout =findViewById(R.id.sexl);
        livingPlceLayout=findViewById(R.id.living_placel);
        signatureLayout=findViewById(R.id.signaturel);
        schoolLayout=findViewById(R.id.schooll);
        occupationLayout  =findViewById(R.id.occupationl);
        hometownLayout=findViewById(R.id .hometownl);
        heightLayout=findViewById(R.id.heightl);

        userDpImage=findViewById(R.id.user_dpi);
        isShowIn1Image=findViewById(R.id.is_show_in1);
        isShowIn2Image =findViewById(R.id.is_show_in2);

        nameTextView=findViewById(R.id.nameT);
        codeTextView =findViewById(R.id.codeT);
        sexTextView =findViewById(R.id.sexT);
        livingPlceTextView=findViewById(R.id.living_placeT);
        signatureView=findViewById(R.id.signatureT);
        schoolView=findViewById(R.id.schoolT);
        occupationView =findViewById(R.id.occupationT);
        hometownView=findViewById(R.id.hometownT);
        heightView=findViewById(R.id.textViewheightT);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }


    public void setListener() {
        //头像点击
        userDpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditUserInfoActivity.this,"hi！",Toast.LENGTH_SHORT).show();
            }
        });
        //昵称点击
        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置昵称");
                intent.putExtra("nr", nameTextView.getText());
                intent.putExtra("url", myURL.getURL()+"/setname");
                intent.putExtra("key", "name");
                startActivity(intent);
            }
        });
        //K歌号点击
        codeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditUserInfoActivity.this,"这是我的K歌号！",Toast.LENGTH_SHORT).show();
            }
        });
       // 性别点击
        sexLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置性别");
                intent.putExtra("nr", sexTextView.getText());
                intent.putExtra("url", myURL.getURL()+"/setsex");
                intent.putExtra("key", "sex");
                startActivity(intent);
            }
        });
        //现居地点击
        livingPlceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置现居地");
                intent.putExtra("nr", livingPlceTextView.getText());
                intent.putExtra("url", myURL.getURL()+"/setliving_place");
                intent.putExtra("key", "living_place");
                startActivity(intent);
            }
        });
        //个人签名点击
        signatureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置个人签名");
                intent.putExtra("nr", signatureView.getText());
                intent.putExtra("url", myURL.getURL()+"/setsignature");
                intent.putExtra("key", "signature");
                startActivity(intent);
            }
        });
        //学校点击
        schoolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置学校");
                intent.putExtra("nr", schoolView.getText());
                intent.putExtra("url", myURL.getURL()+"/setschool");
                intent.putExtra("key", "school");
                startActivity(intent);
            }
        });
        //职业点击
        occupationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置职业");
                intent.putExtra("nr", occupationView.getText());
                intent.putExtra("url", myURL.getURL()+"/setoccupation");
                intent.putExtra("key", "occupation");
                startActivity(intent);
            }
        });
        //家乡点击
        hometownLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置家乡");
                intent.putExtra("nr", hometownView.getText());
                intent.putExtra("url", myURL.getURL()+"/sethometown");
                intent.putExtra("key", "hometown");
                startActivity(intent);
            }
        });
        //身高点击
        heightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditUserInfoActivity.this,UpdateActivity.class);
                intent.putExtra("title", "设置身高");
                intent.putExtra("nr", heightView.getText());
                intent.putExtra("url", myURL.getURL()+"/setheight");
                intent.putExtra("key", "height");
                startActivity(intent);
            }
        });
        //显示活跃点击
        isShowIn1Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowIn1){
                    isShowIn1Image.setImageResource(R.drawable.a7f);
                    updataUserInfo(myURL.getURL()+"/setis_show_in1","is_show_in1","0");
                    isShowIn1=false;
                }else{
                    isShowIn1Image.setImageResource(R.drawable.a_v);
                    updataUserInfo(myURL.getURL()+"/setis_show_in1","is_show_in1","1");
                    isShowIn1=true;
                }
            }
        });
        //显示附近点击
        isShowIn2Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowIn2){
                    isShowIn2Image.setImageResource(R.drawable.a7f);
                    updataUserInfo(myURL.getURL()+"/setis_show_in2","is_show_in2","0");
                    isShowIn2=false;
                }else{
                    isShowIn2Image.setImageResource(R.drawable.a_v);
                    updataUserInfo(myURL.getURL()+"/setis_show_in2","is_show_in2","1");
                    isShowIn2=true;
                }
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
                    codeTextView.setText(data.getString("code"));
                    userDpImage.setImageURL(myURL.getURL()+data.getString("user_dp"));
                    nameTextView.setText(data.getString("name"));
                    sexTextView.setText(data.getString("sex"));
                    livingPlceTextView.setText(data.getString("living_place"));
                    signatureView.setText(data.getString("signature"));
                    schoolView.setText(data.getString("school"));
                    occupationView.setText(data.getString("occupation"));
                    hometownView.setText(data.getString("hometown"));
                    heightView.setText(data.getString("height"));
                    if(data.getString("is_show_in1").equals("1")){
                        isShowIn1Image.setImageResource(R.drawable.a_v);
                        isShowIn1=true;
                    }else{
                        isShowIn1Image.setImageResource(R.drawable.a7f);
                        isShowIn1=false;
                    }
                    if(data.getString("is_show_in2").equals("1")){
                        isShowIn2Image.setImageResource(R.drawable.a_v);
                        isShowIn2=true;
                    }else{
                        isShowIn2Image.setImageResource(R.drawable.a7f);
                        isShowIn2=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
