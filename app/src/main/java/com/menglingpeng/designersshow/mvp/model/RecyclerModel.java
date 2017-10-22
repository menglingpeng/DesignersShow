package com.menglingpeng.designersshow.mvp.model;

import android.os.AsyncTask;

import com.bumptech.glide.load.engine.Resource;
import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.net.HttpUtils;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/17.
 */

public class RecyclerModel implements com.menglingpeng.designersshow.mvp.interf.RecyclerModel{
    private String token;
    private String list;
    private String timeframe;
    private String date;
    private String sort;
    private String page;
    private ArrayList<String> parametersList;
    private BaseActivity baseActivity;

    public RecyclerModel(ArrayList<String> parametersList, BaseActivity baseActivity){
        this.parametersList = parametersList;
        this.baseActivity = baseActivity;
    }

    @Override
    public void getShots(OnloadShotsListener listener) {
         new GetDataTask().execute();
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
                    saveData(page, shotsJson);
                    listener.onSuccess();
                }
            };
            HttpUtils.get(parametersList, callback);
            return null;
        }
    }

    private void saveData(String page, String json){
        //加载更多时，Json字符串需要拼接保存。
        if(Integer.valueOf(page) == 1){
            SharedPreUtil.saveShotsJson(json);
        }else {
            SharedPreUtil.saveMoreShotsJson(json);
        }
    }
}
