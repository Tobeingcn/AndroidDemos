package com.tobeing.test.ui;

import com.tobeing.test.GlobalEnv;
import com.tobeing.test.R;
import com.tobeing.test.util.CPUUTil;
import com.tobeing.test.util.DateUtil;
import com.tobeing.test.util.FileUtil;
import com.tobeing.test.util.MemoryUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author zhengsun
 * @date 2015年5月28日下午3:06:54
 */
public class MainActivity extends Activity {
    private static final int MSG_UPDATE_INFO=12;
    private Button button;
    private static final String CPU_INFO=GlobalEnv.LOG_FOLDER+"CPU.log";
    private StringBuilder stringBuilder;
    private BackHandler handler;
    private HandlerThread backThread;
    private Handler uiHandler;
    private TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stringBuilder=new StringBuilder();
        tvInfo=(TextView) findViewById(R.id.tvInfo);
        if(!new File(GlobalEnv.LOG_FOLDER).exists()){
            new File(GlobalEnv.LOG_FOLDER).mkdirs();
        }
        if(new File(CPU_INFO).exists()){
            new File(CPU_INFO).delete();
        }
        try {
            new File(CPU_INFO).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        findViewById(R.id.btnAllAPP).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllAppActivity.class));
            }
        });
        
        backThread=new HandlerThread("backThread");
        backThread.start();
        handler=new BackHandler(backThread.getLooper());
        handler.sendEmptyMessageDelayed(BackHandler.MSG_GET_MEM_INFO, 500);
        uiHandler=new Handler(){
          @Override
        public void handleMessage(Message msg) {
              switch (msg.what) {
                case MSG_UPDATE_INFO:
                    tvInfo.setText(msg.obj.toString());
                    break;

                default:
                    break;
            }
        }  
        };
        findViewById(R.id.btnSend).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(new File(CPU_INFO).exists()){
                    Intent share = new Intent(Intent.ACTION_SEND);   
                    share.putExtra(Intent.EXTRA_STREAM, 
                    Uri.fromFile(new File(CPU_INFO)));
                    share.setType("*/*");
                    startActivity(Intent.createChooser(share, "Share"));
                }
            }
        });
        findViewById(R.id.btnCopy).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ClipboardManager c= (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                c.setText(stringBuilder.toString());
                Toast.makeText(MainActivity.this, "文本已经复制到前切板，请手动发送给客服", Toast.LENGTH_LONG).show();
            }
        });
    }
    boolean isResume=false;
    @Override
    protected void onStop() {
        super.onStop();
        isResume=false;
    }
    @Override
    protected void onResume() {
        isResume=true;
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        backThread.getLooper().quit();
    }
    private class BackHandler extends Handler{
        private static final int MSG_GET_MEM_INFO=1;
        private BackHandler(Looper Looper){
            super(Looper);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_MEM_INFO:
                    StringBuilder info=new StringBuilder();
                    info.append(DateUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
                    info.append(getTopApp());
                    info.append("\n");
                    info.append(String.format("maxF=%s;curF=%s;minF=%s", CPUUTil.getMaxCpuFreq(),CPUUTil.getCurCpuFreq(),CPUUTil.getMinCpuFreq()));
                    info.append("\n");
                    info.append(String.format("totalMem=%s,usedMen=%s",MemoryUtil.getmem_TOLAL(),MemoryUtil.getmem_UNUSED(MainActivity.this)));
                    info.append("\n");
                    stringBuilder.append(info);
                    if(isResume){
                    uiHandler.obtainMessage(MSG_UPDATE_INFO, info).sendToTarget();}
                    FileUtil.appendData(CPU_INFO, info.toString().getBytes());
                    
                    sendEmptyMessageDelayed(MSG_GET_MEM_INFO, 200);
                    break;

                default:
                    break;
            }
        }
    }
    
    public String getTopApp(){
        try{
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String packageName = cn.getPackageName();
        return packageName;
        }catch(Exception exception){
            return "no package";
        }
    }
}