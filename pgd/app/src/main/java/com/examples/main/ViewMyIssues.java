package com.examples.main;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import static com.examples.main.R.id.image;
import static com.examples.main.R.id.image1;
import static com.examples.main.R.id.image2;

public class ViewMyIssues extends AppCompatActivity {
    SettingsActivity settings = new SettingsActivity();

    public TextView type1, SRN1, location1,issue_date1,completion_date1,resolution_period1,status1;
    ImageView image10,image20;
    int v;
    String image1,image2,u2;
    ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int ans = settings.sizeSetter(this);
        if(ans==1)
            getTheme().applyStyle(R.style.FontStyle_Small,true);
        else if(ans==-1)
            getTheme().applyStyle(R.style.FontStyle_Large,true);
        else
            getTheme().applyStyle(R.style.FontStyle_Medium,true);
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_my_issues);

        String type = getIntent().getExtras().getString("type");
        String SRN = getIntent().getExtras().getString("SRN");
        String location = getIntent().getExtras().getString("location");
        String issue_date = getIntent().getExtras().getString("issue_date");
        String completion_date = getIntent().getExtras().getString("completion_date");
        String resolution_period = getIntent().getExtras().getString("resolution_period");
        String status = getIntent().getExtras().getString("status");
        image1 = getIntent().getExtras().getString("image1");
        image2 = getIntent().getExtras().getString("image2");



        type1 = (TextView) findViewById(R.id.type);
        SRN1 = (TextView) findViewById(R.id.SRN);
        location1 = (TextView) findViewById(R.id.location);
        issue_date1 = (TextView) findViewById(R.id.issue_date);
        completion_date1 = (TextView) findViewById(R.id.completion_date);
        resolution_period1 = (TextView) findViewById(R.id.resolution_period);
        status1 = (TextView) findViewById(R.id.status);
        image10 = (ImageView) findViewById(R.id.image1);
        image20 = (ImageView) findViewById(R.id.image2);

        type1.setText(type);
        location1.setText(location);
        SRN1.setText(SRN);
        issue_date1.setText(issue_date);
        completion_date1.setText(completion_date);
        resolution_period1.setText(resolution_period);
        status1.setText(status);
        if (!image1.equals("")) {
            //String u = "http://vpray.esy.es/images/IMG_20170116_152326.jpg.jpeg";
            String u1="http://vpray.esy.es/"+image1+".jpeg";
                new DownloadImage(u1).execute();

        }
        if (!image2.equals("")){
            u2="http://vpray.esy.es/"+image2;

                //new DownloadImage1(u).execute();


        }
    }
    public class DownloadImage extends AsyncTask<Void,Void,Bitmap> {
        String Url;

        public DownloadImage(String Url){
            this.Url=Url;
        }
        protected void onPreExecute(){
            super.onPreExecute();
            /*progressdialog= new ProgressDialog(ViewMyIssues.this);
            progressdialog.setMessage("Downloading Image");
            progressdialog.show();*/

        }
        Bitmap image = null,image1 = null;
        @Override
        protected Bitmap doInBackground(Void... voids) {

            try {
                URL url1=new URL(Url);
                image=BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                    image = image.copy(Bitmap.Config.ARGB_8888, true);
                URL url2=new URL(u2);
                image1 =BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                    image1 = image1.copy(Bitmap.Config.ARGB_8888, true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            TextView t1 = (TextView) findViewById(R.id.download1);
            TextView t2 = (TextView) findViewById(R.id.download2);
            //TextView t3 = (TextView) findViewById(R.id.not);
            t1.setVisibility(View.GONE);
            image10.setImageBitmap(image);
            //t1.setVisibility(View.GONE);
            if (!image2.equals("")){
                //t2.setVisibility(View.GONE);
                //t3.setVisibility(View.GONE);
                resolution_period1.setText("-");
                t2.setVisibility(View.GONE);
                image20.setImageBitmap(image1);
            }
            else{
                t2.setText("Not solved yet");

            }
            //progressdialog.dismiss();

        }
    }
    public class DownloadImage1 extends AsyncTask<Void,Void,Bitmap> {
        String Url;

        public DownloadImage1(String Url){
            this.Url=Url;
        }
        protected void onPreExecute(){
            super.onPreExecute();
            progressdialog= new ProgressDialog(ViewMyIssues.this);
            progressdialog.setMessage("Downloading Image");
            progressdialog.show();
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap image = null;
            try {
                URL url=new URL(Url);
                image=BitmapFactory.decodeStream(url.openConnection().getInputStream());
                image = image.copy(Bitmap.Config.ARGB_8888, true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image10.setImageBitmap(bitmap);
            //progressdialog.dismiss();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
