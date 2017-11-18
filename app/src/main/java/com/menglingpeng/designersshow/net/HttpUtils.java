package com.menglingpeng.designersshow.net;

import android.util.Log;

import com.menglingpeng.designersshow.mvp.model.Comments;
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

public class

HttpUtils {

    public static String getJson(HashMap<String, String> map, String requestType){
        String json = null;
        HttpUrl httpUrl = null;
        OkHttpClient client = new OkHttpClient();
        if(requestType != Constants.REQUEST_COMMENTS) {
            HttpUrl.Builder builder = HttpUrl.parse(Constants.SHOTS_URL).newBuilder();
            for (String key : map.keySet()) {
                builder.addQueryParameter(key, map.get(key));
                //Log.i(key, map.get(key));
            }
            httpUrl = builder.build();
        }else {
            String url = new StringBuilder().append(Constants.SHOTS_URL).append("/").append(map.get(requestType))
                    .append("/").append(requestType).toString();
            httpUrl = HttpUrl.parse(url);
        }
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            json = null;
        }
        return json;
    }
    
}
