package com.examples.main;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by YashJain on 1/1/2017.
 * For sla only
 */
public class AlertReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        String Srn=intent.getStringExtra("Srn");
        String sec1=intent.getStringExtra("sec");
        int sec=Integer.parseInt(sec1);
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,sec,new Intent(context,SplashScreen.class),PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Citizen Service");
        mBuilder.setContentText("Update for SRN:"+Srn);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        mBuilder.setAutoCancel(true);
        if (notifSetter(context))
        manager.notify(sec,mBuilder.build());
        Log.e("Alert","AlertReceiver ");
        alarm(Srn);
    }
    public void alarm(String srn){
        String first = srn.substring(0, 1);
        String first1 = srn.substring(5, 11);
        String sec1 = first + first1 + "3";
        int sec=Integer.parseInt(sec1);
        Calendar calendar=Calendar.getInstance();
        int hr=calendar.getTime().getHours();
        int min=calendar.getTime().getMinutes();
        int second=calendar.getTime().getSeconds();
        calendar.set(Calendar.HOUR_OF_DAY,hr);
        calendar.set(Calendar.MINUTE,min);
        calendar.set(Calendar.SECOND,second);
        Intent alertIntent=new Intent(context,AlertReceiver1.class);
        alertIntent.putExtra("Srn",srn);
        alertIntent.putExtra("sec",sec);
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+60*60*24*1000,AlarmManager.INTERVAL_DAY,PendingIntent.getBroadcast(context,sec,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
    }
    boolean notifSetter(Context c)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        boolean notif = prefs.getBoolean("notifications_new_message",true);
        return notif;
    }
    
}
