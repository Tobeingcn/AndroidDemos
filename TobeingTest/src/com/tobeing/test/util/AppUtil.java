package com.tobeing.test.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * appUtil 工具
 * @author zhengsun
 * @date 2015年5月28日下午3:43:13
 */
public class AppUtil {
    
    /**
     * 获取所有的安装应用
     * @param context
     */
    public static List<AppInfo> getAllAppInfo(Context context) {
        List<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据

        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager())
                    .toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);// 如果非系统应用，则添加至appList
            }
        }
        return appList;
    }
    
    
    public static class AppInfo {
        public String appName="";
        public String packageName="";
        public String versionName="";
        public int versionCode=0;
        public Drawable appIcon=null;
        @Override
        public String toString() {
            return "[AppInfo:appName"+appName+";packageName"+packageName+";versionName="+"versionName"+";versionCode"+versionCode+"]";
        }
    }
}