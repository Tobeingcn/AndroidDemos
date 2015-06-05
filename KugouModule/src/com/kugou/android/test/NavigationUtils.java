package com.kugou.android.test;

import com.kugou.common.app.KGCommonApplication;
import com.kugou.common.base.AbsFrameworkFragment;
import com.kugou.common.dynamic.DexFileManager;
import com.kugou.common.dynamic.PluginDexFileManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class NavigationUtils {

    public static void startFMMainFragment(AbsFrameworkFragment fragment) {
        try {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).loadDexForceAndBack(
                    DexFileManager.MODULEFM);
            Class<?> class1 = Class.forName("com.kugou.fm.main.FMMainFragment");
            fragment.startFragment((Class<? extends Fragment>) class1, null);
        } catch (ClassNotFoundException e) {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).showLoadFailToast();
            e.printStackTrace();
        }

    }

    public static void startRingtoneClassfiFragment(AbsFrameworkFragment fragment, Bundle bundle) {
        try {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).loadDexForceAndBack(
                    DexFileManager.MODULERINGTONE);
            Class<Fragment> class1 = (Class<Fragment>) Class
                    .forName("com.kugou.ringtone.fragment.RingtoneListFragment");
            fragment.startFragment(class1, bundle);
        } catch (ClassNotFoundException e) {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).showLoadFailToast();
            e.printStackTrace();
        }
    }

    public static void startPackRingtoneFragment(AbsFrameworkFragment fragment, Bundle bundle) {
        try {
            Class<Fragment> class1 = (Class<Fragment>) Class
                    .forName("com.kugou.ringtone.fragment.PackRingtoneListFragment");
            fragment.startFragment(class1, bundle);
        } catch (ClassNotFoundException e) {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).showLoadFailToast();
            e.printStackTrace();
        }

    }
    public static void startRingtoneMainFragment(AbsFrameworkFragment fragment) {
        try {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).loadDexForceAndBack(
                    DexFileManager.MODULERINGTONE);

            Class class1 = Class.forName("com.kugou.ringtone.fragment.KGRingtoneMainFragment");
            fragment.startFragment((Class<? extends Fragment>) class1, null);
        } catch (ClassNotFoundException e) {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).showLoadFailToast();
            e.printStackTrace();
        }
    }

    public static void startGameMainFragment(Context context, boolean fromShortcut) {
        try {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).loadDexForceAndBack(
                    DexFileManager.ANDROIDGAME);
            Class class1 = Class.forName("com.kugou.game.activity.GameCenterActivity");
            Intent intent = new Intent(context, class1);
            intent.putExtra("fromShortcut", fromShortcut);
            intent.putExtra("canSwipe", false);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            PluginDexFileManager.getInstance(KGCommonApplication.getContext()).showLoadFailToast();
            e.printStackTrace();
        }

    }
}
