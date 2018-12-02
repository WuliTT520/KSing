package com.homework.ksing.ksing.ui.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.bumptech.glide.Glide;
import com.homework.ksing.ksing.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * 简书ID：天哥在奔跑
 * 原创Android教程：http://www.jianshu.com/p/9618c038135f
 * 教程答疑专用QQ群：667833258
 */
public class LoopViewPager extends LinearLayout {

    private static final int TIME_GAP = 3000;
    private FixedFirstLayoutViewPager mViewPager;
    private NavIndicator mIndicator;
    private HomePagerAdapter mAdapter = new HomePagerAdapter();
    private Runnable mRunnable = new HomepageSwitcher();
    private boolean isAutoSwitch = true;
    private List<String> images = new ArrayList<>();
    private OnPageClickListener listener;

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setData(List<String> images){
        this.images = images;
        mIndicator.setPageCount(this.images.size());
        mAdapter.notifyDataSetChanged();
    }

    public void setOnPageClickListener(OnPageClickListener listener){
        this.listener = listener;
    }

    private void initView() {
        View container = LayoutInflater.from(getContext()).inflate(R.layout.layout_viewpager, null);
        mViewPager = (FixedFirstLayoutViewPager) container.findViewById(R.id.view_pager);
        mIndicator = (NavIndicator) container.findViewById(R.id.view_indicator);
        mViewPager.setAdapter(mAdapter);
        changeViewPagerScrollerSpeed();
        addView(container);
        addOnPagerChangeListener();
        removeCallBacksWhenTouch();
        if (isAutoSwitch) {
            mViewPager.postDelayed(mRunnable, TIME_GAP);
        }
    }

    private void removeCallBacksWhenTouch() {
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (isAutoSwitch) {
                            mViewPager.removeCallbacks(mRunnable);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    default:
                        if (isAutoSwitch) {
                            mViewPager.postDelayed(mRunnable, TIME_GAP);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void changeViewPagerScrollerSpeed() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext());
            scroller.setFixedDuration(400);
            field.set(mViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOnPagerChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (images.size() > 1) {
                    mViewPager.removeCallbacks(mRunnable);
                    mIndicator.onPageSelected(position % images.size());
                    mViewPager.postDelayed(mRunnable, TIME_GAP);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public boolean isAutoSwitch() {
        return isAutoSwitch;
    }

    public void setIsAutoSwitch(boolean isAutoSwitch) {
        if (this.isAutoSwitch == isAutoSwitch) return;
        this.isAutoSwitch = isAutoSwitch;
        if (isAutoSwitch) {
            mViewPager.postDelayed(mRunnable, TIME_GAP);
        } else {
            mViewPager.removeCallbacks(mRunnable);
        }
    }

    public void setCurrentItem(int index) {
        mViewPager.removeCallbacks(mRunnable);
        mViewPager.postDelayed(mRunnable, TIME_GAP);
        mViewPager.setCurrentItem(images.size() * 50 + index, true);
    }

    protected class HomePagerAdapter extends PagerAdapter {

        private int cacheCount = 0;
        private static final int CACHE_SIZE = 3;

        public void resetCache() {
            this.cacheCount = 0;
        }

        @Override
        public int getCount() {
            return images.size() > 1 ? 2000 : images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView iv = new ImageView(getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String url = images.get(position % images.size());
            Glide.with(getContext()).load(url).into(iv);
            container.addView(iv);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onClick(position % images.size());
                    }
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (cacheCount < CACHE_SIZE) {
                cacheCount = cacheCount + 1;
                return POSITION_NONE;
            } else {
                return super.getItemPosition(object);
            }
        }
    }

    private class HomepageSwitcher extends TimerTask {

        public void run() {
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public int getFixedDuration() {
            return mDuration;
        }

        public void setFixedDuration(int time) {
            mDuration = time;
        }
    }

    public interface OnPageClickListener{
        void onClick(int pos);
    }
}
