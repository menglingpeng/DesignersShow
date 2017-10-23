package com.menglingpeng.designersshow.mvp.model;

import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.load.engine.Resource;
import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.net.HttpUtils;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/17.
 */

public class RecyclerModel implements com.menglingpeng.designersshow.mvp.interf.RecyclerModel{
    private String type;
    private HashMap<String, String> map;
    private BaseActivity baseActivity;

    public RecyclerModel(String type, HashMap<String, String> map, BaseActivity baseActivity){
        this.type = type;
        this.map = map;
        this.baseActivity = baseActivity;
    }

    @Override
    public void getShots(OnloadShotsListener listener) {
         new GetDataTask().execute(listener);
    }

    class GetDataTask extends AsyncTask<OnloadShotsListener, Void, Void> {

        @Override
        protected Void doInBackground(OnloadShotsListener... params) {
            final OnloadShotsListener listener = params[0];
            Callback callback = new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFailure(baseActivity.getString(R.string.on_load_shots_listener_failed_msg));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String shotsJson = response.body().string();
                    Log.i("Response", shotsJson);
                    saveData(map.get("page"), shotsJson);
                    listener.onSuccess();
                }
            };
            HttpUtils.get(map, callback);
            return null;
        }
    }

    private void saveData(String page, String json){
        //加载更多时，Json字符串需要拼接保存。

        if(Integer.valueOf(page) == 1){
            SharedPreUtil.saveShotsJson(type, json);
        }else {
            SharedPreUtil.saveMoreShotsJson(type, json);
        }
        Log.i("SP", SharedPreUtil.getShotsJson(type));
    }
}
