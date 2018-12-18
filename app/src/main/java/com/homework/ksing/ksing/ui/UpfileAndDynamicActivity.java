package com.homework.ksing.ksing.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.controller.MyURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpfileAndDynamicActivity extends AppCompatActivity {
    private Button bofang;
    private Button stop;
    private String filePath;
    private MediaPlayer mediaPlayer;
    private ImageView back;
    private File audioFile;
    private SharedPreferences sp;
    private TextView upload;
    private MyURL myURL=new MyURL();
    private String songCode;
    private EditText dtEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = UpfileAndDynamicActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_upfile_and_dynamic);
        init();
        setListener();
    }
    public void init(){
        bofang=findViewById(R.id.bofang);
        back=findViewById(R.id.uback);
        stop=findViewById(R.id.stop);
        upload=findViewById(R.id.upload);
        dtEditText=findViewById(R.id.dtEditText);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        filePath=intent.getExtras().getString("filePath");
        audioFile=new File(filePath);
        songCode=intent.getExtras().getString("song_code");

    }
    public void setListener() {
        bofang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(filePath);
                        mediaPlayer.prepare();

                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer
                            .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    setTitle("录音播放完毕.");
                                }
                            });

                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInChildThread(myURL.getURL()+"/uploadMySong",audioFile);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getInChildThread(String url,File file) {

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart( //给Builder添加上传的文件
                "upload",  //请求的名字
                file.getName(), //文件的文字，服务器端用来解析的
                RequestBody.create(MediaType.parse(".wav"), file));

        builder.addFormDataPart("nr",dtEditText.getText().toString());

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对

        //创建一个请求对象，传入URL地址和相关数据的键值对的对象

        Request.Builder builder1 = new Request.Builder().addHeader("cookie",sp.getString("sessionID",""));
        builder1.url(url)
                .post(builder.build());
        //创建一个能处理请求数据的操作类
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(builder1.build());

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
