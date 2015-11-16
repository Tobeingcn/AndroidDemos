/**
 * 
 */

package cn.tobeing.notificationdemos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * 通知栏显示管理器
 * 
 * @author liaodongmingRabbit
 */
public class KGNotificationOperation{

    private static final String TAG="KGNotificationOperation";

    public static final int FROM_KUGOUMUSIC = 1;

    public static final int FROM_KUGOUFM = 2;

    public static final int NOTIMODE_PLAY = 1;

    public static final int NOTIMODE_PAUSE = 2;

    public static final int NOTIMODE_PLAYMODE = 3;

    private final int MSG_SHOW_NOTIFICTION = 1;

    private final int MSG_CLOSE_NOTIFICTION = 2;

    private final int MSG_RESET_NOTIFICTION = 3;

    private final int MSG_SHOW_FM_NOTIFICATION = 4;

    private int mNotiMode = 1;

    private Service mService = null;

    private RemoteViews smallContentView = null;

    private RemoteViews bigContentView = null;

    private Notification mNotification = null;

    private Intent mBackIntent = null;

    private String mAvatarPath = null;

    private String mDisplayName = null;

    private String mSingerName = null;

    private String mTrackName = null;

    private boolean isHasAvatar = false;

    private Integer mSystemNotificationTextColor = null;

    private float mSystemNotificationTextSize = 11;

    private final int SHOW_NOTI_MINI_DELAY_TIME = 200;

    private final int SHOW_NOTI_MAX_DELAY_TIME = 500;

    private final String COLOR_SEARCH_RECURSE_TIP = "SOME_SAMPLE_TEXT";

    private Bitmap mFmAvatar;

    // 当前状态栏显示的类型 1酷狗音乐 2酷狗ＦＭ
    private static int mNotificationType;

    private boolean recurseGroup(ViewGroup gp) {
        for (int i = 0; i < gp.getChildCount(); i++) {
            View v = gp.getChildAt(i);
            if (v instanceof TextView) {
                final TextView text = (TextView) v;
                final String szText = text.getText().toString();
                if (COLOR_SEARCH_RECURSE_TIP.equals(szText)) {
                    mSystemNotificationTextColor = text.getTextColors().getDefaultColor();
                    mSystemNotificationTextSize = text.getTextSize();
                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager systemWM = (WindowManager) gp.getContext()
                            .getSystemService(Context.WINDOW_SERVICE);
                    systemWM.getDefaultDisplay().getMetrics(metrics);
                    mSystemNotificationTextSize /= metrics.scaledDensity;
                    return true;
                }
            }
            if (v instanceof ViewGroup) {// 如果是ViewGroup 遍历搜索
                recurseGroup((ViewGroup) gp.getChildAt(i));
            }
        }
        return false;
    }

    /**
     * 枚举Notification颜色
     */
    private void extractColors() {
        if (mSystemNotificationTextColor != null) {
            return;
        }
        try {
            Notification ntf = new Notification();
            ntf.setLatestEventInfo(mService, COLOR_SEARCH_RECURSE_TIP, "Utest", null);
            LinearLayout group = new LinearLayout(TBApplication.getContext());
            RemoteViews tempView = ntf.contentView;
            ViewGroup event = (ViewGroup) tempView.apply(TBApplication.getContext(), group);
            recurseGroup(event);
            group.removeAllViews();
        } catch (Exception e) {
            mSystemNotificationTextColor = null;
        }
    }

    public KGNotificationOperation(Service service) {
        this.mService = service;
        extractColors();
        // 建立一个返回的Intent
        mBackIntent = new Intent(Intent.ACTION_MAIN);
        mBackIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName className = new ComponentName(mService, MainActivity.class);
        mBackIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mBackIntent.setComponent(className);
    }

