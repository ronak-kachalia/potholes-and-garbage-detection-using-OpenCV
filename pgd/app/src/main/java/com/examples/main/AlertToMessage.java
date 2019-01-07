package com.examples.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by YashJain on 1/14/2017.
 */

public class AlertToMessage extends BroadcastReceiver {
    String title,message;
    Context context;
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        int sec=intent.getIntExtra("sec", 0);
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,sec,new Intent(context,SplashScreen.class),PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Citizen Service");
        mBuilder.setContentText("We have successfully received your problems");
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        mBuilder.setAutoCancel(true);
        manager.notify(sec,mBuilder.build());
    }
}



