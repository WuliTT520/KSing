package com.homework.ksing.ksing.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.circlerefresh.CircleRefreshLayout;
import com.homework.ksing.ksing.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jue on 2018/6/2.
 */

public class AFragment extends android.support.v4.app.Fragment {
    private CircleRefreshLayout mRefreshLayout;
    private ListView mList;
    private List contacts=new ArrayList(0);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container,false);
        mRefreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout1);

        mList = (ListView) view.findViewById(R.id.list1);

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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
