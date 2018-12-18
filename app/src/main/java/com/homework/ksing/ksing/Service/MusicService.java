package com.homework.ksing.ksing.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.homework.ksing.ksing.controller.MyURL;

public class MusicService extends Service {
    private IBinder binder=new MyBinder();
    public static MediaPlayer mediaPlayer;
    private MyURL myURL=new MyURL();
    public void setMusic(String path){
        try {
            Uri uri=Uri.parse(myURL.getURL()+path);
            mediaPlayer.setDataSource(this,uri);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("***********************onDestroy*******************");

        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        System.out.println("***********************onUnbind***********************");
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder{
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code){
                case 101://开始播放新一首歌
                    if (mediaPlayer!=null){
                        mediaPlayer.release();
                    }
                    mediaPlayer=new MediaPlayer();
                    String path=data.readString();
                    setMusic(path);
                    mediaPlayer.start();
                    break;
                case 102://暂停
                    mediaPlayer.pause();
                    break;
                case 103:
                    mediaPlayer.start();
                    break;
                case 104://将这首歌停止,后期通过写入data来换歌
                    mediaPlayer.stop();
                    try {
                        mediaPlayer.prepare();
                        mediaPlayer.seekTo(0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
