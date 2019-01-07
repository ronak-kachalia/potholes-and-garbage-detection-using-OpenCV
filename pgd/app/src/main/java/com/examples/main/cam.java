package com.examples.main;
import org.opencv.android.OpenCVLoader;
import org.opencv.features2d.FeatureDetector;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.video.*;
import org.opencv.video.BackgroundSubtractorMOG2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Criteria;
import android.media.MediaActionSound;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;


import static android.R.attr.negativeButtonText;
import static android.R.attr.resource;
import static android.R.attr.src;
import static android.R.attr.windowTitleSize;
import static android.content.Context.SENSOR_SERVICE;
import static com.examples.main.any.j;
import static org.opencv.core.Core.KMEANS_PP_CENTERS;
import static org.opencv.core.Core.KMEANS_USE_INITIAL_LABELS;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.bitwise_and;
import static org.opencv.core.Core.bitwise_not;
import static org.opencv.core.Core.bitwise_or;
import static org.opencv.core.Core.bitwise_xor;
import static org.opencv.core.Core.kmeans;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.core.Mat.zeros;
import static org.opencv.features2d.Features2d.DRAW_RICH_KEYPOINTS;
import static org.opencv.features2d.Features2d.drawKeypoints;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.MORPH_CROSS;
import static org.opencv.imgproc.Imgproc.MORPH_ELLIPSE;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.adaptiveThreshold;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.convexHull;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.floodFill;
import static org.opencv.imgproc.Imgproc.getStructuringElement;
import static org.opencv.imgproc.Imgproc.isContourConvex;
import static org.opencv.imgproc.Imgproc.moments;
import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.resize;
import static org.opencv.imgproc.Imgproc.threshold;
import static org.opencv.photo.Photo.fastNlMeansDenoisingColored;
import static org.opencv.video.Video.createBackgroundSubtractorMOG2;
import static org.opencv.video.Video.estimateRigidTransform;


public class cam extends Fragment implements FragmentInterface,SensorEventListener,LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private  static final String TAG ="MainActivity";
    Bitmap fimage=null;
    File pictureFile=null;
    byte b[];
    static {
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "opencv loaded ");

        }
        else {
            Log.d(TAG, "opencvnotloaded: ");
        }}
    private FrameLayout ll;
    private FragmentActivity fa;
    MainActivity main = new MainActivity();
    int count=0;
    UserDbHelper db;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private Camera mCamera;
    private CameraPreview mPreview;
    public static double lat = 0.0;
    public static double longt = 0.0;
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    int angle;
    float[] mGravity=new float[3];
    float[] mGeomagnetic;
    final double radtodeg=57.3248408;
    final String deg="\u00b0";
    boolean gps;
    boolean locationsetting = false;
    TextView sv;
    EditText IO;
    Button Saver;
    Button Retaker;
    Button Flashbtn;
    Button Capturer;
    TextView Border;
    RelativeLayout angle_show;
    FrameLayout preview;
    static String pathOfImage;
    static String path;
    static String strFileName;
    double mScreenheight;
    boolean oAllow=false;
    int angle22;
    Integer i;
    double m;
    //TextView longtlat;
    TextView ang2;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog.Builder messageDialog;
    public static int flashcount = 0;
    String message="Upload now";
    String x="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
        fa = super.getActivity();
        ll = (FrameLayout) inflater.inflate(R.layout.activity_camera_main, container, false);
        sv=(TextView) ll.findViewById(R.id.angles);
        IO=(EditText) ll.findViewById(R.id.input);
        Saver = (Button) ll.findViewById(R.id.save);
        Retaker = (Button) ll.findViewById(R.id.Retake);
        Flashbtn = (Button) ll.findViewById(R.id.Flash);
        Capturer = (Button) ll.findViewById(R.id.button_capture);
        preview = (FrameLayout) ll.findViewById(R.id.preview);
        Border = (TextView) ll.findViewById(R.id.borders);
        angle_show = (RelativeLayout) ll.findViewById(R.id.angle_show);
        ang2=(TextView) ll.findViewById(R.id.angle2) ;
        alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        messageDialog = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setCancelable(false);
        retkr(0);
        googleapiclient();
        locationrequester();
        locationsetting=false;
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        capturelistner();
        mScreenheight=760*0.000265;
        return ll;
    }





    //INTERNET CHECKER
