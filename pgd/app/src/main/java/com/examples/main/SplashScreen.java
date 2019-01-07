package com.examples.main;

/**
 * Created by default on 04-Jan-17.
 */


        import android.app.Activity;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.Window;
        import android.view.WindowManager;

public class SplashScreen extends Activity {
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(SplashScreen.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_scree);
        marshMallowPermission.requestPermissionForExternalStorage();
        marshMallowPermission.requestPermissionForCamera();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, counter());
    }
    protected long counter()
    {
        while(!marshMallowPermission.checkPermissionForExternalStorage() && !marshMallowPermission.checkPermissionForCamera())
        {

        }
        return 3000;
    }
}
