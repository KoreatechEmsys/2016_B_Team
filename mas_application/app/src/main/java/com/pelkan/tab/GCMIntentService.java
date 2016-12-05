package com.pelkan.tab;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.android.gcm.GCMBaseIntentService;

import java.util.Calendar;

/**
 * Created by JangLab on 2016-08-22.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @Override
    protected void onError(Context arg0, String arg1) {}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMessage(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Tab.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("DSM 알리미")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setTicker("DSM 알리미 - 소식 왔어요!")
                .setAutoCancel(true);
        Notification notification = builder.build();
        nm.notify(Calendar.getInstance().get(Calendar.MILLISECOND), notification);
    }

    @Override
    protected void onRegistered(Context context, final String reg_id) {
        //키가 등록됨
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        //키가 제거됨
    }
}
