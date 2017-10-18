package com.menglingpeng.designersshow.net;

import com.menglingpeng.designersshow.utils.Constants;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/16.
 */

public class HttpUtils {

    public static void get(String token, String list, String timoframe, String date, String sort, String page, Callback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder builder = HttpUrl.parse(Constants.SHOTS).newBuilder();
        //参数list,timeframe,sort缺省状态有默认值，所以需要判定。
        if(token != null){
            builder.addQueryParameter("access_token", token);
        }
        if(list != null){
            builder.addQueryParameter("list", list);
        }
        if(timoframe != null || sort != Constants.SORT_RECENT){
            builder.addQueryParameter("timeframe", timoframe);
        }
        if(date != null){
            builder.addQueryParameter("date", date);
        }
        if(sort != null){
            builder.addQueryParameter("sort", sort);
        }
        builder.addQueryParameter("page", page);
        HttpUrl httpUrl =  builder.build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

}
