package com.homework.ksing.ksing.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.homework.ksing.ksing.R;

public class LoginActivity extends Activity {
    private TextView kg_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        kg_login=findViewById(R.id.kg_login);


        kg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,KgLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
