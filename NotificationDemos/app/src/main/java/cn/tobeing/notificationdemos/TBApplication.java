package cn.tobeing.notificationdemos;

import android.app.Application;
import android.content.Context;

/**
 * Created by sunzheng on 15/11/11.
 */
public class TBApplication extends Application{
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }
    public static Context getContext(){
        return mContext;
    }
}
