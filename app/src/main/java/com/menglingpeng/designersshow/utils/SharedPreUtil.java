package com.menglingpeng.designersshow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.mvp.model.Shots;

import java.util.ArrayList;

/**
 * Created by mengdroid on 2017/10/19.
 */

public class SharedPreUtil {

    private static Context context = BaseApplication.getContext();
    private static SharedPreferences sp= context.getSharedPreferences("ShotsJson", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = sp.edit();

    /**
     * 保存第一页数据。
     */
    public static boolean saveShotsJson(String shotsJson){
        editor.putString("shotsJson", shotsJson);
        return editor.commit();
    }
    /**
     * 保存第一页之后的数据。
     */
    public static boolean saveMoreShotsJson(String moreShotsJson){
        String existedJson = sp.getString("shotsJson", null);
        StringBuilder builder = new StringBuilder();
        //合并两个字符串为一个符合JsonArray格式的。
        String shotsJson = builder.append(existedJson.substring(0, existedJson.length()-1)).append(moreShotsJson.replaceFirst(".", "[")).toString();
        editor.putString("shotsJson", shotsJson);
        return editor.commit();
    }
    //获取保存的数据
    public static String getShotsJson(){
        return sp.getString("shotsJson", null);
    }

}
