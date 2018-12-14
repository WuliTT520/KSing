package com.homework.ksing.ksing.ui.main;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homework.ksing.ksing.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private String[] name=new String[100];
    private int[] picture=new int[100];
    private String[] time=new String[100];
    private String[] text=new String[100];
    private int[] picture1=new int[100];
    private String[] songname=new String[100];
    private String[] num=new String[100];
    private String[]elnum=new String[100];
    private List <String>contacts = new ArrayList<>();

    public MyAdapter(Context context,String []name,int[] picture,String[] time,String[] text,int[] picture1,String[] songname,String[] num,String[]elnum,List <String>contacts) {
        mContext = context;
        this.name=name;
        this.picture=picture;
        this.time=time;
        this.text=text;
        this.picture1=picture1;
        this.songname=songname;
        this.num=num;
        this.elnum=elnum;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.dynamic_list, null);
            viewHolder.name  =  view.findViewById(R.id.name);
            viewHolder.time = view.findViewById(R.id.time);
            viewHolder.text = view.findViewById(R.id.text);
            viewHolder.num = view.findViewById(R.id.num);
            viewHolder.songname = view.findViewById(R.id.songname);
            viewHolder.elnum =  view.findViewById(R.id.elnum);
            viewHolder.picture=view.findViewById(R.id.picture);
            viewHolder.picture2=view.findViewById(R.id.picture2);
            viewHolder.elnumLayout=view.findViewById(R.id.elnumLayout);
            viewHolder.kg_btn=view.findViewById(R.id.kg_btn);
            viewHolder.send_gift=view.findViewById(R.id.send_gift);
            viewHolder.share=view.findViewById(R.id.share);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(name[i]);
        viewHolder.time.setText(time[i]);
        viewHolder.text.setText(text[i]);
        viewHolder.num.setText(num[i]);
        viewHolder.songname.setText(songname[i]);
        viewHolder.elnum.setText(elnum[i]);
        viewHolder.picture.setImageResource(picture[i]);
        viewHolder.picture2.setImageResource(picture1[i]);


//评论点击事件
        viewHolder.elnumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemElnumClickListener.onElnumClick(i);
            }
        });
//打擂点击事件
        viewHolder.kg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemKgClickListener.onKgClick(i);
            }
        });
//送礼点击事件
        viewHolder.send_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemGiftClickListener.onGiftClick(i);
            }
        });
//分享点击事件
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemShareClickListener.onShareClick(i);
            }
        });
        return view;
    }

    /**
     * 删除按钮的监听接口
     */

    public interface onItemElnumClickListener {
        void onElnumClick(int i);
    }
    public interface onItemKgClickListener {
        void onKgClick(int i);
    }
    public interface onItemGiftClickListener {
        void onGiftClick(int i);
    }
    public interface onItemShareClickListener {
        void onShareClick(int i);
    }

    private onItemElnumClickListener mOnItemElnumClickListener;
    private onItemKgClickListener mOnItemKgClickListener;
    private onItemGiftClickListener mOnItemGiftClickListener;
    private onItemShareClickListener mOnItemShareClickListener;


    public void setOnItemElnumClickListener(onItemElnumClickListener mOnItemElnumClickListener) {
        this.mOnItemElnumClickListener = mOnItemElnumClickListener;
    }
    public void setOnItemKgClickListener(onItemKgClickListener mOnItemKgClickListener) {
        this.mOnItemKgClickListener = mOnItemKgClickListener;
    }
    public void setOnItemGiftClickListener(onItemGiftClickListener mOnItemGiftClickListener) {
        this.mOnItemGiftClickListener = mOnItemGiftClickListener;
    }
    public void setOnItemShareClickListener(onItemShareClickListener mOnItemShareClickListener) {
        this.mOnItemShareClickListener = mOnItemShareClickListener;
    }

    class ViewHolder {
        TextView name;
        TextView time;
        TextView text;
        TextView num;
        TextView songname;
        TextView elnum;
        ImageView picture;
        ImageView picture2;
        LinearLayout elnumLayout;
        TextView kg_btn;
        LinearLayout send_gift;
        LinearLayout share;
    }

}
