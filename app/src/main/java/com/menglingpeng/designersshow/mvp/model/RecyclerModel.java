package com.menglingpeng.designersshow.mvp.model;

import android.os.AsyncTask;
import android.util.Log;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.MainActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.mvp.other.RecyclerAdapter;
import com.menglingpeng.designersshow.net.HttpUtils;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/17.
 */

public class RecyclerModel implements com.menglingpeng.designersshow.mvp.interf.RecyclerModel{

    private String type;
    private HashMap<String, String> map;
    private BaseActivity baseActivity;

    public RecyclerModel(BaseActivity baseActivity){
        this.baseActivity = baseActivity;

    }

    @Override
    public void getShots(String requestType, HashMap<String, String> map, OnloadShotsListener listener) {
        this.map = map;
        this.type = requestType;
        new GetDataTask().execute(listener);
    }

    class GetDataTask extends AsyncTask<OnloadShotsListener, Void, String> {
        OnloadShotsListener listener;
        String shotsJson;

        @Override
        protected String doInBackground(OnloadShotsListener... params) {
            listener = params[0];
            shotsJson = HttpUtils.getJson(map, type);
            return shotsJson;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            if(json != null){
                listener.onSuccess(shotsJson, type);
            }else {
                listener.onFailure(baseActivity.getString(R.string.on_load_shots_listener_failed_msg));
            }
        }
    }

}