////////////////////////////
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    ///////////////////////


    //CAMERA BASICS----------
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }



    void capturelistner()
    {
        final Camera.PictureCallback mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mPreview.autoFocusDisable(true);
                captr();
                pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null) {
                    Log.d("", "Error creating media file, check storage permissions: ");
                    return;
                }
                Border.setVisibility(View.INVISIBLE);
                sv.setVisibility(View.INVISIBLE);
                ang2.setVisibility(View.INVISIBLE);
                b = data.clone();
                final Bitmap bp = BitmapFactory.decodeByteArray(b, 0, b.length);
                fimage=bp;
                Saver.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {

//                                    FileOutputStream fos = new FileOutputStream(pictureFile);
//                                    fos.write(b);
//                                    fos.close();
                                    Saver.setClickable(false);
                                    Retaker.setClickable(false);
//
                                    detect d = new detect();
                                    m=0;
                                    m = d.detection(bp);

                                    if (m == 3 || m == 4) {
                                        if (m == 4) {
                                            messageDialog.setMessage("Please take a valid and Clear Picture of the problem!\n\n" +
                                                    "1.You may try clicking picture from different side.\n\n" +
                                                    "2. You may try to take closer pic if possible.\n");

                                        } else if (m == 3) {
                                            messageDialog.setMessage("Invalid image....\n\n\nPlease take PICTURE of valid POTHOLE or GARBAGE only");

                                        }
                                        AlertDialog alertDialog = messageDialog.create();
                                        alertDialog.show();
                                        functionForRetake();
                                    } else {
                                        alertDialogBuilder.setMessage("");
                                        if (!isNetworkAvailable()) {
                                            message = "Save it Offline";
                                            x = "Picture will be uploaded on Internet Connection";
                                        } else {
                                            message = "Upload now";
                                            x = "";
                                        }

                                        if (m == 1) {
                                            alertDialogBuilder.setMessage("Pothole detected...is this correct?\n\n" + x);

                                        } else if (m == 2) {
                                            alertDialogBuilder.setMessage("Garbage detected...is this correct?\n\n" + x);

                                        } else if (m == 5) {
                                            alertDialogBuilder.setMessage("No Issue Detected... is this correct?\n\n" + x);

                                        }

                                        alertDialogBuilder.setPositiveButton(message, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                myFuctionForDataSending(m);
                                                functionForRetake();

                                            }
                                        });
                                        alertDialogBuilder.setNegativeButton("Retake", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                functionForRetake();

                                            }
                                        });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }


                                }
