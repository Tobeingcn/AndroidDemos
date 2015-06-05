package com.kugou.android.test;

import com.kugou.common.app.BaseAppWrapper;
import com.kugou.common.app.KGCommonApplication;
import com.kugou.common.app.lifecycle.IApplicationLifecycle;
import com.kugou.common.dynamic.DexFileManager;
import com.kugou.common.dynamic.DexLoadResult;
import com.kugou.common.filemanager.service.util.FileServiceUtil;
import com.kugou.common.player.playerphone.util.PlayerConfig;
import com.kugou.common.service.util.CommonServiceUtil;
import com.kugou.common.utils.KGLog;

import android.content.Context;

/**
 * create by sunzheng
 */
public class TestApplication extends KGCommonApplication implements IApplicationLifecycle {
    private static Context mContext;

    private static Thread mMainThread;

    public static boolean isNewInstall = false;

    public static boolean isLoadDexFile = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        PlayerConfig.loadLibs();
        
        if (isForeProcess()) {
            DexLoadResult loadResult = DexFileManager.getInstance(mContext).load(false);
            if (loadResult.result) {
                CommonServiceUtil.bindToService(getApplicationContext());
            }
        } else {
            DexFileManager.getInstance(mContext).load(true);
        }
    }

    // 绑定播放服务和后台服务
    public static void bindtService() {
        KGLog.i("exit", "application bindToService");
        FileServiceUtil.setExited(false);
        FileServiceUtil.bindToService(mContext);
    }

    /**
     * 上下文
     * 
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 主线程
     * 
     * @return
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    public static BaseAppWrapper getAppWrapper() {
        return null;
    }

    public void onLowMemory() {
        System.gc();
    }

    public static String getAppPackageName() {
        return mContext.getPackageName();
    }

    public static void exitApp(Context context) {
    }


    public static void exitBackProcess() {
        KGLog.i("exit", "exitBackProcess");
    }


    @Override
    public void onAppCreate(Context context) {
    }
}
