package com.homework.ksing.ksing.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.controller.MyURL;
import com.homework.ksing.ksing.ui.main.MyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MusicAdapter extends BaseAdapter {
    private Context context;
    private String[] song_code;
    private String[] code;
    private String[] song_name;
    private String[] singer;
    private String[] song_picture;
    private List<String> contacts;
    MyURL myURL=new MyURL();
    public MusicAdapter(Context context, String[] song_code, String[] code, String[] song_name, String[] singer, String[] song_picture, List<String> contacts){
        this.context=context;
        this.song_code=song_code;
        this.code=code;
        this.song_name=song_name;
        this.singer=singer;
        this.song_picture=song_picture;
        this.contacts=contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            viewHolder=new ViewHolder();
            view=LayoutInflater.from(context).inflate(R.layout.song_item,null);
            viewHolder.song_pic=view.findViewById(R.id.song_pic);
            viewHolder.song=view.findViewById(R.id.song);
            viewHolder.singer=view.findViewById(R.id.singer);
            viewHolder.more=view.findViewById(R.id.more);
            view.setTag(viewHolder);
        }else{
            viewHolder = (MusicAdapter.ViewHolder) view.getTag();
        }
        viewHolder.song_pic.setImageURL(myURL.getURL()+song_picture[i]);
        viewHolder.song.setText(song_name[i]);
        viewHolder.singer.setText(singer[i]);

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("MusicAdapter","more被点击了");
            }
        });
        return view;
    }

    class ViewHolder{
        MyImageView song_pic;
        TextView song;
        TextView singer;
        ImageView more;
    }
}