//                                catch (FileNotFoundException e) {
//                                    Log.d("", "File not found: " + e.getMessage());
                                catch (Exception e) {
                                    Log.d("", "Error : " + e.getMessage());
                                }
                            }
                        }
                );
                Retaker.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                functionForRetake();
                            }
                        }
                );
            }

        };
        Flashbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flashcount++;
                        Button flash = (Button) ll.findViewById(R.id.Flash);
                        Camera.Parameters params = mCamera.getParameters();
                        if(params.getFlashMode() != null)
                        {
                            if (flashcount % 2 == 1) {
                                params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                                flash.setBackgroundResource(R.drawable.flashon);
                            } else {
                                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                flash.setBackgroundResource(R.drawable.flashoff);
                            }
                            mCamera.setParameters(params);
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Flash mode not supported",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Capturer.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {

                        if((lat>=0 && longt>=0)) {
                            if ((angle>00 && angle<90) || (angle22>20 && angle22<60)) {
                                if(lat<0 && longt==0)
                                {
                                    Toast toast = Toast.makeText(getActivity(),"Wait for GPS co-ordinates!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                                else{
                                    //Capturer.setClickable(false);
                                    Capturer.setEnabled(false);
                                    MediaActionSound sound = new MediaActionSound();
                                    sound.play(MediaActionSound.SHUTTER_CLICK);
                                    mCamera.takePicture(null, null, mPicture);
                                }
                            }
                            else
                            {
                                Toast toast = Toast.makeText(getActivity(),"Click picture between 20"+deg+" to 60"+deg, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }

                        }
                        else {
                            if(!locationchecker())
                            {Toast toast = Toast.makeText(getActivity(),"Turn ON location!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();}
                        }

                    }
                }
        );
    }
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    public void functionForRetake()
    {
        Saver.setClickable(false);
        Retaker.setClickable(false);
        sv.setVisibility(View.VISIBLE);
        ang2.setVisibility(View.VISIBLE);
        mPreview.autoFocusDisable(false);
        retkr(1);
    }
    public void AlertforInternet(){
        Calendar c = Calendar.getInstance();
        int hr=c.getTime().getHours();
        int min=c.getTime().getMinutes();
        int sec=c.getTime().getSeconds();
        c.set(Calendar.HOUR_OF_DAY,hr);
        c.set(Calendar.MINUTE,min);
        c.set(Calendar.SECOND,sec);
        Intent alertIntent = new Intent(getActivity(), AlertforInternet.class);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis()+60*1000,3*60*1000,PendingIntent.getBroadcast(getActivity(),30,alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
    public void myFuctionForDataSending(double m){
        EditText des = (EditText) ll.findViewById(R.id.input);
        Double diii = m;
        i = diii.intValue();

        if (isNetworkAvailable()) {
            j=1;
            new BackgroundWorker(getActivity(), "insertInformation", "", lat, longt, i, des.getText().toString(),this.fimage, strFileName,pathOfImage).execute();
        } else {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(pictureFile);
                fos.write(b);
                fos.close();
            } catch (Exception e) {
                Toast toast = Toast.makeText(getActivity(),"File not found!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            String date = sdf.format(c.getTime());
            db = new UserDbHelper(getActivity());
            db.insertinformation("", "insertInformation", pathOfImage, date, lat, longt, i, des.getText().toString(), strFileName);
            Cursor res=db.CheckStatusforEntry();
            if (res.getCount() == 1){
                Log.e("Alert","Started for internet");
                AlertforInternet();
            }
        }

    }
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            preview.removeView(mPreview);
            mCamera.release();
            mCamera = null;
            releaseCamera();
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
    void captr() {
        IO.setVisibility(View.VISIBLE);
        Saver.setVisibility(View.VISIBLE);
        Retaker.setVisibility(View.VISIBLE);
        Flashbtn.setVisibility(View.GONE);
        Capturer.setVisibility(View.GONE);
    }

    void retkr(int i) {
        Capturer.setEnabled(true);
        Saver.setClickable(true);
        Retaker.setClickable(true);
        Border.setVisibility(View.VISIBLE);
        ang2.setVisibility(View.VISIBLE);
        // Capturer.setClickable(true);
        IO.setVisibility(View.GONE);
        Saver.setVisibility(View.GONE);
        Retaker.setVisibility(View.GONE);
        sv.setVisibility(View.VISIBLE);
        Flashbtn.setVisibility(View.VISIBLE);
        Capturer.setVisibility(View.VISIBLE);
        if (i == 1)
            mCamera.startPreview();
    }
    Context context;
    @Override
    public void onResume() {
        super.onResume();
        //permission
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
        //  marshMallowPermission.requestPermissionForCamera();
        //angle sensor
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        if (marshMallowPermission.checkPermissionForExternalStorage() && marshMallowPermission.checkPermissionForCamera() && checkCameraHardware(getActivity())) {
            if (mCamera == null) {
                mCamera = getCameraInstance();
                mPreview = new CameraPreview(getActivity(), mCamera);
                preview.addView(mPreview, 0);

            }
        }
    }
    //CAMERA BASICS ENDS----------


    //LOCATION----------
    void googleapiclient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
    void locationrequester()
    {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates s = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(getActivity(),0x1);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        System.exit(0);
                        break;
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 0x1:
                switch (resultCode) {

                    case -1:
                        locationsetting=true;
                        onCreateView(null,null,null);
                        break;
                    default: System.exit(0);
                }

                break;
        }
    }

    public boolean locationchecker()
    {
        LocationManager lm = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        try {
            gps= lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        return gps;
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationRequest locationRequest;
        locationRequest = mLocationRequest.setInterval(10);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("", "Connection Failed");
    }

    @Override
    public void onLocationChanged(Location l) {
        //longtlat = (TextView) ll.findViewById(R.id.latlongt);
        lat = l.getLatitude();
        longt = l.getLongitude();
        //longtlat.setText("latt:"+lat+"longt:"+longt);
    }
    //LOCATION ENDS----------


    //SAVE FILE----------
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp +".jpg");
            File file = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp +".jpg");
            strFileName = file.getName();
            path=mediaStorageDir.getPath();
            pathOfImage = mediaStorageDir.getPath() + "/" + strFileName;

        } else {
            return null;
        }
        return mediaFile;
    }
    //SAVE FILE ENDS----------
    double norm_Of_g=0;
    //SENSOR----------
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success)
            {
                //int screenOrient = getResources().getConfiguration().orientation;
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                //if((orientation[2]<=0 && (orientation[2]*radtodeg)>=(-90)) && orientation[1]<=0 )
                //{
                angle = (int) Math.abs(orientation[1] * radtodeg);
                angle22 = (int) Math.abs(orientation[2] * radtodeg);
                //}
                ang2.setText("Horizontal:"+angle22+deg);
                sv.setText("Vertical:"+angle+deg);
            }
        }
        else {
            double norm_Of_g = Math.sqrt(mGravity[0] * mGravity[0] + mGravity[1] * mGravity[1] + mGravity[2] * mGravity[2]);
            if (mGravity[2] > 0) {
                mGravity[2] = mGravity[2] / (float) norm_Of_g;
                angle = (int)(90- (Math.asin((mGravity[2]) / (10*mScreenheight)) * radtodeg * 3));
                sv.setText("Vertical:"+angle+deg);
                ang2.setText("Horizontal:"+angle+deg);

            }
        }
        if ((angle > 20 && angle < 60) || (angle22>20 && angle22<60))
            Border.setBackgroundResource(R.drawable.border1);
        else
            Border.setBackgroundResource(R.drawable.border);
    }    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    //SENSOR ENDS----------
    @Override
    public void fragmentBecameVisible(){

    }

}







