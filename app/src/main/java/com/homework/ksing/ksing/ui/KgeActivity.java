package com.homework.ksing.ksing.ui;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaCasStateException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.activity.KgLoginActivity;
import com.homework.ksing.ksing.controller.MyURL;

public class KgeActivity extends Activity implements OnClickListener {
    private MediaPlayer mediaPlayer;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mediaRecorder = new MediaRecorder();
    private File audioFile;
    private String filePath;
    private String songName;
    private String songCode;
    private MyURL myURL=new MyURL();
    private TextView songNameT;

    @Override
    public void onClick(View view) {
        try {
            String msg = "";
            switch (view.getId()) {
                case R.id.btnStart:
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource("http://www.ytmp3.cn/down/53810.mp3");
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();


                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder
                            .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mediaRecorder
                            .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    audioFile = File.createTempFile("record_", ".wav");
                    Log.d("Main", audioFile.getAbsolutePath());
                    System.out.println(audioFile.getAbsolutePath());

                    mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
                    mediaRecorder.prepare();
                    mediaRecorder.start();



                    msg = "正在录音...";
                    break;
                case R.id.btnStop:
                    if (audioFile != null) {
                        mediaRecorder.stop();
                        mMediaPlayer.stop();
                    }
                    msg = "已经停止录音.";
                    break;
                case R.id.btnPlay:
                    if (audioFile   != null) {
                        mediaRecorder.stop();
                        mMediaPlayer.stop();
                    }
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(myURL.getURL()+filePath);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();


                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder
                            .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mediaRecorder
                            .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    audioFile = File.createTempFile("record_", ".wav");
                    Log.d("Main", audioFile.getAbsolutePath());
                    System.out.println(audioFile.getAbsolutePath());

                    mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    break;
                 case R.id.kgFinish:
                     Intent intent=new Intent(KgeActivity.this,UpfileAndDynamicActivity.class);
                     intent.putExtra("filePath", audioFile.getAbsolutePath());
                     intent.putExtra("song_code", songCode);
                     startActivity(intent);
                    break;
                case R.id.kgback:
                    finish();
                    break;
            }
            setTitle(msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            setTitle(e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = KgeActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_kge);
        ImageView btnStart = (ImageView) findViewById(R.id.btnStart);
        LinearLayout btnStop = (LinearLayout) findViewById(R.id.btnStop);
        LinearLayout btnPlay = (LinearLayout) findViewById(R.id.btnPlay);
        LinearLayout kgFinish=findViewById(R.id.kgFinish);
        ImageView kgback=findViewById(R.id.kgback);
        Intent intent = getIntent();
        filePath=intent.getExtras().getString("song_path");
        songCode=intent.getExtras().getString("song_code");
        songName=intent.getExtras().getString("song_name");
        songNameT=findViewById(R.id.songName);
        songNameT.setText(songName);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        kgFinish.setOnClickListener(this);
        kgback.setOnClickListener(this);


    }
}