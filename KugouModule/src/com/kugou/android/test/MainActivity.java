
package com.kugou.android.test;

import com.kugou.common.base.AbsFrameworkFragment;
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
        return new TestMainFragment();
    }

    @Override
    public AbsFrameworkFragment onCreateMainFragmentChang() {
        return new KtvMainFragment();
    }
}
