package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.support.v4.app.ServiceCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.Service.MusicService;
import com.homework.ksing.ksing.adapter.MusicAdapter;
import com.homework.ksing.ksing.controller.MyURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicPlayerActivity extends Activity {
    String[] song_code=new String[100];
    String[] code=new String[100];
    String[] song_name=new String[100];
    String[] singer=new String[100];
    String[] song_picture=new String[100];
    String[] song_path=new String[100];
    int sum=0;
    ImageView play,next,quit,before;
    ListView musicList;
    SeekBar musicBar;
    TextView flash;
    boolean isPlay=false;
    IBinder binder;
    Handler handler;
    ServiceConnection serviceConnection=null;
    Timer timer=null;
    TimerTask task=null;
    MusicAdapter musicAdapter;
    int playing=-1;
    final OkHttpClient client=new OkHttpClient();
    final Request request=new Request.Builder()
            .url(new MyURL().getAllSong())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        init();
        getSongs();
        setListener();
        service_link();
    }
    /*初始化*/
    public void init(){
        play=findViewById(R.id.play);
        play.setTag(0);
        musicBar=findViewById(R.id.musicBar);
        next=findViewById(R.id.next);
        quit=findViewById(R.id.quit);
        before=findViewById(R.id.before);
        flash=findViewById(R.id.flash);
        musicList=findViewById(R.id.musicList);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 100:
                        musicBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                        break;
                    case 400:
                        Toast.makeText(MusicPlayerActivity.this,"网络异常,请检测网络",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }
    /*设置点击事件*/
    public void setListener(){
        /*关闭*/
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task!=null){
                    task.cancel();
                }
                finish();
            }
        });
        /*开始播放或暂停播放*/
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag=(int)play.getTag();
                if (tag==0){//没有放歌，开始新的一首歌
                    playing=0;
                    isPlay=true;
                    play.setTag(1);
                    play.setImageResource(R.drawable.b4c);
                    try {
                        int code=101;
                        Parcel data=Parcel.obtain();
                        data.writeString(song_path[playing]);
                        Parcel reply=Parcel.obtain();
                        binder.transact(code,data,reply,0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(tag==1){//正在放歌
                    isPlay=false;
                    play.setTag(2);
                    play.setImageResource(R.drawable.b4p);
                    try {
                        int code=102;
                        Parcel data=Parcel.obtain();
                        Parcel reply=Parcel.obtain();
                        binder.transact(code,data,reply,0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if (tag==2){//已经放过歌了，只是暂停了
                    isPlay=true;
                    play.setTag(1);
                    play.setImageResource(R.drawable.b4c);
                    try {
                        int code=103;
                        Parcel data=Parcel.obtain();
                        Parcel reply=Parcel.obtain();
                        binder.transact(code,data,reply,0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                musicBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                musicBar.setMax(MusicService.mediaPlayer.getDuration());
                if (timer==null){
                    if (task==null){
                        task=new TimerTask() {
                            @Override
                            public void run() {
                                Message msg=new Message();
                                msg.what=100;
                                handler.sendMessage(msg);
                            }
                        };
                    }
                    timer=new Timer(true);
                    timer.schedule(task,0,300);
                }
            }
        });
        /*实现下一首歌error*/
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playing==-1){
                    playing=0;
                }else if (playing>=0&&playing<sum){
                    playing++;
                }else{
                    return;
                }

                isPlay=true;
                play.setTag(1);
                play.setImageResource(R.drawable.b4c);
                try {
                    int code=101;
                    Parcel data=Parcel.obtain();
                    data.writeString(song_path[playing]);
                    Parcel reply=Parcel.obtain();
                    binder.transact(code,data,reply,0);
                }catch (Exception e){
                    e.printStackTrace();
                }
                musicBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                musicBar.setMax(MusicService.mediaPlayer.getDuration());
                if (timer==null){
                    if (task==null){
                        task=new TimerTask() {
                            @Override
                            public void run() {
                                Message msg=new Message();
                                msg.what=100;
                                handler.sendMessage(msg);
                            }
                        };
                    }
                    timer=new Timer(true);
                    timer.schedule(task,0,200);
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playing==-1){
                    playing=0;
                }else if (playing>=0&&playing<sum){
                    playing--;
                }else{
                    return;
                }
                isPlay=true;
                play.setTag(1);
                play.setImageResource(R.drawable.b4c);
                try {
                    int code=101;
                    Parcel data=Parcel.obtain();
                    data.writeString(song_path[playing]);
                    Parcel reply=Parcel.obtain();
                    binder.transact(code,data,reply,0);
                }catch (Exception e){
                    e.printStackTrace();
                }
                musicBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                musicBar.setMax(MusicService.mediaPlayer.getDuration());
                if (timer==null){
                    if (task==null){
                        task=new TimerTask() {
                            @Override
                            public void run() {
                                Message msg=new Message();
                                msg.what=100;
                                handler.sendMessage(msg);
                            }
                        };
                    }
                    timer=new Timer(true);
                    timer.schedule(task,0,200);
                }
            }
        });
        /*设置滑动条控制音乐*/
        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    MusicService.mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /*刷新*/
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSongs();
            }
        });
    }
    /*连接后台获取歌单*/
    public void getSongs(){
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg=new Message();
                msg.what=400;
                handler.sendMessage(msg);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String result=response.body().string();
                    System.out.println(result);
                    JSONArray data=new JSONArray(result);
//                    System.out.println(data);
                    for(int i=0;i<data.length();i++){
                        JSONObject jsonObject=data.getJSONObject(i);
                        String sc=jsonObject.getString("song_code");
                        String c=jsonObject.getString("code");
                        String sn=jsonObject.getString("song_name");
                        String s=jsonObject.getString("singer");
                        String sp=jsonObject.getString("song_picture");
                        song_path[i]=jsonObject.getString("song_path");
                        song_code[i]=(sc);
                        code[i]=(c);
                        song_name[i]=(sn);
                        singer[i]=(s);
                        song_picture[i]=(sp);
                    }
                    sum=data.length();
                    List myData=new ArrayList<Map<String,Object>>();
                    for (int i=0;i<data.length();i++){
                        Map item=new HashMap<String,Object>();
                        item.put("song_code",song_code[i]);
                        item.put("code",code[i]);
                        item.put("song_name",song_name[i]);
                        item.put("singer",singer[i]);
                        item.put("song_picture",song_picture[i]);
                        myData.add("1");
                    }
                    musicAdapter=new MusicAdapter(MusicPlayerActivity.this,song_code,code,song_name,singer,song_picture,song_path,myData);
                    musicList.setAdapter(musicAdapter);

                    musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            playing=i;
                            isPlay=true;
                            play.setTag(1);
                            play.setImageResource(R.drawable.b4c);
                            try {
                                int code=101;
                                Parcel data=Parcel.obtain();
                                data.writeString(song_path[playing]);
                                Parcel reply=Parcel.obtain();
                                binder.transact(code,data,reply,0);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            musicBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                            musicBar.setMax(MusicService.mediaPlayer.getDuration());
                            if (timer==null){
                                if (task==null){
                                    task=new TimerTask() {
                                        @Override
                                        public void run() {
                                            Message msg=new Message();
                                            msg.what=100;
                                            handler.sendMessage(msg);
                                        }
                                    };
                                }
                                timer=new Timer(true);
                                timer.schedule(task,0,200);
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    /*绑定服务*/
    public void service_link(){
        serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                binder=iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceConnection=null;
            }
        };
        Intent intent=new Intent(this,MusicService.class);
        startService(intent);
        bindService(intent,serviceConnection,Context.BIND_ABOVE_CLIENT);
    }
    /*结束activity时关闭音乐播放器,finish不一定会调用该方法，finish只是把activity移出栈*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        serviceConnection=null;
        try {
            MusicPlayerActivity.this.finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
