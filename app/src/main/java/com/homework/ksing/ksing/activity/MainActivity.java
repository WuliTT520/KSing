package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.homework.ksing.ksing.R;


import com.example.circlerefresh.CircleRefreshLayout;

public class MainActivity extends Activity {

    private CircleRefreshLayout mRefreshLayout;
    private ListView mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRefreshLayout = (CircleRefreshLayout) findViewById(R.id.refresh_layout);
        mList = (ListView) findViewById(R.id.list);


        String[] strs = {
            "The",
            "Canvas",
            "class",
            "holds",
            "the",
            "draw",
            "calls",
            ".",
            "To",
            "draw",
            "something,",
            "you",
            "need",
            "4 basic",
            "components",
            "Bitmap",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_layout,R.id.showList, strs);
        mList.setAdapter(adapter);





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
                            Thread.sleep(3000);//休眠3秒
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







    }


}
