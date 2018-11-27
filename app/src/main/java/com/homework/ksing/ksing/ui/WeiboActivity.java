package com.homework.ksing.ksing.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.next.easynavigition.constant.Anim;
import com.next.easynavigition.utils.NavigitionUtil;
import com.next.easynavigition.view.EasyNavigitionBar;
import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.ui.weibo.AddThirdFragment;
import com.homework.ksing.ksing.ui.weibo.WBFirstFragment;
import com.homework.ksing.ksing.ui.weibo.WBSecondFragment;
import com.homework.ksing.ksing.view.KickBackAnimator;

import java.util.ArrayList;
import java.util.List;

public class WeiboActivity extends FragmentActivity {

    private EasyNavigitionBar navigitionBar;

    private String[] tabText = {"动态", "发现", "点歌", "消息", "我的"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.c2p, R.mipmap.bri, R.mipmap.buh, R.mipmap.bvm, R.mipmap.bwi};
    //选中时icon
    private int[] selectIcon = {R.mipmap.c2q, R.mipmap.brj, R.mipmap.buh, R.mipmap.bvn, R.mipmap.bwj};

    private List<android.support.v4.app.Fragment> fragments = new ArrayList<>();


    //仿微博图片和文字集合
    private int[] menuIconItems = {R.mipmap.pic1, R.mipmap.pic2, R.mipmap.pic3, R.mipmap.pic4};
    private String[] menuTextItems = {"文字", "拍摄", "相册", "直播"};

    private LinearLayout menuLayout;
    private View cancelImageView;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo);

        navigitionBar = findViewById(R.id.navigitionBar);

        fragments.add(new WBFirstFragment());
        fragments.add(new WBSecondFragment());
        fragments.add(new AddThirdFragment());
        fragments.add(new AddThirdFragment());

        navigitionBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener(new EasyNavigitionBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        if (position == 4) {
                            Toast.makeText(WeiboActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                            //return true则拦截事件、不进行页面切换
                            return true;
                        } else if (position == 2) {
                            //跳转页面（全民K歌）   或者   弹出菜单（微博）
                            showMunu();
                        }
                        return false;
                    }
                })
                .mode(EasyNavigitionBar.MODE_ADD)
                .anim(Anim.ZoomIn)
                .build();


        navigitionBar.setAddViewLayout(createWeiboView());

    }

    //仿微博弹出菜单
    private View createWeiboView() {
        ViewGroup view = (ViewGroup) View.inflate(WeiboActivity.this, R.layout.layout_add_view, null);
        menuLayout = view.findViewById(R.id.icon_group);
        cancelImageView = view.findViewById(R.id.cancel_iv);
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAnimation();
            }
        });
        for (int i = 0; i < 4; i++) {
            View itemView = View.inflate(WeiboActivity.this, R.layout.item_icon, null);
            ImageView menuImage = itemView.findViewById(R.id.menu_icon_iv);
            TextView menuText = itemView.findViewById(R.id.menu_text_tv);

            menuImage.setImageResource(menuIconItems[i]);
            menuText.setText(menuTextItems[i]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            itemView.setLayoutParams(params);
            itemView.setVisibility(View.GONE);
            menuLayout.addView(itemView);
        }
        return view;
    }

    //
    private void showMunu() {
        startAnimation();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //＋ 旋转动画
                cancelImageView.animate().rotation(90).setDuration(400);
            }
        });
        //菜单项弹出动画
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            final View child = menuLayout.getChildAt(i);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(500);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(500);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50 + 100);
        }
    }


    private void startAnimation() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //圆形扩展的动画
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        int x = NavigitionUtil.getScreenWidth(WeiboActivity.this) / 2;
                        int y = (int) (NavigitionUtil.getScreenHeith(WeiboActivity.this) - NavigitionUtil.dip2px(WeiboActivity.this, 25));
                        Animator animator = ViewAnimationUtils.createCircularReveal(navigitionBar.getAddViewLayout(), x,
                                y, 0, navigitionBar.getAddViewLayout().getHeight());
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                navigitionBar.getAddViewLayout().setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //							layout.setVisibility(View.VISIBLE);
                            }
                        });
                        animator.setDuration(300);
                        animator.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 关闭window动画
     */
    private void closeAnimation() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelImageView.animate().rotation(0).setDuration(400);
            }
        });

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                int x = NavigitionUtil.getScreenWidth(this) / 2;
                int y = (NavigitionUtil.getScreenHeith(this) - NavigitionUtil.dip2px(this, 25));
                Animator animator = ViewAnimationUtils.createCircularReveal(navigitionBar.getAddViewLayout(), x,
                        y, navigitionBar.getAddViewLayout().getHeight(), 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //							layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navigitionBar.getAddViewLayout().setVisibility(View.GONE);
                        //dismiss();
                    }
                });
                animator.setDuration(300);
                animator.start();
            }
        } catch (Exception e) {
        }
    }


    public EasyNavigitionBar getNavigitionBar() {
        return navigitionBar;
    }

}
