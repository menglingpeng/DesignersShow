package com.menglingpeng.designersshow.net;

import android.util.Log;

import com.menglingpeng.designersshow.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/16.
 */

public class HttpUtils {

    public static void get(HashMap<String, String> map, Callback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder builder = HttpUrl.parse(Constants.SHOTS).newBuilder();
        for(String key : map.keySet()){
            builder.addQueryParameter(key, map.get(key));
            Log.i(key, map.get(key));
        }
        HttpUrl httpUrl =  builder.build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

}