//PREVIEW
/////////////////////////////////////////////////
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    cam c = new cam();
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;
    boolean previewScreen=false;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    void autoFocusDisable(boolean ans)
    {
        previewScreen=ans;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> previewSize = params.getSupportedPreviewSizes();
            params.setPictureSize(1280, 720);
            params.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            mCamera.setParameters(params);

            setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (mCamera != null) {

                        mCamera.cancelAutoFocus();
                        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>(1);
                        focusAreas.add(new Camera.Area(new Rect(-1000, -1000, 1000, 0), 750));

                        Camera.Parameters parameters = mCamera.getParameters();
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
                        if (parameters.getMaxNumFocusAreas() > 0) {
                            parameters.setFocusAreas(focusAreas);
                        }

                        mCamera.setParameters(parameters);
                        if(previewScreen==false)
                            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                                @Override
                                public void onAutoFocus(boolean success, Camera camera) {
                                    camera.cancelAutoFocus();
                                    Camera.Parameters params = camera.getParameters();
                                    if(params.getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE){
                                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                                        camera.setParameters(params);
                                    }
                                }
                            });
                    }
                    return true;
                }
            });
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("", "Error setting camera preview: " + e.getMessage());
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }

        float ratio;
        if (mPreviewSize.height >= mPreviewSize.width)
            ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
        else
            ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;
        setMeasuredDimension(width, (int) (width * ratio));
//
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null)
            c.releaseCamera();
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null){
            c.releaseCamera();
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e){
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e){
            Log.d("", "Error starting camera preview: " + e.getMessage());
        }
    }
}
//////////////////////////////////////////////////////////////



//PERMISSIONS
////////////////////////////////////////////////////////
class MarshMallowPermission {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
}
////////////////////////////////////////////////////////



//DETECTION CODg
//////////////////////////////////////////////////////



class detect {

    static   int lowerbounds[]=new int[4], answer=0,width=500,height=400, loops, changedPixels,red,green,blue,p1;
    public static Bitmap image_temp , image,image1,image2,image3,image4;
    static boolean not_terminated;
    static ArrayList<ClusterClass> classes;
    static double hist[] = new double[256], score=0,pg=0;



