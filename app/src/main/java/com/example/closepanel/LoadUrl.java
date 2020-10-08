package com.example.closepanel;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebView;

/**
 * Created by ABBOSBEK on 25.02.2017.
 */

public class LoadUrl {
    public static String url = "http://e.citynet.uz/food/1";
    public static void loadUrl(Context context, WebView wb) {
            wb.loadUrl(url);
    }
}
