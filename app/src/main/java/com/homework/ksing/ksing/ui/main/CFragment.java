package com.homework.ksing.ksing.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.circlerefresh.CircleRefreshLayout;
import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.activity.MusicPlayerActivity;

/**
 * Created by Jue on 2018/6/2.
 */

public class CFragment extends android.support.v4.app.Fragment {
    private CircleRefreshLayout mRefreshLayout;
    LinearLayout musicplayer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c, container,false);
        musicplayer=view.findViewById(R.id.musicplayer);
        musicplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MusicPlayerActivity.class);
                startActivity(intent);
            }
        });

        mRefreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout3);
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

}