    public PendingIntent createBackPendingIntent() {
        Intent backIntent;
        backIntent = new Intent(Intent.ACTION_MAIN);
        backIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName className = new ComponentName(mService, MainActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        backIntent.setComponent(className);

        return PendingIntent.getActivity(mService, 0, backIntent, 0);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"handleMessage:msg.what=" + msg.what);
            TimeComsuerUtil.getInstance().start("showNotification inUI");
            switch (msg.what) {
                case MSG_SHOW_NOTIFICTION:
                    Log.i(TAG,"MSG_SHOW_NOTIFICTION");

                    if (mNotification == null) {
                        mNotification = new Notification();
                    }
                    mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
                    mNotification.icon = R.drawable.stat_notify_musicplayer;
                    mNotification.tickerText = mDisplayName;
                    mNotification.contentView = smallContentView;
                    mNotification.contentIntent = PendingIntent.getActivity(mService, 0,
                            mBackIntent, 0);
                    try {
                        TimeComsuerUtil.getInstance().start("startForeground");
                        mService.startForeground(TBService.PLAYBACKSERVICE_STATUS, mNotification);
                        TimeComsuerUtil.getInstance().end("startForeground");
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_CLOSE_NOTIFICTION:
                    mService.stopForeground(true);
                    isHasAvatar = false;
                    break;
                case MSG_RESET_NOTIFICTION:
                    showNotification(new Object().hashCode()%2==0?FROM_KUGOUFM
                            : FROM_KUGOUMUSIC, NOTIMODE_PAUSE);
                    isHasAvatar = false;
                    break;
                case MSG_SHOW_FM_NOTIFICATION:
                    showNotification(FROM_KUGOUFM, NOTIMODE_PLAY);
                    break;
                default:
                    break;
            }
            TimeComsuerUtil.getInstance().end("showNotification inUI");
        }
    };

