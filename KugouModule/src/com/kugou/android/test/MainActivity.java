
package com.kugou.android.test;

import com.kugou.common.base.AbsFrameworkFragment;
import com.kugou.common.base.MainFragmentKan;
import com.kugou.ktv.android.main.activity.KtvMainFragment;
import com.kugou.ktv.android.navigation.NavigationFragment;

import android.os.Bundle;

public class MainActivity extends FrameworkActivity {

    @Override
    public AbsFrameworkFragment onCreateFMPlayerFragment() {
        return new TestMainFragment();
    }

    @Override
    public AbsFrameworkFragment onCreateMenuFragmentTing() {
        return new TestMainFragment();
    }
    
    @Override
    public AbsFrameworkFragment onCreateMenuFragmentKan() {
        return new TestMainFragment();
    }
    
    @Override
    public AbsFrameworkFragment onCreateMenuFragmentChang() {
        return new NavigationFragment();
    }
    
    @Override
    public AbsFrameworkFragment onCreateMenuFragmentWan() {
        return new TestMainFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onDestroy() {
        LifecycleInitiationUtils.releaseKtv();
        super.onDestroy();
    }

    @Override
    public void onPlayerSlideCallback(boolean arg0, boolean arg1) {
        
    }

    @Override
    public void onFinishFragment(int currentIndex) {
        
    }

    @Override
    public AbsFrameworkFragment onCreateMainFragmentTing() {
        return new TestMainFragment();
    }

    @Override
    public AbsFrameworkFragment onCreateMainFragmentKan() {
        // 看模块
        AbsFrameworkFragment kan = null;
        String kanClsName = "com.kugou.fanxing.main.MainFragment";
        if (isClassExist(kanClsName)) {
            try {
                kan = (AbsFrameworkFragment) Class.forName(kanClsName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            kan = new MainFragmentKan();
        }
        return kan;
    }

    @Override
    public AbsFrameworkFragment onCreateMainFragmentChang() {
        return new KtvMainFragment();
    }
    
    private boolean isClassExist(String className) {
        boolean exist = false;
        try {
            Class.forName(className);
            exist = true;
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

}
