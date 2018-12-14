package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.homework.ksing.ksing.R;

public class NotificationDemoActivity extends Activity {
    Button ceshi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_demo);
        init();
    }
    public void init(){

        ceshi=findViewById(R.id.ceshi);

        ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*获取消息管理类*/
                NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel("1",
                        "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true); //是否在桌面icon右上角展示小红点
                channel.setLightColor(R.color.red); //小红点颜色
                channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                manager.createNotificationChannel(channel);

                Notification.Builder builder=new Notification.Builder(NotificationDemoActivity.this)
                        .setSmallIcon(R.drawable.ul)
                        .setContentTitle("测试标题")/*设置标题*/
                        .setChannelId("1")/*设置渠道*/
                        .setContentText("这是一个测试通知样例");
                manager.notify(1,builder.build());
                Log.w("消息推送测试","运行到代码了");
            }
        });
    }
}
