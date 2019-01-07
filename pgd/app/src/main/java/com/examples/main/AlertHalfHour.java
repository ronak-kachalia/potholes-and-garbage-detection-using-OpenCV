package com.examples.main;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by YashJain on 1/11/2017.
 */

public class AlertHalfHour extends BroadcastReceiver {
    Context context;
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int Sec=intent.getIntExtra("sec", 0);
        String Srn = intent.getStringExtra("Srn"),var="";

        if (isNetworkAvailable()) {
            try {
                var = new RetrivingData("getEntry",Srn).execute().get();
                String [] part=var.split("a");
                int count = 0;
                String v = part[0];
                        if (v.equals("1")) {
                            NotificationManager manager1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, Sec, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                            //AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                            NotificationCompat.Builder mBuilder1 = new NotificationCompat.Builder(context);
                            mBuilder1.setSmallIcon(R.mipmap.ic_launcher);
                            mBuilder1.setContentTitle("Hurray Problem solved!!!");
                            mBuilder1.setContentText("Click for more description for " + Srn);
                            mBuilder1.setContentIntent(pendingIntent1);
                            mBuilder1.setDefaults(NotificationCompat.DEFAULT_SOUND);
                            mBuilder1.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
                            mBuilder1.setAutoCancel(true);
                            if (notifSetter(context))
                            manager1.notify(Sec, mBuilder1.build());
                            cancelAlarm(context, Sec);
                            //alarmManager.cancel(pendingIntent1);

                    }
            }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void cancelAlarm(Context context,int Sec) {
        Intent intent = new Intent(context, AlertHalfHour.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, Sec, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    boolean notifSetter(Context c)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        boolean notif = prefs.getBoolean("notifications_new_message",true);
        return notif;
    }
}
