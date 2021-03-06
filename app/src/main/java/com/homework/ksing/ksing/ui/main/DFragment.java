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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.circlerefresh.CircleRefreshLayout;
import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.activity.KgLoginActivity;
import com.homework.ksing.ksing.activity.SettingActivity;
import com.homework.ksing.ksing.controller.MyURL;
import com.homework.ksing.ksing.ui.EvaluateActivity;
import com.homework.ksing.ksing.ui.KgeActivity;
import com.homework.ksing.ksing.ui.MainActivity;
import com.homework.ksing.ksing.view.ScrollDisabledListView;

import org.json.JSONArray;
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
    private MyAdapter adapter;
    private ListView mList;
    private List contacts=new ArrayList(0);
    private SharedPreferences sp;
    private TextView uName;
    private TextView fNum;
    private TextView foNum;
    private CircleImageView userDp;
    private ImageView setting;

    private String[] name=new String[100];
    private String[] picture=new String[100];
    private String[] time=new String[100];
    private String[] text=new String[100];
    private String[] picture1=new String[100];
    private String[] songname=new String[100];
    private String[] num=new String[100];
    private String[]elnum=new String[100];
    private MyURL myURL=new MyURL();
    private ImageView toKg;
    private String[]state_code=new String[100];
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d, container,false);
        getInChildThread(myURL.getURL()+"/getUserInfo");
        toKg=view.findViewById(R.id.imageView21);
        toKg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), KgeActivity.class);
                startActivity(intent);
            }
        });
        userDp=view.findViewById(R.id.user_dp);
         uName=view.findViewById(R.id.u_name);
        fNum=view.findViewById(R.id.friendNum);
        foNum=view.findViewById(R.id.focusedNum);
        setting=view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        View myBottomView = View.inflate(getActivity(), R.layout.my_bottom_layout, null);
        mList = (ListView) myBottomView.findViewById(R.id.dynamicListview);
//        mList.setEnabled(false);
        mList.setFocusable(false);
        /*
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

*/

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
                                getInChildThread1(myURL.getURL()+"/getMyDynamic");
                                try {
                                    Thread.sleep(1000);//休眠3秒
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
                    System.out.println(data.toString());

                     System.out.println(data.getString("user_name"));
                    userDp.setImageURL(myURL.getURL()+data.getString("user_dp"));
                    uName.setText(data.getString("user_name")+"");
                    fNum.setText(data.getString("friend_num")+"");
                    foNum.setText(data.getString("focused_num")+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getInChildThread1(myURL.getURL()+"/getMyDynamic");

            }
        });





    }

    private void getInChildThread1(String url) {

        sp=this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        RequestBody body = new FormBody.Builder()
                .build();
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
                JSONArray data = null;
                try {
                    data = new JSONArray(result);
                    System.out.println(data);
                    for(int i=0;i<data.length();i++)
                    {
                        JSONObject jsonObject=data.getJSONObject(i);
                        name[i]=jsonObject.getString("name");
                        System.out.println(name[i]);
                        time[i] ="15:39";
                        text[i] =jsonObject.getString("user_text");
                        picture[i]=jsonObject.getString("user_dp");
                        picture1[i]=jsonObject.getString("song_picture");
                        songname[i] = jsonObject.getString("song_name");
                        num[i]="3";
                        elnum[i]=jsonObject.getString("evaluate_num");
                        state_code[i]=jsonObject.getString("state_code");

                    }
                    List<String>contacts1=new ArrayList();
                    for (int i=0;i<data.length();i++) {

                        HashMap map=new HashMap<String,Object>();
                        map.put("name",name[i]);
                        map.put("time",time[i]);
                        map.put("picture",picture[i]);
                        map.put("text",text[i]);
                        map.put("num",num [i]);
                        map.put("picture1",picture1[i]);
                        map.put("songname",songname[i]);
                        map.put("elnum",elnum[i]);
                        contacts1.add(i+"  author:Leslie___Cheung");

                        contacts.add(map);

                    }
                    /*
                    SimpleAdapter adapter=new SimpleAdapter(getActivity(),contacts,R.layout.dynamic_list,new String[]{"name","time","picture","text","num","picture1","songname","elnum"}
                            ,new int[]{R.id.name,R.id.time,R.id.picture,R.id.text,R.id.num,R.id.picture2,R.id.songname,R.id.elnum});

                    mList.setAdapter(adapter);

                    mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(getActivity(),"111111111111111！",Toast.LENGTH_SHORT).show();
                        }
                    });

                    */
                    adapter = new MyAdapter(getActivity(), name,picture,time,text,picture1,songname,num,elnum,contacts1);
                    mList.setAdapter(adapter);
                    adapter.setOnItemElnumClickListener(new MyAdapter.onItemElnumClickListener() {
                        @Override
                        public void onElnumClick(int i) {
                            System.out.println("评论    "+i);
                            Intent intent=new Intent(getActivity(), EvaluateActivity.class);
                            intent.putExtra("state_code", state_code[i]);
                            intent.putExtra("song_dp",picture1[i]);
                            intent.putExtra("songName",songname[i]);
                            startActivity(intent);
                        }
                    });
                    adapter.setOnItemKgClickListener(new MyAdapter.onItemKgClickListener() {
                        @Override
                        public void onKgClick(int i) {
                            System.out.println("K歌    "+i);
                        }
                    });
                    adapter.setOnItemGiftClickListener(new MyAdapter.onItemGiftClickListener() {
                        @Override
                        public void onGiftClick(int i) {
                            System.out.println("送礼    "+i);
                        }
                    });
                    adapter.setOnItemShareClickListener(new MyAdapter.onItemShareClickListener() {
                        @Override
                        public void onShareClick(int i) {
                            System.out.println("分享    "+i);
                            Intent intent=new Intent(Intent.ACTION_SEND);

                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                            intent.putExtra(Intent.EXTRA_TEXT, "我在全民K歌看到了一条有趣的动态，快来围观！！");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Intent.createChooser(intent, getActivity().getTitle()));
                        }
                    });

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
