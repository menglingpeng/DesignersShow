package com.menglingpeng.designersshow.mvp.model;

import android.os.AsyncTask;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnloadJsonListener;
import com.menglingpeng.designersshow.net.HttpUtils;

import java.util.HashMap;

/**
 * Created by mengdroid on 2017/10/17.
 */

public class RecyclerModel implements com.menglingpeng.designersshow.mvp.interf.RecyclerModel{

    private String type;
    private String requestType;
    private HashMap<String, String> map;
    private BaseActivity baseActivity;

    public RecyclerModel(BaseActivity baseActivity){
        this.baseActivity = baseActivity;

    }

    @Override
    public void getShots(String type, String requestType, HashMap<String, String> map, OnloadJsonListener listener) {
        this.map = map;
        this.type = type;
        this.requestType = requestType;
        new GetDataTask().execute(listener);
    }

    class GetDataTask extends AsyncTask<OnloadJsonListener, Void, String> {
        OnloadJsonListener listener;
        String shotsJson;

        @Override
        protected String doInBackground(OnloadJsonListener... params) {
            listener = params[0];
            shotsJson = HttpUtils.getJson(map, type, requestType);
            return shotsJson;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            if(json != null){
                listener.onSuccess(shotsJson, requestType);
            }else {
                listener.onFailure(baseActivity.getString(R.string.on_load_shots_listener_failed_msg));
            }
        }
    }

}
