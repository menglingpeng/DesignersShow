package com.menglingpeng.designersshow.net;

import android.util.Log;

import com.menglingpeng.designersshow.mvp.model.Comments;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/16.
 */

public class

HttpUtils {

    public static String getJson(HashMap<String, String> map, String requestType) {
        String json = null;
        HttpUrl httpUrl = null;
        HttpUrl.Builder urlBuilder = null;
        Request request = null;
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        RequestBody requestBody = null;
        switch (requestType) {
            case Constants.REQUEST_COMMENTS:
                String url = new StringBuilder().append(Constants.SHOTS_URL).append("/").append(map.get(Constants.SHOTS))
                        .append("/").append(requestType).toString();
                urlBuilder = HttpUrl.parse(url).newBuilder();
                httpUrl = urlBuilder.build();
                break;
            case Constants.REQUEST_AUTH_TOKEN:
                for (String key : map.keySet()){
                   bodyBuilder.add(key, map.get(key)) ;
                }
                requestBody = bodyBuilder.build();
                break;
            case Constants.REQUEST_AUTH_USER:
                urlBuilder = HttpUrl.parse(Constants.SHOTS_URL).newBuilder();
                for (String key : map.keySet()) {
                    urlBuilder.addQueryParameter(key, map.get(key));
                }
                httpUrl = urlBuilder.build();
                break;
            default:
                urlBuilder = HttpUrl.parse(Constants.SHOTS_URL).newBuilder();
                for (String key : map.keySet()) {
                    urlBuilder.addQueryParameter(key, map.get(key));
                }
                httpUrl = urlBuilder.build();
                break;
        }
        if (requestType.equals(Constants.REQUEST_AUTH_TOKEN)){
            request = new Request.Builder()
                    .url(Constants.REQUEST_AUTH_TOKEN_URL)
                    .post(requestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(httpUrl)
                    .get()
                    .build();
        }
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
