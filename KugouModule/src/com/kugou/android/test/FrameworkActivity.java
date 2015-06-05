
package com.kugou.android.test;

import com.kugou.common.base.AbsFrameworkFragment;
import com.kugou.common.base.ViewPagerFrameworkDelegate;
import com.kugou.common.base.ViewPagerFrameworkDelegate.DelegateListener;
import com.kugou.ktv.android.common.activity.KtvBaseActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class FrameworkActivity extends KtvBaseActivity implements DelegateListener {

    private ViewPagerFrameworkDelegate mDelegate = new ViewPagerFrameworkDelegate(this, this);

    @Override
    public ViewPagerFrameworkDelegate getDelegate() {
        return mDelegate;
    }

    @Override
    public AbsFrameworkFragment onCreatePlayerFragment() {
        return new PlayerFragment();
    }

    @Override
    public void onKeyBackSlideCallback() {

    }

    @Override
    public void onNewBundle(Bundle args) {

    }

    @Override
    public void onSlideToLeftCallback(int currentPos, int previousPos) {

    }

    @Override
    public void onSlideToRightCallback(int currentPos, int previousPos) {

    }

    public void showPlayerFragment(boolean anim) {
        mDelegate.showPlayerFragment(anim);
    }

    public void hidePlayerFragmentToLeft(boolean anim) {
        mDelegate.hidePlayerFragmentToLeft(anim);
    }

    public void hidePlayerFragmentToRight(boolean anim) {
        mDelegate.hidePlayerFragmentToRight(anim);
    }

    public boolean isPlayerFragmentShowing() {
        return mDelegate.isPlayerFragmentShowing();
    }

    public boolean isPlayerFragmentScrolling() {
        return mDelegate.isPlayerFragmentScrolling();
    }

    public void showMenu(boolean anim) {
        mDelegate.showMenu(anim);
    }

    public void hideMenu(boolean anim) {
        mDelegate.hideMenu(anim);
    }

    public boolean isMenuOpen() {
        return mDelegate.isMenuOpen();
    }

    public boolean isMenuScrolling() {
        return mDelegate.isMenuScrolling();
    }

    public void showMainFragment() {
        mDelegate.showMainFragment();
    }

    public AbsFrameworkFragment getCurrentFragment() {
        return mDelegate.getCurrentFragment();
    }

    public void startFragment(AbsFrameworkFragment target, Class<? extends Fragment> cls,
            Bundle args, boolean anim, boolean replaceMode, boolean clearTop) {
        mDelegate.startFragment(target, cls, args, anim, replaceMode, clearTop);
    }
    
    public static class PlayerFragment extends AbsFrameworkFragment {
        
    }
}