    public static int detection(Bitmap img1)
    {
        img1 = img1.copy(Bitmap.Config.ARGB_8888, true);
        Mat img = new Mat();
        Utils.bitmapToMat(img1, img);
        //resize to produce the shrinking
        resize(img,img, new Size(width,height));
        //grey scale conversion + gussian blur to remove noise
        cvtColor(img, img,COLOR_RGB2GRAY);
        Imgproc.medianBlur(img, img, 5);
        Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);


        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image1=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image2=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image3=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image4=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image1);
        canvas.drawColor(Color.BLACK);
        canvas=new Canvas(image2);
        canvas.drawColor(Color.BLACK);
        canvas=new Canvas(image3);
        canvas.drawColor(Color.BLACK);
        canvas=new Canvas(image4);
        canvas.drawColor(Color.BLACK);

        Utils.matToBitmap(img, image);

        //kmean to original image
        int []srcData =new int[height*width],ogsrc=new int[height*width];
        image.getPixels(srcData, 0, width, 0, 0, width, height);
        int ts=height*width;
        for (int i=0;i<ts;i++) {
            ogsrc[i]= srcData[i];
            srcData[i] = (int) (Color.red(srcData[i]) * 0.2116 + Color.blue(srcData[i]) * 0.0714 + Color.green(srcData[i]) * 0.72);
            hist[srcData[i]]++;
        }
        initialize(image,4);
        calculateBounds();
        while (not_terminated)
        {
            recalculateMeans();
            loops++;
            checkTermination();
        }

        Imgproc.Canny(img,img ,lowerbounds[3], lowerbounds[2]);
        Mat edges = new Mat(img.size(), CvType.CV_8UC1),im1= new Mat(img.size(), CvType.CV_8UC1),im2=new Mat(img.size(), CvType.CV_8UC1);
        Bitmap edge = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, edge);
        image=processImage(4);

        //finding contours in image

        List<MatOfPoint> contours = new ArrayList<>(), contours1 = new ArrayList<>(),contours2 = new ArrayList<>(),contours3 = new ArrayList<>(),contours4 = new ArrayList<>();
        Mat hierarchy = new Mat(), hierarchy1 = new Mat(),hierarchy2 = new Mat(),hierarchy3 = new Mat(),hierarchy4 = new Mat();
        Utils.bitmapToMat(edge, edges);
        dilate(edges,edges,Imgproc.getStructuringElement(MORPH_ELLIPSE,new Size(3,3),new Point(0,0)));

        Utils.bitmapToMat(image1, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.findContours(img, contours1, hierarchy1, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        Utils.bitmapToMat(image2, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.findContours(img, contours2, hierarchy2, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        Utils.bitmapToMat(image3, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.findContours(img, contours3, hierarchy3, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        Utils.bitmapToMat(image4, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.findContours(img, contours4, hierarchy4, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));




        int s1=contours1.size();
        double a=0,totalArea=0,score1=0,score2=0,score3=0,score4=0,score=0,tc=0,gb=0,totalEdge=0;
        Imgproc.cvtColor(edges,edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        int open=0,close=0,s=contours.size();

        for( int i = 0; i< s; i++) // iterate through each contour.
        {
            a = contourArea(contours.get(i)) / 1000;
            if (hierarchy.get(0, i)[2] > -1)
            {
                int  g=(int)hierarchy.get(0, i)[2];

                if (hierarchy.get(0, (int) hierarchy.get(0, i)[2])[2] ==-1) {
                    close++;
                    totalEdge+=a;
                }

            }
            else
                open++;
        }

        for( int i = 0; i< s1; i++ ) {
            a = contourArea(contours1.get(i)) / 100;
            if (hierarchy1.get(0, i)[2] > -1)
            {
                if (hierarchy1.get(0, (int) hierarchy1.get(0, i)[2])[2] == -1) {
                    score1++;
                    totalArea += a;
                }
            }
            else
            {
                if(a>1&&a<25)
                    gb+=a;
            }
        }

       int  s2=contours2.size();
        for( int i = 0; i< s2; i++ ) {
            a = contourArea(contours2.get(i)) / 100;
            if (hierarchy2.get(0, i)[2] > -1)
            {
                if (hierarchy2.get(0, (int) hierarchy2.get(0, i)[2])[2] == -1) {
                    score2++;
                    totalArea += a;
                }
            }
            else
            {
                if(a>1&&a<25)
                    gb+=a;
            }
        }


        int  s3=contours3.size();
        for( int i = 0; i< s3; i++ ) {
            a = contourArea(contours3.get(i)) / 100;
            if (hierarchy3.get(0, i)[2] > -1)
            {
                if (hierarchy3.get(0, (int) hierarchy3.get(0, i)[2])[2] == -1) {
                    score3++;
                    totalArea += a;
                }
            }
            else
            {
                if(a>1&&a<25)
                    gb+=a;
            }
        }

        int  s4=contours4.size();

        for( int i = 0; i< s4; i++ ) {
            a = contourArea(contours4.get(i)) / 100;
            if (hierarchy4.get(0, i)[2] > -1)
            {
                if (hierarchy4.get(0, (int) hierarchy4.get(0, i)[2])[2] == -1) {
                    score4++;
                    totalArea += a;

                }
            }
            else
            {
                if(a>1&&a<25)
                    gb+=a;
            }
        }


        tc=s1+s2+s3+s4;
        score=score1+score2+score3+score4;

        if(totalEdge>15&&(tc>gb)&&open>150&&close>25&&s>150&&(score1+score2>25)&&(score3+score4>9||gb>500||open>250))
        answer=2;

        else if((tc+gb)>200&&totalEdge<10&&totalEdge>1&&open>20&&open<200&&close<50&&score1<25&&score2<10&&score3>0&&score2>0 &&(close>20||tc>2*gb))
            answer=1;
        else if(score1<8&&score2<8&&score3<8&&score4<8&&open<20&&close<10&&totalEdge<3)
            answer=5;
        else
            answer=3;

       image_temp.recycle();
       image.recycle();
       image1.recycle();
       image2.recycle();
       image3.recycle();
       image4.recycle();
       img1.recycle();
       edge.recycle();

        return (int)answer;
    }



    private static Bitmap processImage( int bins) {



        int delta = 255 / (bins-1);
        for (int h = 0,w; h < height; h++){
            for ( w = 0; w < width; w++){
                p1 = image_temp.getPixel(w, h);
                 green =Color.green(p1);
                for (int i = 0,g,p=Color.rgb(0,255,0); i<classes.size(); i++) {
                    if (green > classes.get(i).lowerbound && green< classes.get(i).upperbound){
                        g = i*delta;
                        g=Color.rgb(g,g,g);
                        if(i==1)
                            image1.setPixel(w,h,g);
                        else  if(i==2)
                            image2.setPixel(w,h,g);
                        else if(i==3)
                            image3.setPixel(w,h,g);
                        else
                        image4.setPixel(w,h,p);
                    }
                }
            }}
        return image_temp;
    }
    private static void  initialize(Bitmap image, int bins){
        image_temp = image;
        image_temp = image_temp.copy(Bitmap.Config.ARGB_8888, true);
        loops =changedPixels= 0;
        not_terminated = true;
        classes = new ArrayList<>();
        for (int i = 0; i < bins; i++) {
            ClusterClass cc = new ClusterClass(createMean(bins, i));
            classes.add(cc);}
    }
    private static void calculateBounds() {
        for (int i = 0; i < classes.size(); i++){
            int lb = calculateLowerBound(classes.get(i));
            lowerbounds[i] = lb;
            classes.get(i).setBounds(lb,calculateUpperBound(classes.get(i)) );}
    }
    private static int calculateLowerBound(ClusterClass cc) {
        int cMean = cc.getMean();
        int currentBound = 0;
        for (int i = 0; i< classes.size(); i++) {
            if (cMean > classes.get(i).getMean()) {
                currentBound = Math.max((cMean + classes.get(i).getMean())/2, currentBound);}                       }
        return currentBound;
    }
    private static int calculateUpperBound(ClusterClass cc) {
        int cMean = cc.getMean();
        int currentBound = 255;
        for (int i = 0; i< classes.size(); i++) {
            if (cMean < classes.get(i).getMean()) {
                currentBound = Math.min((cMean + classes.get(i).getMean())/2, currentBound);}                   }
        return currentBound;
    }
    private static void recalculateMeans() {
        for (int i = 0; i<classes.size(); i++)
            classes.get(i).calculateMean(hist);
        calculateChangedPixels();
    }
    private static void checkTermination() {
        if (loops >= 50)
            not_terminated = false;
        if (changedPixels <= 300)
            not_terminated = false;
    }
    private static void calculateChangedPixels() {
        int changed = 0;
        for (int i = 0; i<classes.size(); i++) {
            int c = calculateLowerBound(classes.get(i));
            if (c < lowerbounds[i]) {
                for (int j = c; j<lowerbounds[i]; j++)
                    changed += hist[j];}
            if (c > lowerbounds[i]) {
                for (int j = lowerbounds[i]; j<c; j++)
                    changed+=hist[j];
            }  }
        changedPixels = changed;
        calculateBounds();
    }
    private static int createMean(int bins, int index) {
        return (int)(255 / (bins-1) * index);
    }
    public  static class ClusterClass {
        int mean, upperbound, lowerbound;
        public ClusterClass(int m) {
            mean = m;
        }
        public  void setBounds(int lb, int ub) {
            lowerbound = lb;
            upperbound = ub;
        }
        public  int getMean() {
            return mean;
        }
        public  void calculateMean(double [] histogram) {
            int tempMean = 0;
            int counter = 0;
            for (int i = lowerbound; i<= upperbound; i++) {
                counter += (int)histogram[i];
                tempMean += (int)histogram[i] * i;
            }
            try{
                mean = tempMean / counter;
            }
            catch(ArithmeticException e){}
        }
    }
}




