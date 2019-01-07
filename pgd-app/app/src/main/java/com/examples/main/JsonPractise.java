package com.examples.main;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by YashJain on 12/28/2016.
 */
public class JsonPractise extends AsyncTask<Void,Void,String> {

    String data;
    public  JsonPractise(String data){

        this.data=data;
    }
    @Override
    protected String doInBackground(Void... voids) {
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
            String post_data = URLEncoder.encode("encoded_string","UTF-8")+"="+URLEncoder.encode(data,"UTF-8");
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        any.l=result;
    }
}