    public void cancelNotification() {
        ((NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(TBService.PLAYBACKSERVICE_STATUS);
        mHandler.removeMessages(MSG_SHOW_NOTIFICTION);
        mHandler.removeMessages(MSG_CLOSE_NOTIFICTION);
        mHandler.sendEmptyMessageDelayed(MSG_CLOSE_NOTIFICTION, 0);
    }

    public void resetNotification() {
        mHandler.removeMessages(MSG_SHOW_NOTIFICTION);
        mHandler.removeMessages(MSG_RESET_NOTIFICTION);
        mHandler.sendEmptyMessageDelayed(MSG_RESET_NOTIFICTION, 0);
    }

    /**
     * 有头像的时候生成通知栏
     */
    public void showNotification(int from, int notifyMode, String avatarPath) {
        Log.i("showNotification",
                "showNotification(int notifyMode, String avatarPath) ----isExitApp:");
        if (!TextUtils.isEmpty(avatarPath)) {
            mNotification = new Notification();
            mAvatarPath = avatarPath;
            Log.i("showNotification", "showNotification(int,int,string):" + notifyMode);
            setNotiPlayingMode(notifyMode);
            showNotification(from, true);
        }
    }

    /**
     * 根据播放状态notifyMode刷新通知栏
     */
    public void showNotification(final int from, final int notifyMode) {
        Log.i("showNotification", "showNotification(int notifyMode)----isExitApp:");
        // if (PlayerOwner.getInstance().getCurrentOwner() == PlayerOwner.Kgfm
        // || PlayerOwner.getInstance().getCurrentOwner() == PlayerOwner.KgAudio
        // || PlayerOwner.getInstance().getCurrentOwner() == PlayerOwner.none
        // || PlayerOwner.getInstance().getCurrentOwner() ==
        // PlayerOwner.Kgringtone
        // || PlayerOwner.getInstance().getCurrentOwner() ==
        // PlayerOwner.Kgringtone_make) {

        mFmAvatar = null;
        mAvatarPath = "";
        mNotification = new Notification();
        if (from == FROM_KUGOUFM) {
        } else {
            String singName = "我是一个兵";
            // 合唱的多个歌手，取第一个
            String temp = "、";
            if (!TextUtils.isEmpty(singName) && singName.contains(temp)) {
                singName = singName.substring(0, singName.indexOf(temp));
            }
        }
        Log.i("showNotification", "showNotification(int,int):" + notifyMode);
        setNotiPlayingMode(notifyMode);
        showNotification(from, true);
        // }

    }

    /**
     * 生成提示栏
     */
    private void showNotification(int from, boolean isMinDelay) {
        Log.d("zlx_lyric", "-----> showNotification");
        try {
            // 生成提示栏
            isHasAvatar = false;
            mNotificationType = from;
            if (from == FROM_KUGOUFM) {
                mDisplayName = "我是一个兵";
            } else {
                mDisplayName = "我是一个兵";
            }
            mSingerName = "我是一个兵";
            if ("".equals(mSingerName)) {
                mSingerName = "未知歌手";
            }
            mTrackName = "我是一个兵";
            if (TextUtils.isEmpty(mDisplayName)) {
                mDisplayName = mService.getResources().getString(R.string.kugou_slogan);
                mTrackName = mDisplayName;
            }
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                constructSuperCommonAndBigBar(from);
            } else if (android.os.Build.VERSION.SDK_INT >= 15) {
                constructSuperCommonBar(from);
            } else {
                constructCommonBar(from);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("zlx_lyric", "MSG_SHOW_NOTIFICTION<---");
        mHandler.removeMessages(MSG_SHOW_NOTIFICTION);
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_NOTIFICTION,
                isMinDelay ? SHOW_NOTI_MINI_DELAY_TIME : SHOW_NOTI_MAX_DELAY_TIME);
    }

    /**
     * isforce生成提示栏
     */
    private void showNotification(int from, int notifyMode, boolean isforce) {

        mNotification = new Notification();
        String singName = "我是一个兵";
        // 合唱的多个歌手，取第一个
        String temp = "、";
        if (!TextUtils.isEmpty(singName) && singName.contains(temp)) {
            singName = singName.substring(0, singName.indexOf(temp));
        }
        // if
        // (PlaybackServiceUtil.isMatchArtistName(singName).equals(ArtistMatcher.UNKNOWN_ARITST))
        // {
        // String temp2 = "-";
        // if (!TextUtils.isEmpty(singName) && singName.contains(temp2)) {
        // singName = singName.substring(0, singName.indexOf(temp2));
        // }
        // } else {
        // singName = PlaybackServiceUtil.isMatchArtistName(singName);
        // }
        setNotiPlayingMode(notifyMode);
        showNotification(from, true);
    }

    // private synchronized boolean isNotiPlayingMode() {
    // KGLog.e("nathaniel", "play state:" + mNotiMode);
    // return mNotiMode == NOTIMODE_PLAY ? true : false;
    // }

    private synchronized void setNotiPlayingMode(int mode) {
        mNotiMode = mode;
    }

    /**
     * 一个普通，啥都没的通知栏
     */
    synchronized private void constructCommonBar(int from) {
        smallContentView = new RemoteViews(mService.getPackageName(), R.layout.statusbar_nobtn);
        if(mFmAvatar != null) {
            smallContentView.setImageViewBitmap(R.id.statusbar_artist_image, mFmAvatar);
            smallContentView.setViewVisibility(R.id.kugou_logo, View.VISIBLE);
        } else {
            isHasAvatar = false;
            smallContentView.setImageViewResource(R.id.statusbar_artist_image,
                    R.drawable.notification_default_icon);
            smallContentView.setViewVisibility(R.id.kugou_logo, View.GONE);
        }
        smallContentView.setTextViewText(R.id.statusbar_track_name, mDisplayName);
        if (mSystemNotificationTextColor != null) {
            smallContentView.setTextColor(R.id.statusbar_track_name, mSystemNotificationTextColor);
            // smallContentView.setTextColor(R.id.kg_title,
            // mSystemNotificationTextColor);
        }
    }

    /**
     * 一个带头像和播放暂停、下一首按钮的通知栏
     */
    synchronized private void constructSuperCommonBar(int from) {
        final ComponentName serviceName = new ComponentName(mService, TBService.class);
        smallContentView = new RemoteViews(mService.getPackageName(), R.layout.statusbar);

        Intent intentNext = new Intent(TBService.NEXT_ACTION_FROM_NOTI).putExtra(
                TBService.STATUSBAR_FLAG, true);
        Intent intentUnlock = new Intent(TBService.UNLOCK_MINI_LYRIC_FORM_NOTI);
        Intent intentPlayOrPause = new Intent(TBService.TOGGLEPAUSE_FROM_NOTI).putExtra(
                TBService.STATUSBAR_FLAG, true);
        Intent intentExitKugou = new Intent(TBService.EXIT_KUGOU_BY_STATUSBAR);
        Intent intentShowMiniLyric = new Intent(TBService.STATUSBAR_FLAG);
        if (from == FROM_KUGOUFM) {
            intentNext.putExtra("fromFM", Boolean.TRUE);
            intentPlayOrPause.putExtra("fromFM", Boolean.TRUE);
        }

        intentUnlock.setComponent(serviceName);
        intentNext.setComponent(serviceName);
        intentPlayOrPause.setComponent(serviceName);
        intentExitKugou.setComponent(serviceName);
        intentShowMiniLyric.setComponent(serviceName);

        PendingIntent pIntentNext = PendingIntent.getService(mService, 0, intentNext,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentUnlock = PendingIntent.getService(mService, 0, intentUnlock,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentPlayOrPause = PendingIntent.getService(mService, 0, intentPlayOrPause,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentExitKugou = PendingIntent.getService(mService, 0, intentExitKugou,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentMiniLyric = PendingIntent.getService(mService, 0, intentShowMiniLyric,
                PendingIntent.FLAG_CANCEL_CURRENT);
        smallContentView
                .setOnClickPendingIntent(R.id.statusbar_super_content_next_btn, pIntentNext);

        smallContentView.setOnClickPendingIntent(R.id.statusbar_super_content_pause_or_play,
                pIntentPlayOrPause);
        smallContentView.setOnClickPendingIntent(R.id.statusbar_super_content_close_btn,
                pIntentExitKugou);
        smallContentView.setOnClickPendingIntent(R.id.statusbar_small_lyric_btn,
                needShowUnlockView(from) ? pIntentUnlock : pIntentMiniLyric);
        if (mFmAvatar != null) {
            smallContentView.setImageViewBitmap(R.id.statusbar_artist_image, mFmAvatar);
            smallContentView.setViewVisibility(R.id.kugou_logo, View.VISIBLE);
        } else {
            isHasAvatar = false;
            smallContentView.setImageViewResource(R.id.statusbar_artist_image,
                    R.drawable.notification_default_icon);
            smallContentView.setViewVisibility(R.id.kugou_logo, View.GONE);
        }
        // smallContentView.setTextViewText(R.id.statusbar_track_name,
        // mDisplayName);
        if (from == FROM_KUGOUFM) {
            smallContentView.setViewVisibility(R.id.statusbar_small_lyric_btn, View.GONE);
            smallContentView.setViewVisibility(R.id.statusbar_singer_name, View.GONE);
            smallContentView.setTextViewText(R.id.statusbar_song_name, mDisplayName);
        } else {
            smallContentView.setViewVisibility(R.id.statusbar_small_lyric_btn, View.VISIBLE);
            smallContentView.setViewVisibility(R.id.statusbar_singer_name, View.VISIBLE);
            smallContentView.setTextViewText(R.id.statusbar_singer_name, mSingerName);
            smallContentView.setTextViewText(R.id.statusbar_song_name, mTrackName);
        }
        if (mService.getResources().getString(R.string.kugou_slogan).equals(mTrackName)) {
            smallContentView.setViewVisibility(R.id.statusbar_singer_name, View.GONE);
        }
        if (mSystemNotificationTextColor != null) {
            smallContentView.setTextColor(R.id.statusbar_song_name, mSystemNotificationTextColor);
            // smallContentView.setTextColor(R.id.kg_title,
            // mSystemNotificationTextColor);
        }

        // if (isNotiPlayingMode()) {
            smallContentView.setImageViewResource(R.id.statusbar_super_content_pause_or_play,R.drawable.kg_btn_noti_pause);
    }

    /**
     * 一个带头像和下一首按钮和超大BIG控制栏的通知栏
     */
    synchronized private void constructSuperCommonAndBigBar(int from) {
        final ComponentName serviceName = new ComponentName(mService, TBService.class);
        boolean isOpen = false;

        Intent intentNext = new Intent(TBService.NEXT_ACTION_FROM_NOTI).putExtra(
                TBService.STATUSBAR_FLAG, true);
        Intent intentPlayOrPause = new Intent(TBService.TOGGLEPAUSE_FROM_NOTI).putExtra(
                TBService.STATUSBAR_FLAG, true);
        Intent intentPrev = new Intent(TBService.PREVIOUS_ACTION_FROM_NOTI).putExtra(
                TBService.STATUSBAR_FLAG, true);
        Intent intentUnlock = new Intent(TBService.UNLOCK_MINI_LYRIC_FORM_NOTI);
        Intent intentExitKugou = new Intent(TBService.EXIT_KUGOU_BY_STATUSBAR);
        Intent intentFM = new Intent(TBService.FM_BY_STATUSBAR).putExtra(
                TBService.STATUSBAR_FLAG, true);
        Intent intentShowMiniLyric = new Intent(
                !isOpen ? TBService.OPEN_MINI_LYRIC_FORM_NOTI
                        : TBService.CLOSE_MINI_LYRIC_FORM_NOTI).putExtra(
                TBService.STATUSBAR_FLAG, true);
        if (from == FROM_KUGOUFM) {
            intentNext.putExtra("fromFM", Boolean.TRUE);
            intentPlayOrPause.putExtra("fromFM", Boolean.TRUE);
            intentPrev.putExtra("fromFM", Boolean.TRUE);
        }

        intentUnlock.setComponent(serviceName);
        intentNext.setComponent(serviceName);
        intentPlayOrPause.setComponent(serviceName);
        intentPrev.setComponent(serviceName);
        intentExitKugou.setComponent(serviceName);
        intentShowMiniLyric.setComponent(serviceName);

        PendingIntent pIntentNext = PendingIntent.getService(mService, 0, intentNext,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentUnlock = PendingIntent.getService(mService, 0, intentUnlock,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentPlayOrPause = PendingIntent.getService(mService, 0, intentPlayOrPause,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentPrev = PendingIntent.getService(mService, 0, intentPrev,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentExitKugou = PendingIntent.getService(mService, 0, intentExitKugou,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentFM = PendingIntent.getService(mService, 0, intentFM,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntentMiniLyric = PendingIntent.getService(mService, 0, intentShowMiniLyric,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // 大的BAR
        bigContentView = new RemoteViews(mService.getPackageName(),
                R.layout.statusbar_big_content_view);
        bigContentView.setOnClickPendingIntent(R.id.statusbar_big_content_next_btn, pIntentNext);
        bigContentView.setOnClickPendingIntent(R.id.statusbar_big_content_unlock_layout, pIntentUnlock);
        bigContentView.setOnClickPendingIntent(R.id.statusbar_big_content_prev_btn, pIntentPrev);
        bigContentView.setOnClickPendingIntent(R.id.statusbar_big_content_pause_or_play,
                pIntentPlayOrPause);
        bigContentView.setOnClickPendingIntent(R.id.statusbar_big_content_close_btn,
                pIntentExitKugou);
        bigContentView.setOnClickPendingIntent(R.id.statusbar_big_lyric_btn, pIntentMiniLyric);
        if (from == FROM_KUGOUFM) {
            // 正在播放FM，只显示FM标志
            bigContentView.setViewVisibility(R.id.statusbar_big_content_play_mode, View.VISIBLE);
            bigContentView.setViewVisibility(R.id.statusbar_big_lyric_btn, View.GONE);
            bigContentView.setImageViewResource(R.id.statusbar_big_content_play_mode,
                    R.drawable.abc_ab_share_pack_mtrl_alpha);
            bigContentView.setOnClickPendingIntent(R.id.statusbar_big_content_play_mode, pIntentFM);
        } else {
            bigContentView.setViewVisibility(R.id.statusbar_big_content_play_mode, View.GONE);
            bigContentView.setViewVisibility(R.id.statusbar_big_lyric_btn, View.VISIBLE);
        }

        bigContentView.setViewVisibility(R.id.kg_big_notification_top_line,
                needShowUnlockView(from) ? View.GONE  : View.VISIBLE);
        bigContentView.setViewVisibility(R.id.statusbar_big_content_unlock_layout,
                needShowUnlockView(from) ? View.VISIBLE : View.GONE);

        // 小的BAR
        smallContentView = new RemoteViews(mService.getPackageName(), R.layout.statusbar);
        smallContentView
                .setOnClickPendingIntent(R.id.statusbar_super_content_next_btn, pIntentNext);
        smallContentView.setOnClickPendingIntent(R.id.statusbar_super_content_pause_or_play,
                pIntentPlayOrPause);
        smallContentView.setOnClickPendingIntent(R.id.statusbar_super_content_close_btn,
                pIntentExitKugou);
        smallContentView.setOnClickPendingIntent(R.id.statusbar_small_lyric_btn,
                needShowUnlockView(from) ? pIntentUnlock : pIntentMiniLyric);
        switchLyricSign(isOpen, from);

        bigContentView.setTextViewText(R.id.statusbar_track_name, mDisplayName);
        if (from == FROM_KUGOUFM) {
            smallContentView.setViewVisibility(R.id.statusbar_small_lyric_btn, View.GONE);
            smallContentView.setViewVisibility(R.id.statusbar_singer_name, View.GONE);
            smallContentView.setTextViewText(R.id.statusbar_song_name, mDisplayName);
        } else {
            smallContentView.setViewVisibility(R.id.statusbar_small_lyric_btn, View.VISIBLE);
            smallContentView.setViewVisibility(R.id.statusbar_singer_name, View.VISIBLE);
            smallContentView.setTextViewText(R.id.statusbar_singer_name, mSingerName);
            smallContentView.setTextViewText(R.id.statusbar_song_name, mTrackName);
        }
        if (mService.getResources().getString(R.string.kugou_slogan).equals(mTrackName)) {
            smallContentView.setViewVisibility(R.id.statusbar_singer_name, View.GONE);
        }

        if (mSystemNotificationTextColor != null) {
            smallContentView.setTextColor(R.id.statusbar_song_name, mSystemNotificationTextColor);
            // smallContentView.setTextColor(R.id.kg_title,
            // mSystemNotificationTextColor);
            bigContentView.setTextColor(R.id.statusbar_track_name, mSystemNotificationTextColor);
        }

        if (mFmAvatar != null) {
            bigContentView.setImageViewBitmap(R.id.statusbar_artist_image, mFmAvatar);
            bigContentView.setViewVisibility(R.id.kugou_logo, View.VISIBLE);
            smallContentView.setImageViewBitmap(R.id.statusbar_artist_image, mFmAvatar);
            smallContentView.setViewVisibility(R.id.kugou_logo, View.VISIBLE);
        } else {
            isHasAvatar = false;
            bigContentView.setImageViewResource(R.id.statusbar_artist_image,
                    R.drawable.notification_default_icon);
            bigContentView.setViewVisibility(R.id.kugou_logo, View.GONE);
            smallContentView.setImageViewResource(R.id.statusbar_artist_image,
                    R.drawable.notification_default_icon);
            smallContentView.setViewVisibility(R.id.kugou_logo, View.GONE);
        }
        // 播放按钮的状态应该依赖于是否在播放歌曲，而不是依赖于变量
        // if (isNotiPlayingMode()) {
        if (new Object().hashCode()%2==0) {
            bigContentView.setImageViewResource(R.id.statusbar_big_content_pause_or_play,
                    R.drawable.kg_btn_noti_pause);
            smallContentView.setImageViewResource(R.id.statusbar_super_content_pause_or_play,
                    R.drawable.kg_btn_noti_pause);
        } else {
            bigContentView.setImageViewResource(R.id.statusbar_big_content_pause_or_play,
                    R.drawable.kg_btn_noti_play);
            smallContentView.setImageViewResource(R.id.statusbar_super_content_pause_or_play,
                    R.drawable.kg_btn_noti_play);
        }

        try {
            Field field = mNotification.getClass().getDeclaredField("bigContentView");
            field.set(mNotification, bigContentView);
            field = mNotification.getClass().getDeclaredField("priority");
            Field maxPro = mNotification.getClass().getDeclaredField("PRIORITY_MAX");
            int maxProprity = maxPro.getInt(null);
            field.set(mNotification, maxProprity);
        } catch (Exception e) {
        }
    }

    private boolean needShowUnlockView(int from) {
        return false;
    }

    private void switchLyricSign(boolean isOpen, int from) {
        // TODO Auto-generated method stub
        int drawableId = isOpen ? R.drawable.kg_btn_noti_lyr_open : R.drawable.kg_btn_noti_lyr_close;
        if (bigContentView != null) {
            bigContentView.setImageViewResource(R.id.statusbar_big_lyric_btn, drawableId);
        }
        drawableId = needShowUnlockView(from) ? R.drawable.kg_btn_noti_lyr_unlock : drawableId;
        smallContentView.setImageViewResource(R.id.statusbar_small_lyric_btn, drawableId);
    }

    public boolean getIsHasAvatar() {
        return isHasAvatar;
    }

    private String getSingerNameFromDatabase() {
        return "我是一个兵";
    }

    // private void setPlayModeImage() {
    // if (bigContentView == null)
    // return;
    // PlayMode curPlaymode = PlaybackServiceUtil.getPlayMode();
    // switch (curPlaymode) {
    // case REPEAT_SINGLE:
    // bigContentView.setImageViewResource(R.id.statusbar_big_content_play_mode,
    // R.drawable.ic_player_mode_single_default);
    // break;
    // case REPEAT_ALL:
    // bigContentView.setImageViewResource(R.id.statusbar_big_content_play_mode,
    // R.drawable.ic_player_mode_all_default);
    //
    // break;
    //
    // case RANDOM:
    // bigContentView.setImageViewResource(R.id.statusbar_big_content_play_mode,
    // R.drawable.ic_player_mode_random_default);
    //
    // break;
    // default:
    // bigContentView.setImageViewResource(R.id.statusbar_big_content_play_mode,
    // R.drawable.ic_player_mode_all_default);
    // break;
    // }
    //
    // }

    public void registerReceiver() {
    }

    /**
     * 获取当前状态栏显示的类型
     * 
     * @return 1 酷狗音乐 2 酷狗FM
     */
    public static int getNotificationType() {
        return mNotificationType;
    }
}
