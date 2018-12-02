package com.homework.ksing.ksing.ui.main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * 简书ID：天哥在奔跑
 * 原创Android教程：http://www.jianshu.com/p/9618c038135f
 * 教程答疑专用QQ群：667833258
 */
public class FixedFirstLayoutViewPager extends ViewPager {

    public FixedFirstLayoutViewPager(Context context) {
        super(context);
    }

    public FixedFirstLayoutViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
        fixFirstLayout();
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
        fixFirstLayout();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        fixFirstLayout();
    }

    private void fixFirstLayout() {
        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
