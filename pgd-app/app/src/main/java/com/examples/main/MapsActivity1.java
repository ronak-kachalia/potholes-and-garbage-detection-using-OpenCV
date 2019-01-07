package com.examples.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MapsActivity1 extends Fragment implements FragmentInterface,OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{
    MapView m;
    String var;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String s="";
    EditText text;
    ProgressDialog progress;
    MainActivity main = new MainActivity();
    int count_=0;
    String main_url="http://vpray.esy.es/MyWholeDatabase.php",result="",line="";
    private LatLngBounds BOUNDS = new LatLngBounds(new LatLng(18.894423, 72.809072), new LatLng(19.596876, 73.393253));
    public static GoogleMap mMap;
    int search_ = 0;
    com.shamanland.fab.FloatingActionButton f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SettingsActivity settings = new SettingsActivity();
        int ans = settings.sizeSetter(getActivity());
        if(ans==1)
            getActivity().getTheme().applyStyle(R.style.FontStyle_Small,true);
        else if(ans==-1)
            getActivity().getTheme().applyStyle(R.style.FontStyle_Large,true);
        else
            getActivity().getTheme().applyStyle(R.style.FontStyle_Medium,true);
        View v;
        if(isNetworkAvailable()) {
            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_refresh);
            myIcon.setColorFilter(Color.rgb(255,255,255), PorterDuff.Mode.SRC_ATOP);
            count_++;
            v = inflater.inflate(R.layout.fragment_map_issues, container, false);
            m = (MapView) v.findViewById(R.id.mapView);
            m.onCreate(savedInstanceState);
            Button b = (Button) v.findViewById(R.id.search_button);
            text = (EditText) v.findViewById(R.id.editText);
            progress = new ProgressDialog(getActivity());
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            f = (com.shamanland.fab.FloatingActionButton) v.findViewById(R.id.myFAB);
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(s.equals("")) {
                        mMap.clear();
                        search_ = 0;
                    }
                    m.getMapAsync(MapsActivity1.this);
                }
            });
            b.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    View v1 = getActivity().getCurrentFocus();
                    if (v1 != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v1.getWindowToken(), 0);
                    }
                    if(isNetworkAvailable()) {
                        s = text.getText().toString();
                        s = s.trim().replaceAll("[\n.]+","").replaceAll("[ ]{2,}+"," ");
                        text.setText(s);
                        if (s.equals("")) {
                            Toast.makeText(getActivity(), "Enter Something", Toast.LENGTH_LONG).show();
                        } else {
                            search_ = 1;
                            new loadFragment().execute();
                        }
                    }
                    else
                        Toast.makeText(getActivity(),"No Internet Available. Please check connection.",Toast.LENGTH_LONG).show();
                }
            });
            m.getMapAsync(MapsActivity1.this);
        }
        else{
            v = inflater.inflate(R.layout.no, container, false);
            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_refresh);
            myIcon.setColorFilter(Color.rgb(2,119,189), PorterDuff.Mode.SRC_ATOP);
            final LinearLayout t = (LinearLayout) v.findViewById(R.id.refresh_btn);
            t.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Animation animation = new RotateAnimation(0.0f, 360.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    animation.setRepeatCount(0);
                    animation.setDuration(1000);
                    ImageView i = (ImageView) getView().findViewById(R.id.refresh_icon);
                    if(i != null) {
                        i.setAnimation(animation);
                        i.startAnimation(animation);
                        if (isNetworkAvailable()) {
                            MapsActivity1 fragment = (MapsActivity1) getFragmentManager().getFragments().get(3);
                            getFragmentManager().beginTransaction()
                                    .detach(fragment)
                                    .attach(fragment)
                                    .commit();
                        } else
                            Toast.makeText(getActivity(), "No Internet Found", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        return v;
    }

    private HashMap<Marker, String> mHashMap = new HashMap<Marker, String>();
    LatLng mumbai;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mumbai = new LatLng(19.0760,72.8777);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mumbai));
        mMap.setMaxZoomPreference(19.0f);
        mMap.setMinZoomPreference(9.5f);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setLatLngBoundsForCameraTarget(BOUNDS);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        new loadFragment().execute();
        mMap.setOnInfoWindowClickListener(this);
        f.setVisibility(View.VISIBLE);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        final String s = mHashMap.get(marker);

        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setMessage("Do you want to add this issue to 'My Issues' ?");
        alertDialogBuilder.setPositiveButton("Add",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserDbHelper db=new UserDbHelper(getActivity());
                if (!s.equals("")){
                    String var;
                    db.insertinformation(s, "insertSrn", "", "", 0, 0, 0, "", "");

//                    File G=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"BACKUP");
//                    if (!G.exists()) {
//                        if (!G.mkdirs()) {
//                            Log.d("BACKUP", "failed to create directory");
//                        }
//                    }
//                    File file=new File(G.getPath()+"/"+s+".txt");
//                    if (!file.exists()) {
//                        try {
//                            new CountMe(s).execute();
//                            FileWriter f = new FileWriter(file);
//                            f.flush();
//                            f.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }

                    Toast.makeText(getActivity(),"Issue added successfully",Toast.LENGTH_LONG).show();
                    try {
                        var = new RetrivingData("getEntry",s).execute().get(20000, TimeUnit.MILLISECONDS);
                        if(!var.equals("")){
                            String[] part=var.split("a");
                            String date[]=part[1].split("b");
                            String dp=date[0];
                            String sla=date[1];
                            int x=0;
                            if (!sla.equals(""))
                                 x= Integer.parseInt(date[1]);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            Date Date1 = null, Date2 = null;
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            String formattedDate = df.format(calendar.getTime());
                            Date1 = sdf.parse(dp);
                            Date2 = sdf.parse(formattedDate);
                            long r = (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
                            Long alertTime;
                            int p = (int) r;
                            x = x - p;

                            if (x <= 0)
                                x = 1;
                            String first = s.substring(0, 1);
                            String first1 = s.substring(5, 10);
                            String sec = first + first1 + "4";
                            SlaAlert(sec,s,x);
                            HourtoHourInternet(sec,s);
                        }
                    }
                    catch (InterruptedException|ExecutionException|NullPointerException  |TimeoutException|ParseException |ArrayIndexOutOfBoundsException|AndroidRuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isNetworkAvailable()) {
         try{   m.onResume();}
         catch (Exception e)
         {
             e.printStackTrace();
         }
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public void SlaAlert(String sec,String s,int x){
        Long alertTime1=new GregorianCalendar().getTimeInMillis()+60*60*24*x*1000;
        Intent alertIntent=new Intent(getActivity(),AlertReceiver.class);
        alertIntent.putExtra("Srn", s);
        alertIntent.putExtra("sec", sec);
        AlarmManager alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime1, PendingIntent.getBroadcast(getActivity(),Integer.parseInt(sec),alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
    }
    public void HourtoHourInternet(String sec,String s){
        Calendar cal=Calendar.getInstance();
        int hr=cal.getTime().getHours();
        int min=cal.getTime().getMinutes();
        int second=cal.getTime().getSeconds();
        cal.set(Calendar.HOUR_OF_DAY,hr);
        cal.set(Calendar.MINUTE,min);
        cal.set(Calendar.SECOND,second);
        Intent alertIntent1=new Intent(getActivity(),AlertHalfHour.class);
        alertIntent1.putExtra("Srn",s);
        alertIntent1.putExtra("sec",Integer.parseInt(sec));
        AlarmManager alarmManager1=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),/*AlarmManager.INTERVAL_HOUR*/10*1000,PendingIntent.getBroadcast(getActivity(),Integer.parseInt(sec),alertIntent1,PendingIntent.FLAG_UPDATE_CURRENT));
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void fragmentBecameVisible(){
        if(count_==0)
            if(isNetworkAvailable()){
                main.count++;
                count_++;
                MapsActivity1 fragment = (MapsActivity1) getFragmentManager().getFragments().get(3);
                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
            }
    }

    class loadFragment extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute(){
            Animation animation = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setDuration(1000);
            f.setAnimation(animation);
            f.startAnimation(animation);

        }
        @Override
        protected Void doInBackground(Void... params) {
            if(search_ == 1)
                main_url="http://vpray.esy.es/searchmap.php";
            else
                main_url="http://vpray.esy.es/MyWholeDatabase.php";
                try {
                    URL url = new URL(main_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(s, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException | NullPointerException | ArrayIndexOutOfBoundsException | AndroidRuntimeException e) {
                    e.printStackTrace();
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);
            if(search_ == 0) {
                try {
                    if (!result.equals("")) {
                        jsonObject = new JSONObject(result);
                        jsonArray = jsonObject.getJSONArray("response");
                        int count = 0;
                        while (count < jsonArray.length()) {
                            Float l1 = Float.parseFloat(jsonArray.getJSONObject(count).getString("Loc1"));
                            Float l2 = Float.parseFloat(jsonArray.getJSONObject(count).getString("Loc2"));
                            String srn = jsonArray.getJSONObject(count).getString("Srn");
                            mumbai = new LatLng(l1, l2);
                            if (srn.charAt(0) == '1') {
                                Marker m = mMap.addMarker(new MarkerOptions().position(mumbai).title("Add to My Issues?"));
                                m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p));
                                mHashMap.put(m, srn);
                            } else {
                                Marker m = mMap.addMarker(new MarkerOptions().position(mumbai).title("Add to My Issues?"));
                                mHashMap.put(m, srn);
                                m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.g));
                            }
                            count++;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mumbai = new LatLng(19.0760, 72.8777);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mumbai, 10));
            }
            else{
                    Geocoder geocoder = new Geocoder(getActivity());
                    List<Address> addressList = null;
                    try {
                        addressList = geocoder.getFromLocationName(s, 1, 18.894423, 72.809072, 19.596876, 73.393253);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addressList != null && addressList.size() != 0) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            Double minloc1=Double.parseDouble(jsonObject.getString("ml1"));
                            Double minloc2 = Double.parseDouble(jsonObject.getString("nl1"));
                            Double maxloc1 = Double.parseDouble(jsonObject.getString("ml2"));
                            Double maxloc2 = Double.parseDouble(jsonObject.getString("nl2"));
                            minloc1 -= 0.000500;
                            minloc2 -= 0.000500;
                            maxloc1 += 0.000500;
                            maxloc2 += 0.000500;
                            LatLngBounds bounds = new LatLngBounds(new LatLng(minloc1, minloc2), new LatLng(maxloc1, maxloc2));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"No data found in this region",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        AlertDialog.Builder alertDialogBuilder;
                        alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setPositiveButton("Close",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialogBuilder.setMessage("Enter correct location");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
            }

            result="";
            f.clearAnimation();
        }
    }
}
