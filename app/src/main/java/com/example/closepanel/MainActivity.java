package com.example.closepanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private WebView wb;
    private DBHelper dbHelper;
    private TimerRestart timer = null;
    private int check;
    private boolean isFinish = false;
    private SharedPreferences preferences;
    private String closePassword = "citymakon123321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        hideNavigationBar();

        preferences = getSharedPreferences("CITYMAKON",Context.MODE_PRIVATE);

        boolean homeClick = preferences.getBoolean("HOME_CLICK", false);

        Log.d("TTTT","onCreate ");
        try {
            if (homeClick)
            loadSystem();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fabButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View sasses) {
                showDialog();
            }
        });

    }


    private void loadSystem() throws MalformedURLException {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        check = 0;
        wb = findViewById(R.id.wb);
        //wb.setWebViewClient(new MyWebViewClient());
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setAllowContentAccess(true);
        wb.getSettings().setAllowFileAccess(true);
        wb.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wb.getSettings().setLoadsImagesAutomatically(true);
        wb.getSettings().setBlockNetworkImage(false);

        wb.getSettings().setBuiltInZoomControls(false);
        wb.getSettings().setSupportZoom(false);
      //  File f = new File(Environment.getExternalStorageDirectory(), "Download/musseum/index.html");
      //  wb.loadUrl("file:///android_asset/dist/index.html");


        if ( timer == null ){
            Log.d("TTTT","timer null");

            timer = new TimerRestart(this, wb);
            timer.execute();
        }

    }


    private boolean isF() {
        if (dbHelper.SQL_TEXT("select ischeck from data where id=1").getInt(0) != 0)
            return false;
        return true;
    }


    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onPause() {
        Log.d("TTTT","onPause ");
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);

        preferences.edit().putBoolean("HOME_CLICK",true).apply();

        super.onPause();
    }


    void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onBackPressed() {
        // showDialog();
        if (isFinish){
           // super.onBackPressed();
          //  preferences.edit().putBoolean("HOME_CLICK",false).apply();
             System.exit(-1);
        }
    }

    void showDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_close, null, false);
        builder.setView(view);
        AlertDialog dialog = builder.create();

       try {
           dialog.show();
       }catch (Exception e){
           e.printStackTrace();
       }

        final EditText editText = view.findViewById(R.id.edt);

        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = editText.getText().toString();

                if (!password.isEmpty() && password.equals(closePassword)) {
                    isFinish = true;
                    getPackageManager().clearPackagePreferredActivities(getPackageName());
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Password isn't correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}