package com.menglingpeng.designersshow.mvp.model;

import android.os.AsyncTask;

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

    public RecyclerModel(String token, String list, String timoframe, String date, String sort, String page){
        this.token = token;
        this.list = list;
        this.timeframe = timoframe;
        this.date = date;
        this.sort = sort;
        this.page = page;
    }

    @Override
    public void getShots(OnloadShotsListener listener) {
         new GetDataTask().execute();
    }

    class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Callback callback = new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String shotsJson = response.body().string();
                    saveData(page, shotsJson);
                }
            };
            HttpUtils.get(token, list, timeframe, date, sort, page, callback);
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
