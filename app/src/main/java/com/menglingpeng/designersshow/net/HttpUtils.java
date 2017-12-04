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

    public static String getJson(HashMap<String, String> map, String type, String requestType, String requestMethod) {
        String json = null;
        Request request = null;
        HttpUrl httpUrl = null;
        String url = null;
        HttpUrl.Builder urlBuilder = null;
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        RequestBody requestBody = null;
        switch (requestMethod){
            case Constants.REQUEST_GET_MEIHOD:
                switch (type) {
                    case Constants.REQUEST_AUTH_USER:
                        urlBuilder = HttpUrl.parse(Constants.AUTHENTICATED_USER_URL).newBuilder();
                        break;
                    case Constants.REQUEST_SINGLE_USER:
                        url = new StringBuilder().append(Constants.SINGLE_USER_URL).append("/").append(map.get(Constants.ID)).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.TAB_FOLLOWING:
                        urlBuilder = HttpUrl.parse(Constants.LIST_SHOTS_FOR_USERS_FOLLEOED_BY_A_USER_URL).newBuilder();
                        break;
                    case Constants.MENU_MY_LIKES:
                        urlBuilder = HttpUrl.parse(Constants.LIST_SHOTS_FOR_AUTH_USER_LIKES_URL).newBuilder();
                        break;
                    case Constants.MENU_MY_SHOTS:
                        urlBuilder = HttpUrl.parse(Constants.LIST_SHOTS_FOR_AUTH_USER_URL).newBuilder();
                        break;
                    case Constants.MENU_MY_BUCKETS:
                        urlBuilder = HttpUrl.parse(Constants.LIST_BUCKETS_FOR_AUTH_USER_URL).newBuilder();
                        break;
                    case Constants.REQUEST_CHOOSE_BUCKET:
                        urlBuilder = HttpUrl.parse(Constants.LIST_BUCKETS_FOR_AUTH_USER_URL).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_SHOTS_FOR_A_BUCKET:
                        url = new StringBuilder().append(Constants.BUCKETS_URL).append("/").append(map.get(Constants.BUCKET_ID))
                                .append("/").append(Constants.SHOTS).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_CHECK_IF_LIKE_SHOT:
                        url = new StringBuilder().append(Constants.SHOTS_URL).append("/").append(map.get(Constants.ID))
                                .append("/").append(Constants.LIKE).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_COMMENTS:
                        url = new StringBuilder().append(Constants.SHOTS_URL).append("/").append(map.get(Constants.SHOT_ID))
                                .append("/").append(Constants.COMMENTS).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER:
                        urlBuilder = HttpUrl.parse(Constants.AUTHENTICATED_USER_URL).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_SHOTS_FOR_AUTH_USER:
                        url = new StringBuilder().append(Constants.AUTHENTICATED_USER_URL).append("/").append(Constants.SHOTS).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_FOR_AUTH_USER:
                        url = new StringBuilder().append(Constants.AUTHENTICATED_USER_URL).append("/").append(Constants.FOLLOWERS).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_FOLLOWING_FOR_AUTH_USER:
                        url = new StringBuilder().append(Constants.AUTHENTICATED_USER_URL).append("/").append(Constants.FOLLOWING).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_DETAIL_FOR_A_USER:
                        url = new StringBuilder().append(Constants.SINGLE_USER_URL).append("/").append(map.get(Constants.ID)).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_SHOTS_FOR_A_USER:
                        url = new StringBuilder().append(Constants.SINGLE_USER_URL).append("/").append(map.get(Constants.ID)).append("/").append(Constants.SHOTS).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_FOR_A_USER:
                        url = new StringBuilder().append(Constants.SINGLE_USER_URL).append(map.get(Constants.ID)).append("/").append(Constants.FOLLOWERS).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    case Constants.REQUEST_LIST_FOLLOWING_FOR_A_USER:
                        url = new StringBuilder().append(Constants.SINGLE_USER_URL).append(map.get(Constants.ID)).append("/").append(Constants.FOLLOWING).toString();
                        urlBuilder = HttpUrl.parse(url).newBuilder();
                        break;
                    default:
                        urlBuilder = HttpUrl.parse(Constants.SHOTS_URL).newBuilder();
                        break;
                }
                switch (type){
                    case Constants.REQUEST_CHECK_IF_LIKE_SHOT:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
                        break;
                    case Constants.REQUEST_LIST_SHOTS_FOR_A_BUCKET:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, map.get(Constants.ACCESS_TOKEN));
                        urlBuilder.addEncodedQueryParameter(Constants.PAGE, map.get(Constants.PAGE));
                        break;
                    case Constants.REQUEST_LIST_COMMENTS:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, map.get(Constants.ACCESS_TOKEN));
                        break;
                    case Constants.REQUEST_SINGLE_USER:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, map.get(Constants.ACCESS_TOKEN));
                        break;
                    case Constants.REQUEST_LIST_DETAIL_FOR_A_USER:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, map.get(Constants.ACCESS_TOKEN));
                        break;
                    case Constants.REQUEST_LIST_SHOTS_FOR_A_USER:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, map.get(Constants.ACCESS_TOKEN));
                        urlBuilder.addEncodedQueryParameter(Constants.SORT, map.get(Constants.SORT));
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_FOR_A_USER:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, map.get(Constants.ACCESS_TOKEN));
                        break;
                    case Constants.REQUEST_LIST_FOLLOWING_FOR_A_USER:
                        urlBuilder.addEncodedQueryParameter(Constants.ACCESS_TOKEN, map.get(Constants.ACCESS_TOKEN));
                        break;
                    default:
                        for (String key : map.keySet()) {
                            urlBuilder.addQueryParameter(key, map.get(key));
                        }
                        break;
                }
                httpUrl = urlBuilder.build();
                request = new Request.Builder()
                        .url(httpUrl)
                        .get()
                        .build();
                break;
            case Constants.REQUEST_POST_MEIHOD:
                switch (type){
                    case Constants.REQUEST_AUTH_TOKEN:
                        for (String key : map.keySet()){
                            bodyBuilder.add(key, map.get(key)) ;
                        }
                        url = Constants.REQUEST_AUTH_TOKEN_URL;
                        break;
                    case Constants.REQUEST_LIKE_A_SHOT:
                        url = new StringBuilder().append(Constants.SHOTS_URL).append("/").append(map.get(Constants.ID))
                               .append("/").append(Constants.LIKE).toString();
                        bodyBuilder.add(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
                        break;
                    case Constants.REQUEST_CREATE_A_BUCKET:
                        for (String key : map.keySet()){
                            bodyBuilder.add(key, map.get(key)) ;
                        }
                        url = Constants.BUCKETS_URL;
                        break;
                }
                requestBody = bodyBuilder.build();
                request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                break;
            case Constants.REQUEST_PUT_MEIHOD:
                switch (type) {
                    case Constants.REQUEST_ADD_A_SHOT_TO_BUCKET:
                        url = new StringBuilder().append(Constants.BUCKETS_URL).append("/").append(map.get(Constants.BUCKET_ID)).append("/").append(Constants.SHOTS).toString();
                        map.remove(Constants.BUCKET_ID);
                        for (String key : map.keySet()) {
                            bodyBuilder.addEncoded(key, map.get(key));
                        }
                        break;
                }
                requestBody = bodyBuilder.build();
                request = new Request.Builder()
                        .url(url)
                        .put(requestBody)
                        .build();
                break;
            case Constants.REQUEST_DELETE_MEIHOD:
                switch (type){
                    case Constants.REQUEST_UNLIKE_A_SHOT:
                        url = new StringBuilder().append(Constants.SHOTS_URL).append("/").append(map.get(Constants.ID))
                                .append("/").append(Constants.LIKE).toString();
                        bodyBuilder.add(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
                        break;
                }
                requestBody = bodyBuilder.build();
                request = new Request.Builder()
                        .url(url)
                        .delete(requestBody)
                        .build();
                break;
        }
            Response response = null;
            try {
                response = client.newCall(request).execute();
                json = response.body().string();
                if(json.equals("")){
                    json = String.valueOf(response.code());
                }

            } catch (IOException e) {
                e.printStackTrace();
                json = e.getMessage();
            }
            return json;
        }

    
}
