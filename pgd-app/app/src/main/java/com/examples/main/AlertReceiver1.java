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
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by YashJain on 1/2/2017.
 */
public class AlertReceiver1 extends BroadcastReceiver {
    Context context;
    int Sec;
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String []part = new String[3];
        String var="";
        Sec=intent.getIntExtra("sec", 0);
        String Srn = intent.getStringExtra("Srn");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Sec, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Citizen Service");
        mBuilder.setContentText("Update for " +Srn);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        if (isNetworkAvailable()) {      //Intern
            // et is not connected
            try {
                var = new RetrivingData("getEntry",Srn).execute().get();
                part=var.split("a");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


                String v = part[0];
//
                if (v.equals("1")) {
                    NotificationManager manager1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(context, Sec, new Intent(context, SplashScreen.class), PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder1 = new NotificationCompat.Builder(context);
                    mBuilder1.setSmallIcon(R.mipmap.ic_launcher);
                    mBuilder1.setContentTitle("Hurray Problem solved!!!");
                    mBuilder1.setContentText(" " + Srn);
                    mBuilder1.setContentIntent(pendingIntent);
                    mBuilder1.setDefaults(NotificationCompat.DEFAULT_SOUND);
                    mBuilder1.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
                    mBuilder1.setAutoCancel(true);
                    if (notifSetter(context))
                    manager1.notify(Sec, mBuilder1.build());
                    cancelAlarm(context, Sec);
                } else {
                    manager.notify(Sec, mBuilder.build());
                }

        } else {
            manager.notify(Sec, mBuilder.build());
        }
        Log.e("Alert","AlertReceiver 1");
    }
    public static void cancelAlarm(Context context,int Sec) {
        Intent intent = new Intent(context, AlertReceiver1.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, Sec, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    boolean notifSetter(Context c)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        boolean notif = prefs.getBoolean("notifications_new_message",true);
        return notif;
    }
}
