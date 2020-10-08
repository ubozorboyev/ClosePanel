package com.example.closepanel;

import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;

public class TimerRestart extends AsyncTask<Void, Void, Void> {
    private Context context;
    private WebView wb;

    public TimerRestart(Context context, WebView wb) {
        this.context = context;
        this.wb = wb;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        synchronized (this) {
            while (true) {
                try {
                    publishProgress();
                    wait(90000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        LoadUrl.loadUrl(context, wb);
    }
}
