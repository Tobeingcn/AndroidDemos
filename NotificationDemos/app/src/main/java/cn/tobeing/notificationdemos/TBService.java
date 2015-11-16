package cn.tobeing.notificationdemos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by sunzheng on 15/11/11.
 */
public class TBService extends Service{
    public static final int PLAYBACKSERVICE_STATUS=0x001;
    private static final String TAG="TBService";
    public static final String STATUSBAR_FLAG="STATUSBAR_FLAG";
    public static final String NEXT_ACTION_FROM_NOTI="NEXT_ACTION_FROM_NOTI";
    public static final String EXIT_KUGOU_BY_STATUSBAR ="EXIT_KUGOU_BY_STATUSBAR" ;
    public static final String UNLOCK_MINI_LYRIC_FORM_NOTI = "UNLOCK_MINI_LYRIC_FORM_NOTI";
    public static final String TOGGLEPAUSE_FROM_NOTI = "TOGGLEPAUSE_FROM_NOTI";
    public static final String OPEN_MINI_LYRIC_FORM_NOTI ="OPEN_MINI_LYRIC_FORM_NOTI" ;
    public static final String CLOSE_MINI_LYRIC_FORM_NOTI = "CLOSE_MINI_LYRIC_FORM_NOTI";
    public static final String PREVIOUS_ACTION_FROM_NOTI = "PREVIOUS_ACTION_FROM_NOTI";
    public static final String FM_BY_STATUSBAR ="FM_BY_STATUSBAR" ;
    public static KGNotificationOperation KGNotificationOperation;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        KGNotificationOperation=new KGNotificationOperation(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class TOGGLEPAUSE_FROM_NOTI {
    }
}
