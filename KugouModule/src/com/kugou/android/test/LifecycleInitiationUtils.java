package com.kugou.android.test;

import com.kugou.common.module.ktv.LifecycleInitiation;

public class LifecycleInitiationUtils {
    
    public static void releaseKtv(){
        LifecycleInitiation.onAppRelease();
    }
}
