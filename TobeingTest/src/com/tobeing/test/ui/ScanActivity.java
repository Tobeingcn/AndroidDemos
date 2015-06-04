
package com.tobeing.test.ui;

import com.tobeing.test.R;
import com.tobeing.test.util.TBLog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ScanActivity extends Activity {
    
    private static final String TAG="ScardTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Log.d(TAG, "file,dataDir="+getApplicationInfo().dataDir);
        findViewById(R.id.btnLog).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                scan();
                
            }
        });
    }

    private void scan(){
 new Thread(new Runnable() {
            
            @Override
            public void run() {
                String path="/storage/emulated/0/Music/歌曲识别测试/多个歌手";
                path="/storage/extSdCard/extMusic/歌单";
                String lastStream="";
                int i=0;
                while (i++<1000) {
                    try {
                        Thread.sleep(100);
                        File[] files=new File(path).listFiles();
                        String newlist=Arrays.toString(files);
                        TBLog.d("newlist", "newlist="+newlist);
                        if(lastStream!=null&&!lastStream.equals(newlist)){
                            new IllegalArgumentException("error different ");
                        }else{
                            lastStream=newlist;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //printAllSdcard(Environment.getExternalStorageDirectory(), 0);
                //printAllSdcard(new File("/storage/emulated/legacy"), 0); 
                //printAllSdcard(new File("/storage/emulated/0"), 0); 
                //printAllSdcard(new File("/storage/extSdCard"), 0); 
                //printAllSdcard(new File("/mnt/shell/emulated"), 0);
                //printAllSdcard(new File("/storage/emulated"), 0); 
                //printAllSdcard(new File("/sdcard/"), 0); 
               // printAllSdcard(new File("/storage/emulated/0/tencent/tassistant/log/"), 0);
            }
        }).start();
    }
    public void printAllSdcard(File file,int deep){
        if(deep>2){
            return;
        }
        if(file.isDirectory()){
            Log.d(TAG, "file,isDir="+file.getAbsolutePath());
            Log.d(TAG, "file,Canon="+file.getAbsolutePath());
            File[] files=file.listFiles();
            for (File subFile:files) {
                printAllSdcard(subFile, deep+1);
            }
    
        }else{
            Log.d(TAG, "file,Absol="+file.getAbsolutePath());
            try {
                Log.d(TAG, "file,Canon="+file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
