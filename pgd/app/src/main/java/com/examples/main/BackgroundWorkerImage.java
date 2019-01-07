package com.examples.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by YashJain on 12/23/2016.
 */
public class BackgroundWorkerImage extends AsyncTask<Void,Void,String> {
    String Operation,Name;
    Bitmap Image;
    String Srn;
    public BackgroundWorkerImage(String Operation, Bitmap Image, String Name,String Srn){
        this.Operation=Operation;
        this.Name=Name;
        this.Image=Image;
        this.Srn=Srn;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String login_url = "http://vpray.esy.es/upload.php";
        String result="";
        String line;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream) ;
        String encodedImage= Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
        try {
        URL url=new URL(login_url);
        HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        OutputStream outputStream=httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
        String post_data = URLEncoder.encode("encoded_string","UTF-8")+"="+URLEncoder.encode(encodedImage,"UTF-8")+"&"
                +URLEncoder.encode("image_name","UTF-8")+"="+URLEncoder.encode(Name,"UTF-8")+"&"
                +URLEncoder.encode("Srn","UTF-8")+"="+URLEncoder.encode(Srn,"UTF-8")+"&"
                +URLEncoder.encode("Operation","UTF-8")+"="+URLEncoder.encode(Operation,"UTF-8");
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

    } catch (Exception e) {
        e.printStackTrace();
    }
        return result;
    }
    protected void onPostExecute(String result) {
        Log.e("Insert or Update Img",result);

    }
}
