package com.menglingpeng.designersshow.mvp.model;

import android.os.AsyncTask;

import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.net.HttpUtils;
import com.menglingpeng.designersshow.net.Json;
import com.menglingpeng.designersshow.utils.Constants;

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
    public void getShots( OnloadShotsListener listener) {
        new GetDataTask().execute();
    }

    class GetDataTask extends AsyncTask<Void, Void, ArrayList<Shots>>{
        ArrayList<Shots> shotsList = new ArrayList<>();

        @Override
        protected ArrayList<Shots> doInBackground(Void... params) {

            Callback callback = new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String shotsJson = response.body().string();
                    shotsList = Json.parseShots(shotsJson);

                }
            };
            HttpUtils.get(token, list, timeframe, date, sort, page, callback);
            return shotsList;
        }
    }


}
