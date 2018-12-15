package com.homework.ksing.ksing.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.activity.NotificationDemoActivity;
import com.homework.ksing.ksing.activity.WelcomeActivity;
import com.homework.ksing.ksing.controller.MyURL;
import com.homework.ksing.ksing.ui.MainActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetMsgService extends Service {
    public static final String TAG = "GetMsgService";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Timer timer;
    @Override
    public void onCreate() {
        super.onCreate();
//        Log.w(TAG,"创建服务123");

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final OkHttpClient client = new OkHttpClient();
                MyURL myURL=new MyURL();
                sp=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                Log.w(TAG,sp.getString("sessionID",""));
                final Request request=new Request.Builder()
                        .addHeader("cookie",sp.getString("sessionID",""))
                        .url(myURL.getMsg())
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.w(TAG,"请求错误");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String result = response.body().string();
                            Log.w("success",result);
//                            System.out.println("***********");
//                            System.out.println("result="+result);
//                            System.out.println("***********");


                            if(!(result.equals(""))){
                                NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                NotificationChannel channel = new NotificationChannel("1",
                                        "动态消息", NotificationManager.IMPORTANCE_DEFAULT);
                                channel.enableLights(true); //是否在桌面icon右上角展示小红点
                                channel.setLightColor(R.color.red); //小红点颜色
                                channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                                manager.createNotificationChannel(channel);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,  intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                Notification.Builder builder=new Notification.Builder(GetMsgService.this)
                                        .setSmallIcon(R.drawable.ul)
                                        .setContentTitle("全民K歌")/*设置标题*/
                                        .setChannelId("1")/*设置渠道*/
                                        .setContentIntent(contentIntent)/*设置跳转页面*/
                                        .setAutoCancel(true)/*点击消失*/
                                        .setContentText(result+"评论了你，快去看看看吧！");
                                manager.notify(1,builder.build());
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        },1000,1000*60);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
