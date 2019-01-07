package com.examples.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Statistics extends Fragment implements FragmentInterface{
    private SwipeRefreshLayout swipeContainer;
    BarChart barChart=null;
    BarData data=null;
    ArrayList<BarEntry> group1,group2;
    EditText text;
    Button search;
    BarDataSet barDataSet1,barDataSet2;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TextView t1,t2,t3,t4,t5,t6;
    LinearLayout l,l1;
    String result="",line="",main_url="http://vpray.esy.es/Graph.php",s="";
    int pu=0, ps=0, ss=0, su=0, search_= 0;
    View v1;
    int count=0;
    MainActivity main = new MainActivity();
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
        //Check network availability
        if(isNetworkAvailable()) { //If network available
            count++;
            v = inflater.inflate(R.layout.fragment_statistics, container, false);//Preparing layout
            barChart = (BarChart) v.findViewById(R.id.chart);//preparing barchart
            swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(isNetworkAvailable()){
                        v1 = getActivity().getCurrentFocus();
                        if (v1 != null) {
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v1.getWindowToken(), 0);
                        }
                        s = text.getText().toString();
                        s = s.trim().replaceAll("[\n.]+","").replaceAll("[ ]{2,}+"," ");
                        text.setText(s);
                        search_ = 2;//refresh variable
                        new loadFragment().execute();
                    }
                    else
                        Toast.makeText(getActivity(), "No Internet Available. Please check connection.", Toast.LENGTH_LONG).show();
                }
            });
            new loadFragment().execute(); //DB fetching data
            l = (LinearLayout) v.findViewById(R.id.display);
            l1 = (LinearLayout) v.findViewById(R.id.display2);
            t1 = (TextView) v.findViewById(R.id.total_potholes);
            t2 = (TextView) v.findViewById(R.id.potholes_solved);
            t3 = (TextView) v.findViewById(R.id.total_garbage);
            t4 = (TextView) v.findViewById(R.id.garbage_solved);
            t5 = (TextView) v.findViewById(R.id.no_issues);
            t6 = (TextView) v.findViewById(R.id.address);

            //displayStats();//display textual statistics

            text = (EditText) v.findViewById(R.id.editText);
            search = (Button) v.findViewById(R.id.search_button);
            search.setOnClickListener(new View.OnClickListener(){//on Search
                @Override
                public void onClick(View view) {
                    v1 = getActivity().getCurrentFocus();
                    if (v1 != null) {//Close keypad
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
                            Geocoder geocoder = new Geocoder(getActivity());
                            List<Address> addressList = null;
                            try {
                                addressList = geocoder.getFromLocationName(s, 1, 18.894423, 72.809072, 19.596876, 73.393253);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addressList != null && addressList.size() != 0) {
                                search_ = 1;//Search variable
                                new loadFragment().execute();
                            }
                            else{//Incorrect location
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
                    }
                    else
                        Toast.makeText(getActivity(),"No Internet Available. Please check connection.",Toast.LENGTH_LONG).show();

                }
            });
        }
        else{//If network not available
            v = inflater.inflate(R.layout.no, container, false);
            Drawable myIcon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_refresh);
            myIcon.setColorFilter(Color.rgb(2,119,189), PorterDuff.Mode.SRC_ATOP);
            final LinearLayout t = (LinearLayout) v.findViewById(R.id.refresh_btn);
            t.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){//On refresh click
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
                            main.count ++;
                            count++;
                            Statistics fragment = (Statistics) getFragmentManager().getFragments().get(2);
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


    class loadFragment extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
                swipeContainer.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url=new URL(main_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(s, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //it calls the post execute method with following return data
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException  | NullPointerException  | ArrayIndexOutOfBoundsException| AndroidRuntimeException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("response");
                pu = Integer.parseInt(jsonArray.getJSONObject(0).getString("pothole_Unsolved"));
                ps = Integer.parseInt(jsonArray.getJSONObject(0).getString("pothole_solved"));
                su = Integer.parseInt(jsonArray.getJSONObject(0).getString("sanitization_Unsolved"));
                ss = Integer.parseInt(jsonArray.getJSONObject(0).getString("sanitization_solved"));
                result="";
            } catch (JSONException | ArrayIndexOutOfBoundsException| AndroidRuntimeException e) {
                e.printStackTrace();
            }
            if(search_ == 0) {//Initial fetch
                barChart.setScaleEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    barChart.setDescription("");
                }
                YAxis yAxisRight = barChart.getAxisRight();
                yAxisRight.setEnabled(false);
                ArrayList<String> labels = new ArrayList<>();
                labels.add("POTHOLES");
                labels.add("GARBAGE");
                barChart.setAutoScaleMinMaxEnabled(true);
                barChart.getAxisLeft().setAxisMaxValue(((ps + pu) > (ss + su)) ? (((ps + pu) >= 10) ? (ps + pu + 5) : (10)) : (((ss + su) >= 10) ? (ss + su + 5) : (10)));
                barChart.getAxisLeft().setAxisMinValue(0);

                group1 = new ArrayList<>();
                group1.add(new BarEntry(ps + pu, 0));
                group1.add(new BarEntry(ss + su, 1));

                group2 = new ArrayList<>();
                group2.add(new BarEntry(ps, 0));
                group2.add(new BarEntry(ss, 1));

                barDataSet1 = new BarDataSet(group1, "Issues Reported");
                barDataSet1.setColor(Color.rgb(155, 0, 0));

                barDataSet2 = new BarDataSet(group2, "Issues Solved");
                barDataSet2.setColor(Color.rgb(0, 155, 0));

                barChart.getAxisLeft().setTextSize(12);
                barChart.getXAxis().setTextSize(12);

                final ArrayList<BarDataSet> dataset = new ArrayList<>();
                dataset.add(barDataSet1);
                dataset.add(barDataSet2);

                data = new BarData(labels, dataset);

                barChart.setData(data);
                barChart.animateY(3000);
            }
            else{//Search or Refresh
                displayStats();
                if(group1 !=null && group2 !=null) {
                    group1.clear();
                    group2.clear();
                }
                group1.add(new BarEntry(ps+pu, 0));
                group1.add(new BarEntry(ss+su, 1));
                group2.add(new BarEntry(ps, 0));
                group2.add(new BarEntry(ss, 1));
                barDataSet1.notifyDataSetChanged();
                barDataSet2.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                barChart.getAxisLeft().setAxisMaxValue(((ps+pu)>(ss+su))?(((ps+pu)>=10)?(ps+pu+5):(10)):(((ss+su)>=10)?(ss+su+5):(10)));
                barChart.getAxisLeft().setAxisMinValue(0);
                barChart.notifyDataSetChanged();
                data.notifyDataChanged();
                barChart.animateY(2000);
            }
            displayStats();
            swipeContainer.setRefreshing(false);
        }
    }



    public void displayStats(){//Textual statistics

        l.setVisibility(View.VISIBLE);
        if(ps+pu+ss+su>0) {
            l1.setVisibility(View.VISIBLE);
            t1.setText("" + (ps + pu));
            t2.setText("" + ps);
            t3.setText("" + (ss + su));
            t4.setText("" + ss);
            t5.setVisibility(View.GONE);
        }
        else{
            t5.setVisibility(View.VISIBLE);
            l1.setVisibility(View.GONE);
        }
        if(s.equals(""))
            t6.setText(" ( Mumbai )");
        else
            t6.setText(" ( "+s+" )");

    }


    @Override
    public void fragmentBecameVisible(){
        if(count==0)
        if(isNetworkAvailable()){
            main.count++;
            count++;
            Statistics fragment = (Statistics) getFragmentManager().getFragments().get(2);
            getFragmentManager().beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
