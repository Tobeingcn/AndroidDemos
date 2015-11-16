package cn.tobeing.notificationdemos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sunzheng on 15/11/9.
 */
public class TBNotificationUtil {
    public static int mNotificationCount=0;
    private static final String TAG="TBNotificationUtil";

    public static void showSimpleNotification(Context context, String title, String content, int id) {
        TimeComsuerUtil.getInstance().start("showSimpleNotification");
        NotificationManager manager = getNotificationManager(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);
        Notification notify1 = new Notification();
        notify1.icon = R.drawable.icon;
        notify1.tickerText = title+mNotificationCount++;
        notify1.when = System.currentTimeMillis();
        notify1.setLatestEventInfo(context, title,
                content, pendingIntent);
        notify1.number = 1;
        notify1.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        // 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
        manager.notify(id, notify1);
        TimeComsuerUtil.getInstance().end("showSimpleNotification");
    }

    public static void showSimpleNotificationV11(Context context, String title, String content, int id) {
        TimeComsuerUtil.getInstance().start("showSimpleNotificationV11");
        NotificationManager manager = getNotificationManager(context);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify2 = new Notification.Builder(context)
                .setSmallIcon(R.drawable.icon) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                        // icon)
                .setTicker(title +mNotificationCount++)// 设置在status
                        // bar上显示的提示文字
                .setContentTitle(title)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText(content)// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(11) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(id, notify2);
        TimeComsuerUtil.getInstance().end("showSimpleNotificationV11");
    }
    public static void showSimpleNotificationV16(Context context, String title, String content, int id) {
        TimeComsuerUtil.getInstance().start("showSimpleNotificationV16");
        NotificationManager manager = getNotificationManager(context);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        Notification notify3 = new Notification.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setTicker(title + mNotificationCount++)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent3).setNumber(16).build(); // 需要注意build()是在API
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        manager.notify(id, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提
        TimeComsuerUtil.getInstance().end("showSimpleNotificationV16");
    }

    public static void showSimpleNotificationVSelf(Context context, String title, String content, int id) {
        TimeComsuerUtil.getInstance().start("showSimpleNotificationVSelf");
        NotificationManager manager = getNotificationManager(context);
        Notification myNotify = new Notification();
        myNotify.icon = R.drawable.icon;
        myNotify.tickerText = title+ mNotificationCount++;
        myNotify.when = System.currentTimeMillis();
        myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.layout_notification_self);
        rv.setTextViewText(R.id.tvTitle, title+";"+content);
        myNotify.contentView = rv;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
                intent, 0);
        myNotify.contentIntent = contentIntent;
        manager.notify(id, myNotify);
        TimeComsuerUtil.getInstance().end("showSimpleNotificationVSelf");
    }
    public static void showSimpleNotificationVSelfCPX(Context context, String title, String content, int id) {
        TimeComsuerUtil.getInstance().start("showSimpleNotificationVSelfCPX");
        NotificationManager manager = getNotificationManager(context);
        Notification myNotify = new Notification();
        myNotify.icon = R.drawable.icon;
        myNotify.tickerText = title+ mNotificationCount++;
        myNotify.when = System.currentTimeMillis();
        myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.statusbar);
        rv.setTextViewText(R.id.tvTitle, title+";"+content);
        myNotify.contentView = rv;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
                intent, 0);
        myNotify.contentIntent = contentIntent;
        TimeComsuerUtil.getInstance().start("notify");
        manager.notify(id, myNotify);
        TimeComsuerUtil.getInstance().end("notify");
        TimeComsuerUtil.getInstance().end("showSimpleNotificationVSelfCPX");
    }

    public static void clearAll(Context context) {
        Log.d(TAG, "clearAll");
        NotificationManager manager =  getNotificationManager(context);
        manager.cancelAll();
    }
    public static void clear(Context context,int id) {
        Log.d(TAG, "clearAll");
        NotificationManager manager = getNotificationManager(context);
        manager.cancel(id);
    }
    private static NotificationManager manager;
    public static NotificationManager getNotificationManager(Context context){
        if(manager==null) {
            manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
