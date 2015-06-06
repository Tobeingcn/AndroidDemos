package com.kugou.mvdemo;


import android.os.Environment;
import android.util.Log;

import java.util.Date;

/**
 * log工具
 * @author zhengsun
 * @date 2015年5月28日下午2:55:51
 */
public class TBLog {

    private static final String TAG = "TBLog";

    private static final String T_TAG = "T_TBLog";
    
    private static boolean isDebug = true;
    
    private static boolean isLogToFile=false;
    

    private static String mLogFilePath = GlobalEnv.LOG_FOLDER + GlobalEnv.LOG_FILENAME;
    
    /**
     * 当前毫秒
     * 
     * @param desc 描述
     */
    public static void currentTime(int index) {
        if (isDebug) {
            StringBuilder builder = new StringBuilder();
            builder.append(index).append(":").append(String.valueOf(System.currentTimeMillis()));
            Log.i(T_TAG, builder.toString());
        }
    }

    /**
     * 当前毫秒
     * 
     * @param desc 描述
     */
    public static void currentTime(String desc) {
        if (isDebug) {
            StringBuilder builder = new StringBuilder();
            builder.append(desc).append(":").append(String.valueOf(System.currentTimeMillis()));
            Log.i(T_TAG, builder.toString());
        }
    }

    /**
     * 默认TAG=kugou
     * 
     * @param msg
     */
    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 自定义TAG
     * 
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
        if(isLogToFile){
            writeLog(tag+":"+msg);
        }
    }
    /**
     * 默认TAG=kugou
     * 
     * @param msg
     */
    public static void v(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }

    /**
     * 自定义TAG
     * 
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
        if(isLogToFile){
            writeLog(tag+":"+msg);
        }
    }
    public static void writeLog(String msg) {
        if (isDebug) {
            StringBuffer sb = new StringBuffer();
            sb.append("datetime:");
            sb.append(DateUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
            sb.append("\r\n");
            sb.append(msg);
            appendLog(mLogFilePath, sb.toString());
        }
    }

    public static void appendLog(String filePath, String msg) {
        if (openOrCreateLogFile(filePath)) {
            FileUtil.appendData(filePath, (msg + "\r\n\r\n").getBytes());
        }
    }

    // 打开或创建日志文件
    private static boolean openOrCreateLogFile(String filePath) {
        if (isSDCardAvailable()) {
            FileUtil.createFile(filePath, FileUtil.MODE_UNCOVER);
            return true;
        }
        return false;
    }
    /**
     * 检测储存卡是否可用
     * 
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    
    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    /**
     * 自定义TAG
     * 
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
        if(isLogToFile){
            writeLog(tag+":"+msg);
        }
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    /**
     * 自定义TAG
     * 
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg == null ? "" : msg);
        }
        if(isLogToFile){
            writeLog(tag+":"+msg);
        }
    }
}
