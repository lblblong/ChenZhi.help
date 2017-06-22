package com.beilong.chenzzs.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by LBL on 2016/11/7.
 */

public class SplashActivity extends AppCompatActivity{

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            SplashActivity.this.startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            SplashActivity.this.finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.sendEmptyMessageDelayed(1,1000);
    }
}
