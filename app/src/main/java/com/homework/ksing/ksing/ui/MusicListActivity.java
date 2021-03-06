package com.homework.ksing.ksing.ui;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
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
import com.homework.ksing.ksing.activity.MusicPlayerActivity;
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

public class MusicListActivity extends AppCompatActivity {
    String[] song_code=new String[100];
    String[] code=new String[100];
    String[] song_name=new String[100];
    String[] singer=new String[100];
    String[] song_picture=new String[100];
    String[] song_path=new String[100];
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
        setContentView(R.layout.activity_music_list);
        init();
        getSongs();
    }
    /*初始化*/
    public void init(){

        musicList=findViewById(R.id.musicList1);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 100:
                        musicBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                        break;
                    case 400:
                        Toast.makeText(MusicListActivity.this,"网络异常,请检测网络",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
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
                    musicAdapter=new MusicAdapter(MusicListActivity.this,song_code,code,song_name,singer,song_picture,song_path,myData);
                    musicList.setAdapter(musicAdapter);

                    musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(MusicListActivity.this,KgeActivity.class);
                            intent.putExtra("song_path", song_path[i]);
                            intent.putExtra("song_name", song_name[i]);
                            intent.putExtra("song_code", song_code[i]);
                            startActivity(intent);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
