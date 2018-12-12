package com.homework.ksing.ksing.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.circlerefresh.CircleRefreshLayout;
import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.activity.KgLoginActivity;
import com.homework.ksing.ksing.ui.MainActivity;
import com.homework.ksing.ksing.view.ScrollDisabledListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Jue on 2018/6/2.
 */

public class DFragment extends android.support.v4.app.Fragment {
    private CircleRefreshLayout mRefreshLayout;
    private LinearLayout myBottom;
    private ScrollDisabledListView mList;
    private List contacts=new ArrayList(0);
    private SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d, container,false);
        getInChildThread("http://192.168.88.1:8080/getuserinfo");


        View myBottomView = View.inflate(getActivity(), R.layout.my_bottom_layout, null);
        mList = (ScrollDisabledListView) myBottomView.findViewById(R.id.dynamicListview);
//        mList.setEnabled(false);
        mList.setFocusable(false);
        String[] name = {"张舒杨","张舒杨","张舒杨","张舒杨","张舒杨"};
        int[] picture = {R.drawable.touxiang1,R.drawable.touxiang1,R.drawable.touxiang1,R.drawable.touxiang1,R.drawable.touxiang1};
        String[] time = {"15:39","15:39","15:39","15:39","15:39"};
        String[] text = {"我成为我的好友圈盟主，不服来战！","我成为我的好友圈盟主，不服来战！","我成为我的好友圈盟主，不服来战！","我成为我的好友圈盟主，不服来战！","我成为我的好友圈盟主，不服来战！"};
        int[] picture1 = {R.drawable.songimg,R.drawable.songimg,R.drawable.songimg,R.drawable.songimg,R.drawable.songimg};
        String[] songname = {"舍得","舍得","舍得","舍得","舍得"};
        String[] num = {"3","3","3","3","3"};
        String[]elnum={"5","评论","4","评论","评论"};
        for (int i=0;i<name.length;i++) {

            HashMap map=new HashMap<String,Object>();
            map.put("name",name[i]);
            map.put("time",time[i]);
            map.put("picture",picture[i]);
            map.put("text",text[i]);
            map.put("num",num [i]);
            map.put("picture1",picture1[i]);
            map.put("songname",songname[i]);
            map.put("elnum",elnum[i]);
            contacts.add(map);

        }
        SimpleAdapter adapter=new SimpleAdapter(getActivity(),contacts,R.layout.dynamic_list,new String[]{"name","time","picture","text","num","picture1","songname","elnum"}
                ,new int[]{R.id.name,R.id.time,R.id.picture,R.id.text,R.id.num,R.id.picture2,R.id.songname,R.id.elnum});

        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),"111111111111111！",Toast.LENGTH_SHORT).show();
            }
        });



        myBottom=view.findViewById(R.id.myBottom);

        myBottom.addView(myBottomView);

        mRefreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout4);
        mRefreshLayout.setOnRefreshListener(

                new CircleRefreshLayout.OnCircleRefreshListener() {


                    @Override
                    public void refreshing() {
                        // do something when refresh starts
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(2000);//休眠3秒
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mRefreshLayout.finishRefreshing();

                                /**
                                 * 要执行的操作
                                 */
                            }
                        }.start();
                    }

                    @Override
                    public void completeRefresh() {
                        // do something when refresh complete


                    }
                });

        return view;
    }

    private void getInChildThread(String url) {

        sp=this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        RequestBody body = new FormBody.Builder()
                .add("code","").build();
        //创建一个请求对象，传入URL地址和相关数据的键值对的对象
        final Request request = new Request.Builder().addHeader("cookie",sp.getString("sessionID",""))
                .url(url)
                .post(body).build();

        //创建一个能处理请求数据的操作类
        Call call = client.newCall(request);

        //使用异步任务的模式请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG","错误信息：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();

                JSONObject data = null;
                try {
                    data = new JSONObject(result);
                    System.out.println(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });





    }

    //提示消息
    public void showToast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
