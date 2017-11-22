package com.menglingpeng.designersshow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.mvp.model.Shots;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mengdroid on 2017/10/19.
 */

public class SharedPreUtil {

    private static Context context = BaseApplication.getContext();
    private static SharedPreferences sp= context.getSharedPreferences("ShotsJson", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = sp.edit();


    /**
     * 首次启动应用后，保存标志位
     * @param isFirstStart
     * @return
     */
    public static boolean saveIsFirstStart(Boolean isFirstStart){
        editor.putBoolean("isFirstStart", isFirstStart);
        return editor.commit();
    }

    /**
     * 判断应用是否第一次启动
     */
    public static boolean getIsFirstStart(){
        Boolean isFirstStart = false;
        isFirstStart = sp.getBoolean("isFirstStart", true);
        return isFirstStart;
    }

    /**
     * 发现页面需要各种参数的组合
     * 保存list shots网络请求的各种参数
     * @param map
     *           list shots网络请求的各种参数
     * @return
     */
    public static boolean saveParameters(HashMap<String, String> map){
        for(String key : map.keySet()){
            editor.putString(key, map.get(key));
        }
        return editor.commit();
    }

    /**
     * 获取list shots网络请求的各种参数
     * @return
     *        参数Map
     */
    public static HashMap<String, String> getParameters(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", sp.getString("access_token", null));
        map.put("list", sp.getString("list", null));
        map.put("timeframe", sp.getString("timeframe", null));
        map.put("date", sp.getString("date", null));
        map.put("sort", sp.getString("sort", null));
        map.put("page", sp.getString("page", null));
        return map;
    }

    /**
     * 退出应用时，需要删除参数
     */
    public static void  deletedParameters(){
        String[] parameters = {"list","timeframe", "date", "sort", "page"};
        for (int i=0;i<parameters.length-1;i++){
            editor.remove(parameters[i]);
        }
        editor.commit();
    }

    /**
     * list comments，user等网络请求需要单独的授权token
     * 获取保存的授权token
     * @return
     */
    public static String getAuthToken(){
        String accessToken = sp.getString("access_token", null);
        return accessToken;
    }

    /**
     *退出登陆
     */
    public static Boolean DeleteAuthToken(){
        editor.putString("access_token", Constants.ACCESS_TOKEN);
        return editor.commit();
    }

}
