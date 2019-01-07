package com.examples.main;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Created by YashJain on 1/4/2017.
 */
    public class AlertforInternet extends BroadcastReceiver {
    Context context;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int count = 0;
        UserDbHelper db = new UserDbHelper(context);
        Cursor res1 = db.CheckStatusforEntry();
        int e = res1.getCount();
        if (isNetworkAvailable()) {
            if (e != 0) {

                while (res1.moveToNext()) {

                    try {
                        Bitmap image = BitmapFactory.decodeFile(res1.getString(0));
                        Void s = new BackgroundWorker(context, "insertInformation", "", res1.getDouble(2), res1.getDouble(3), res1.getInt(4), res1.getString(5), image, res1.getString(6), res1.getString(0)).execute().get();


                            Log.e("s","s");
                            db.deldummyEntry(res1.getString(6));
                            db.close();

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
                res1.close();
            } else {
                Cursor res2 = db.CheckStatusforEntry();
                int e1 = res1.getCount();
                if (e1 == 0) {

                    Log.e("Offline", "Succesfully sent");
                    //Toast.makeText(context,"All entries sended offline",Toast.LENGTH_LONG).show();
                    db.deleteEntries();
                    cancelAlarm(context, 30);
                    Long alertTime1=new GregorianCalendar().getTimeInMillis();
                    Intent alertIntent=new Intent(context,AlertToMessage.class);
                    alertIntent.putExtra("sec", 50);
                    AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime1, PendingIntent.getBroadcast(context,50,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));

                }
            }
        }

    }

    public static void cancelAlarm(Context context, int Sec) {
        Intent intent = new Intent(context, AlertforInternet.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 30, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.e("Cancelling Alarm","Alarm for alertforinternet cancelled");
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
