package com.homework.ksing.ksing.ui.weibo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.ui.WeiboActivity;

/**
 * Created by Administrator on 2018/6/2.
 */

public class WBFirstFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wb_first, null);

        Button bt01 = view.findViewById(R.id.bt01);
        bt01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof WeiboActivity) {
                    ((WeiboActivity) getActivity()).getNavigitionBar().setMsgPointCount(2, 109);
                    ((WeiboActivity) getActivity()).getNavigitionBar().setMsgPointCount(0, 5);
                    ((WeiboActivity) getActivity()).getNavigitionBar().setHintPoint(3, true);
                }
            }
        });


        Button bt02 = view.findViewById(R.id.bt02);
        bt02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof WeiboActivity) {
                    ((WeiboActivity) getActivity()).getNavigitionBar().clearAllHintPoint();
                    ((WeiboActivity) getActivity()).getNavigitionBar().clearAllMsgPoint();
                }

            }
        });

        Button bt03 = view.findViewById(R.id.bt03);
        bt03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof WeiboActivity) {
                    ((WeiboActivity) getActivity()).getNavigitionBar().clearMsgPoint(0);
                }
            }
        });


        Button bt04 = view.findViewById(R.id.bt04);
        bt04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof WeiboActivity) {
                    ((WeiboActivity) getActivity()).getNavigitionBar().clearHintPoint(3);

                }
            }
        });

        Button bt05 = view.findViewById(R.id.bt05);
        bt05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("111111111111");
                if (getActivity() instanceof WeiboActivity) {
                    ((WeiboActivity) getActivity()).getNavigitionBar().selectTab(1);
                    ((WBSecondFragment) (((WeiboActivity) getActivity()).getNavigitionBar().getAdapter().getItem(1))).showToast("嘻嘻哈哈嗝");

                }
            }
        });

        return view;
    }


}
