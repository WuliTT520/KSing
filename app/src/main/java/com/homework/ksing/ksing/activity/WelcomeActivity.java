package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.Service.GetMsgService;
import com.homework.ksing.ksing.ui.MainActivity;

public class WelcomeActivity extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp.getBoolean("isLogin",false)){
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }else{
                    Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        },1000*2);
    }
}
