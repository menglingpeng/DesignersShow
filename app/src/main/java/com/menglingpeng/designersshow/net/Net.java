package com.menglingpeng.designersshow.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/16.
 */

public class Net {

    public static boolean isconnected(Context context){
        ConnectivityManager conManager = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
