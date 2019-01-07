package com.examples.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

class BackgroundWorker extends AsyncTask<Object, Object, Void> {
    UserDbHelper db;
    int Sla, Status, type;
    private double Loc1, Loc2;
    private  String operation, Description, Srn;
    Context context;
    private String result = "", filename,path;
    private Bitmap Image;
    private Long res1=null;
    String srn;
    String udp;
    String sec1;
    BackgroundWorker(Context context, String operation, String Srn, double Loc1, double Loc2, int type, String Description, Bitmap img, String filename, String path) {
        this.Srn = Srn;
        this.Loc1 = Loc1;
        this.Loc2 = Loc2;
        this.type = type;
        this.operation = operation;
        this.Description = Description;
        this.context = context;
        this.Image = img;
        this.filename = filename;
        this.path=path;
    }
    private ProgressDialog progressdialog;
    any a=new any();
    protected void onPreExecute(){
        super.onPreExecute();

        if (a.ret2()==1){
        progressdialog= new ProgressDialog(context);
        progressdialog.setMessage("Registering Problem");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
    }


    @Override
    protected Void doInBackground(Object... voids) {
        String login_url = "http://vpray.esy.es/InsertAndUpdate1.php";
        String line = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        if (operation.equals("insertInformation")) {
            try {
                URL url = new URL(login_url);
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8") + "&"
                        + URLEncoder.encode("Loc1", "UTF-8") + "=" + Loc1 + "&"
                        + URLEncoder.encode("Loc2", "UTF-8") + "=" + Loc2 + "&"
                        + URLEncoder.encode("Type", "UTF-8") + "=" + type + "&"
                        + URLEncoder.encode("encoded_string", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8") + "&"
                        + URLEncoder.encode("image_name", "UTF-8") + "=" + URLEncoder.encode(filename, "UTF-8") + "&"
                        + URLEncoder.encode("Description", "UTF-8") + "=" + URLEncoder.encode(Description, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                while ((line = bufferedReader.readLine()) != null) {
                    result = line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                UserDbHelper db;
                db = new UserDbHelper(context);
                result=result.trim();
                if(!result.equals(2)){
                String[] par = result.split("x");
                String[] parts = par[1].split("a");
                srn = parts[0];
                udp = par[0];
                res1=db.insertinformation(srn, "insertSrn", "", "", 0, 0, 0, "", "");
                    String first = srn.substring(0, 1);
                    String first1 = srn.substring(5, 11);
                    String sec = first + first1 + "1";
                    sec1 = first + first1 + "2";
                if (udp.equals("0")) {
                    String[] parts1 = parts[1].split("b");
                    double l1 = Double.parseDouble(parts1[0]);
                    String[] parts2 = parts1[1].split("c");
                    double l2 = Double.parseDouble(parts2[0]);
                    String[] parts3 = parts2[1].split("d");
                    String dp = parts3[0];
                    String[] part4 = parts3[1].split("e");
                    int x = 0;
                    if (!part4[0].equals(""))
                        x = Integer.parseInt(part4[0]);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Date Date1 = null, Date2 = null;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(calendar.getTime());
                    Date1 = sdf.parse(dp);
                    Date2 = sdf.parse(formattedDate);
                    long r = (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);

                    int p = (int) r;
                    x = x - p;
                    if (x <= 0)
                        x = 1;
                    if (res1 > 0) {
                        SlaAlert(sec, srn, x);
                        HourtoHourInternet(sec1, srn);
                    }

                }
                    if (udp.equals("1"))
                        HourtoHourInternet(sec1, srn);
                }
                else if (result.equals("2")||udp.equals("1")){
                    HourtoHourInternet(sec1, srn);
                }
            } catch (NullPointerException |UnknownHostException|ProtocolException|ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operation.equals("onupdate")) {
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Srn", "UTF-8") + "=" + URLEncoder.encode(Srn, "UTF-8") + "&"
                        + URLEncoder.encode("Description", "UTF-8") + "=" + URLEncoder.encode(Description, "UTF-8") + "&"
                        + URLEncoder.encode("Operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                while ((line = bufferedReader.readLine()) != null) {
                    result = line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (a.ret2()==1)
        progressdialog.dismiss();
        any.j=0;
        if ((!result.equals(""))){
            if (result.equals("2")){
                Toast.makeText(context, "Problem has already been updated as solved or not been reported here before", Toast.LENGTH_LONG).show();
            }
            else if (res1==0&&udp.equals("0")) {
                Toast.makeText(context, "Problem already exist in my complaints", Toast.LENGTH_LONG).show();
            }
            else if (res1==0&&udp.equals("1")){
                Toast.makeText(context, "Problem updated successfully", Toast.LENGTH_LONG).show();
            }
            else{
                if (udp.equals("1")&&res1==1)
                    Toast.makeText(context, "Successfully Updated and added to my issue", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Successfull Added", Toast.LENGTH_LONG).show();
                File G=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"BACKUP");
                if (!G.exists()) {
                    if (!G.mkdirs()) {
                        Log.d("BACKUP", "failed to create directory");
                    }
                }
                File file=new File(G.getPath()+"/"+srn+".txt");
                if (!file.exists()) {
                    try {
                        FileWriter f = new FileWriter(file);
                        f.flush();
                        f.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else {

            Toast.makeText(context, "Unsuccessfull in loading", Toast.LENGTH_LONG).show();
        }

    }
    private void SlaAlert(String sec,String s,int x){
        Long alertTime1=new GregorianCalendar().getTimeInMillis()+60*60*24*x*1000;

        Intent alertIntent=new Intent(context,AlertReceiver.class);
        alertIntent.putExtra("Srn", s);
        alertIntent.putExtra("sec", sec);
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime1, PendingIntent.getBroadcast(context,Integer.parseInt(sec),alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
    }
    private void HourtoHourInternet(String sec,String s){
        Calendar cal=Calendar.getInstance();
        int hr=cal.getTime().getHours();
        int min=cal.getTime().getMinutes();
        int second=cal.getTime().getSeconds();

        cal.set(Calendar.HOUR_OF_DAY,hr);
        cal.set(Calendar.MINUTE,min);
        cal.set(Calendar.SECOND,second);

        Intent alertIntent1=new Intent(context,AlertHalfHour.class);
        alertIntent1.putExtra("Srn",s);
        alertIntent1.putExtra("sec",Integer.parseInt(sec));
        AlarmManager alarmManager1=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+10*1000,AlarmManager.INTERVAL_HOUR,PendingIntent.getBroadcast(context,Integer.parseInt(sec),alertIntent1,PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
