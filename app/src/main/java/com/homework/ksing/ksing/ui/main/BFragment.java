package com.homework.ksing.ksing.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.circlerefresh.CircleRefreshLayout;
import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/2.
 */

public class BFragment extends android.support.v4.app.Fragment {
    private CircleRefreshLayout mRefreshLayout;
    private LoopViewPager viewPager;
    private List<String> images = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, null);


        mRefreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout2);
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

        viewPager = (LoopViewPager) view.findViewById(R.id.viewpager);
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504330680909&di=a615ac65f3e2084626984f110bd918ef&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f6d057a988cf0000018c1b29283d.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504332690541&di=c47a1a1737653e07e288e42b977c63c0&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F016f705553f4090000009c5091c0b7.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504332936143&di=33523d984b211a81558577f0f7795377&imgtype=0&src=http%3A%2F%2Feasyread.ph.126.net%2FRjNwlT5__FJHVtVaassowQ%3D%3D%2F6597180817330744819.jpg");
        viewPager.setData(images);
        viewPager.setOnPageClickListener(new LoopViewPager.OnPageClickListener() {
            @Override
            public void onClick(int pos) {
                Toast.makeText(getActivity(),"click:"+pos,Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }


}
