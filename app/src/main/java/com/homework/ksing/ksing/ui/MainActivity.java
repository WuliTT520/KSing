package com.homework.ksing.ksing.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.ksing.ksing.Service.GetMsgService;
import com.homework.ksing.ksing.ui.main.AFragment;
import com.homework.ksing.ksing.ui.main.DFragment;
import com.next.easynavigition.constant.Anim;
import com.next.easynavigition.utils.NavigitionUtil;
import com.next.easynavigition.view.EasyNavigitionBar;
import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.ui.main.CFragment;
import com.homework.ksing.ksing.ui.main.BFragment;
import com.homework.ksing.ksing.view.KickBackAnimator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {



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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = MainActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                   );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }


        setContentView(R.layout.activity_main);


        Intent serviceIntent =new Intent(MainActivity.this,GetMsgService.class);
        startService(serviceIntent);

        navigitionBar = findViewById(R.id.navigitionBar);

        fragments.add(new AFragment());
        fragments.add(new BFragment());
        fragments.add(new CFragment());
        fragments.add(new DFragment());

        navigitionBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .navigitionBackground(Color.parseColor("#FFFFFF"))
                .iconSize(30)
                .selectTextColor(Color.parseColor("#F14F44"))
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener(new EasyNavigitionBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        if (position == 4) {


                        } else if (position == 2) {
                            //跳转页面（全民K歌）   或者   弹出菜单（微博）
                            Intent intent=new Intent(MainActivity.this,MusicListActivity.class);
                            startActivity(intent);
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

        ViewGroup view = (ViewGroup) View.inflate(MainActivity.this, R.layout.layout_add_view, null);
        menuLayout = view.findViewById(R.id.icon_group);
        cancelImageView = view.findViewById(R.id.cancel_iv);
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAnimation();
            }
        });
        for (int i = 0; i < 4; i++) {
            View itemView = View.inflate(MainActivity.this, R.layout.item_icon, null);
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
                        int x = NavigitionUtil.getScreenWidth(MainActivity.this) / 2;
                        int y = (int) (NavigitionUtil.getScreenHeith(MainActivity.this) - NavigitionUtil.dip2px(MainActivity.this, 50));
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
