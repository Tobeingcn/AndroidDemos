package com.tobeing.test.ui;

import com.tobeing.test.GlobalEnv;
import com.tobeing.test.R;
import com.tobeing.test.util.AppUtil;
import com.tobeing.test.util.CommonUtil;
import com.tobeing.test.util.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 获取所有应用的页面
 * @author zhengsun
 * @date 2015年5月28日下午3:37:36
 */
public class AllAppActivity extends Activity {
    private TextView tvAllApp;
    private Handler handler;
    private String APP_PATH=GlobalEnv.LOG_FOLDER+"appInfo.log";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_app);
        tvAllApp=(TextView) findViewById(R.id.tvAllAPP);
        tvAllApp.setText("正在查询所有应用，请等待");
        findViewById(R.id.btnSend).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(new File(APP_PATH).exists()){
                    Intent share = new Intent(Intent.ACTION_SEND);   
                    share.putExtra(Intent.EXTRA_STREAM, 
                    Uri.fromFile(new File(APP_PATH)));
                    share.setType("*/*");
                    startActivity(Intent.createChooser(share, "Share"));
                }
            }
        });
        findViewById(R.id.btnCoptText).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ClipboardManager c= (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                c.setText(tvAllApp.getText());
                Toast.makeText(AllAppActivity.this, "文本已经复制到前切板，请手动发送给客服", Toast.LENGTH_LONG).show();
            }
        });
       handler=new Handler(){
           @Override
        public void handleMessage(Message msg) {
               switch (msg.what) {
                case 0:
                    tvAllApp.setText(msg.obj.toString());
                    break;

                default:
                    break;
            }
           }
        };
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                String allInfo=CommonUtil.ListToString(AppUtil.getAllAppInfo(AllAppActivity.this));
              handler.obtainMessage(0, allInfo).sendToTarget();
              if(new File(APP_PATH).exists()){
                  new File(APP_PATH).delete();
              }
              try {
                  new File(APP_PATH).createNewFile();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              FileUtil.appendData(APP_PATH, allInfo.getBytes());
            }
        }).start();
    }
}
