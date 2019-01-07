package com.examples.main;


import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.AndroidRuntimeException;
import android.util.Log;

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

/**
 * Created by YashJain on 12/18/2016.
 */

//starting of asynctask for retriving the data
public class RetrivingData extends AsyncTask<Void,Void,String> {      //contains:<first=input to the class,second=dont know,third=returning type of doInBackground>
    String jason_string,result="",line="";
    String main_url;
    AlertDialog alertDialog;
    String operation,search;
    //StoreJsonFetchData [] array;
    RetrivingData(String operation,String search1){
        this.operation=operation;
        this.search=search1;
    }

    @Override
    protected void onPreExecute() {
        if(operation.equals("map"))
        main_url="http://vpray.esy.es/MyWholeDatabase.php";//setting the url to make http connectiona with php file for performing the databse functions
        else if (operation.equals("graph"))
            main_url="http://vpray.esy.es/Graph.php";
        else if(operation.equals("searchBoundry"))
            main_url="http://vpray.esy.es/searchmap.php";
        else if(operation.equals("getEntry"))
            main_url="http://vpray.esy.es/notify.php";

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url=new URL(main_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8");
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
            Log.e("connection &  retrive","succesfull");
            //it calls the post execute method with following return data
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException  |NullPointerException  |ArrayIndexOutOfBoundsException|AndroidRuntimeException e) {
            e.printStackTrace();
        }
        return result;

    }



    protected  void onPostExecute(String result) {
        jason_string = result;
    }
//End of async task
}
