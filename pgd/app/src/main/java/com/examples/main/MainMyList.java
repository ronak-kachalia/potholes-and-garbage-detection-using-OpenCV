package com.examples.main;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainMyList extends Fragment implements FragmentInterface{
    private List<Items> ItemList = new ArrayList<>();
    public RecyclerView recyclerView;
    public ItemsAdapter mAdapter;
    String data12="",Image1,Image2;
    int count=0;
    int x=0;
    static TextView text_my_issues;
    //private String Image1,Image2;
    public Items Item;
    private SwipeRefreshLayout swipeContainer;
    public View v = null;
    MainActivity main = new MainActivity();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsActivity settings = new SettingsActivity();
        int ans = settings.sizeSetter(getActivity());
        if(ans==1)
            getActivity().getTheme().applyStyle(R.style.FontStyle_Small,true);
        else if(ans==-1)
            getActivity().getTheme().applyStyle(R.style.FontStyle_Large,true);
        else
            getActivity().getTheme().applyStyle(R.style.FontStyle_Medium,true);
        if(isNetworkAvailable()) {
            count++;
            v = inflater.inflate(R.layout.activity_mylist, container, false);
            text_my_issues = (TextView) v.findViewById(R.id.text_my_issues);
            x=1;
            swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
            mAdapter = new ItemsAdapter(getActivity().getApplicationContext(), ItemList);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            prepareItemsData();
            mAdapter.notifyDataSetChanged();
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    if(isNetworkAvailable()){
                        mAdapter.clear();
                        prepareItemsData();
                        swipeContainer.setRefreshing(false);
                    }
                    else {
                        Toast.makeText(getActivity(), "No Internet Available", Toast.LENGTH_LONG).show();
                    }

                }
            });


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
                    i.setAnimation(animation);
                    i.startAnimation(animation);
                    if(isNetworkAvailable()){
                        main.count ++;
                        count++;
                        MainMyList fragment = (MainMyList) getFragmentManager().getFragments().get(1);
                        getFragmentManager().beginTransaction()
                                .detach(fragment)
                                .attach(fragment)
                                .commit();
                    }
                    else
                        Toast.makeText(getActivity(),"No Internet Found",Toast.LENGTH_LONG).show();

                }
            });
        }
        return v;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void prepareItemsData() {
        /*ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Downloading Music :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();*/
        TextView text = (TextView) v.findViewById(R.id.text_my_issues);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        UserDbHelper t = new UserDbHelper(getActivity());
        String Type;
        String Srn;
        String Address;
        String D_Report;
        String D_Solved,Stat;
        String Status;//,diff;
        Cursor res = t.toDisplayMycompalints();
        while (res.moveToNext()) {
            jsonArray.put(res.getString(0));
        }

        try {
            jsonObject.put("my", jsonArray);
            int count=0;
            data12 = jsonObject.toString();
            String var1 = new RetrivingData1().execute().get(10000, TimeUnit.MILLISECONDS);
            String [] var=var1.split("&");
            JSONObject jsonObject1 = new JSONObject(var[1]);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("response");
                while (count < jsonArray1.length()) {
                    text.setVisibility(View.GONE);
                    Address = jsonArray1.getJSONObject(count).getString("Address");
                    Srn = jsonArray1.getJSONObject(count).getString("Srn");
                    if (Srn.charAt(0) == '1')
                        Type = "Pothole";
                    else
                        Type = "Garbage";
                    D_Report = jsonArray1.getJSONObject(count).getString("D_Report");
                    D_Solved = jsonArray1.getJSONObject(count).getString("D_Solved");
                    Status = jsonArray1.getJSONObject(count).getString("Status");
                    if (Status.equals("0"))
                        Stat = "Unsolved";
                    else
                        Stat = "Solved";
                    if (D_Solved.equals("0000-00-00"))
                        D_Solved = "--";
                    Image1 = jsonArray1.getJSONObject(count).getString("Image1");
                    Image2 = jsonArray1.getJSONObject(count).getString("Image2");
                    String details = new RetrivingData("getEntry",Srn).execute().get();
                    String []part=details.split("a");
                    String v=part[0];
                    String []part1=part[1].split("b");
                    String sla=part1[1];
                    int x=Integer.parseInt(sla);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Date Date1 = sdf.parse(D_Report);
                    Date Date2 = sdf.parse(var[0]);
                    long r = (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
                    int p = (int) r;
                    x = x - p;
                    String Sla=Integer.toString(x);
                    Item = new Items(Type, Srn, Address, D_Report, D_Solved, Sla, Stat,Image1,Image2);
                    ItemList.add(Item);
                    count++;

                }
            if(count == 0){
                text.setVisibility(View.VISIBLE);
            }
        } catch (JSONException | InterruptedException | ExecutionException | NullPointerException e) {
            e.printStackTrace();
            text.setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void fragmentBecameVisible(){
        if(count==0)
        if(isNetworkAvailable()){
            count++;
            main.count++;
            MainMyList fragment = (MainMyList) getFragmentManager().getFragments().get(1);
            getFragmentManager().beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit();
        }
    }

    class RetrivingData1 extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(Void... params) {
            String login_url = "http://vpray.esy.es/PractiseJsonArraySend.php";
            String result="";
            String line;
            try {
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("encoded_string","UTF-8")+"="+URLEncoder.encode(data12,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected  void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        
    }
}