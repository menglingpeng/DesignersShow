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
     * 首次启动应用后，保存标志位。
     */
    public static boolean saveIsFirstStart(Boolean isFirstStart){
        editor.putBoolean(Constants.IS_FIRST_START, isFirstStart);
        return editor.commit();
    }

    /**
     * 判断应用是否第一次启动。
     */
    public static boolean getIsFirstStart(){
        Boolean isFirstStart = false;
        //isFirstStart不存在则是第一次启动，值为ture
        isFirstStart = sp.getBoolean(Constants.IS_FIRST_START, true);
        return isFirstStart;
    }

    /**
     * 获取用户登陆状态。
     * @return
     *        是否登陆。
     */
    public static boolean getIsLogin(){
        Boolean isLogin = false;
        isLogin = sp.getBoolean(Constants.IS_LOGIN, false);
        return isLogin;
    }

    /**
     * 保存用户登陆状态。
     * @param isLogin
     *              用户登陆状态。
     * @return
     */
    public static boolean saveIsLogin(Boolean isLogin){
        editor.putBoolean(Constants.IS_LOGIN, isLogin);
        return editor.commit();
    }

    /**
     * 发现页面需要各种参数的组合。
     * 保存list shots网络请求的各种参数。
     * @param map
     *           list shots网络请求的各种参数。
     * @return
     */
    public static boolean saveParameters(HashMap<String, String> map){
        for(String key : map.keySet()){
            editor.putString(key, map.get(key));
        }
        return editor.commit();
    }

    /**
     * 获取list shots网络请求的各种参数。
     * @return
     *        参数Map
     */
    public static HashMap<String, String> getParameters(){
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.ACCESS_TOKEN, sp.getString(Constants.ACCESS_TOKEN, null));
        map.put(Constants.LIST, sp.getString(Constants.LIST, null));
        map.put(Constants.TIMEFRAME, sp.getString(Constants.TIMEFRAME, null));
        map.put(Constants.DATE, sp.getString(Constants.DATE, null));
        map.put(Constants.SORT, sp.getString(Constants.SORT, null));
        map.put(Constants.PAGE, sp.getString(Constants.PAGE, null));
        return map;
    }

    public static String getLoginData(String key){
        return sp.getString(key, null);
    }

    /**
     * 退出应用时，需要删除发现页面参数。
     */
    public static void  deletedParameters(){
        String[] parameters = {Constants.LIST,Constants.TIMEFRAME, Constants.DATE, Constants.SORT, Constants.PAGE};
        for (int i=0;i<parameters.length-1;i++){
            editor.remove(parameters[i]);
        }
        editor.commit();
    }

    /**
     * list comments，user等网络请求需要授权token。
     * 获取保存的授权token。
     * @return
     */
    public static String getAuthToken(){
        String accessToken = sp.getString(Constants.ACCESS_TOKEN, null);
        return accessToken;
    }

    /**
     *退出登陆
     */
    public static Boolean DeleteAuthToken(){
        editor.putString(Constants.ACCESS_TOKEN, Constants.APP_ACCESS_TOKEN);
        return editor.commit();
    }

}
