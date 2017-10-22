package com.menglingpeng.designersshow.net;

import com.menglingpeng.designersshow.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/16.
 */

public class HttpUtils {

    public static void get(ArrayList<String> parametersList, Callback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder builder = HttpUrl.parse(Constants.SHOTS).newBuilder();
        for(int i=0;i<parametersList.size()-1;i++){
            builder.addQueryParameter("\" + p + \"", parametersList.get(i));
        }
        HttpUrl httpUrl =  builder.build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

}
