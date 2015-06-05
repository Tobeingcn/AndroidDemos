package com.kugou.android.test;

import com.kugou.common.dynamic.DexFileManager;
import com.kugou.common.dynamic.DexLoadResult;
import com.kugou.common.service.util.CommonServiceUtil;
import com.kugou.common.skin.SkinFactory;
import com.kugou.ktv.android.common.activity.KtvBaseActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.Gravity;
import android.widget.TextView;

public class SplashActivity extends KtvBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView text = new TextView(this);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(50);
        text.setTextColor(Color.BLUE);
        text.setBackgroundColor(Color.WHITE);
        text.setText("测试项目");
        setContentView(text);
        
        HandlerThread mHandlerThread = new HandlerThread("splash thread");
        mHandlerThread.start();
        BackgroundHandler  mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
        mBackgroundHandler.sendEmptyMessage(MSG_INIT_APP_STATE);
        
        initTheme();
    }
    
    private void initTheme() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SkinFactory factory = new SkinFactory();
                factory.parseStyleXml(getApplicationContext());
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }).start();
    }
    
    private final int MSG_INIT_APP_STATE = 0;
    private class BackgroundHandler extends Handler {

        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_INIT_APP_STATE:
                    DexLoadResult loadResult = DexFileManager.getInstance(mContext).load(false);
                    if (loadResult.result) {
                        CommonServiceUtil.bindToService(getApplicationContext());
                    }
                  
                    break;
            }
        };
    };
}
