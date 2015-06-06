package com.kugou.mvdemo;

import android.os.Environment;

import java.io.File;

public class GlobalEnv {
    /**
     * sdcard
     */
    public static final String ROOT_DIR = Environment
            .getExternalStorageDirectory().toString();
    
    public static final String APP_NAME="Kugou";
    /**
     * 日志目录
     */
    public static final String LOG_FOLDER = ROOT_DIR + File.separator+APP_NAME+File.separator+"log"+File.separator;
    
    public static final String LOG_FILENAME=APP_NAME+".log";
}
