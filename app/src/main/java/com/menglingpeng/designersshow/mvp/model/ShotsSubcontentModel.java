package com.menglingpeng.designersshow.mvp.model;

import android.os.AsyncTask;

import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.mvp.interf.ShotsSubcontentModelIf;
import com.menglingpeng.designersshow.net.HttpUtils;

/**
 * Created by mengdroid on 2017/11/16.
 */

public class ShotsSubcontentModel implements ShotsSubcontentModelIf {

    private String requestType;
    private int id;

    public ShotsSubcontentModel(){
    }

    @Override
    public void getShotsSubContent(int id, String requestType, OnloadShotsListener listener) {
        this.id = id;
        this.requestType = requestType;
        new GetShotsSubContentJsonTask().execute(listener);
    }

    class GetShotsSubContentJsonTask extends AsyncTask<OnloadShotsListener, Void, String>{

        OnloadShotsListener listener;
        String shotsSubJson;

        @Override
        protected String doInBackground(OnloadShotsListener... listeners) {
            listener = listeners[0];
            shotsSubJson = HttpUtils.getSubcontentJson(id, requestType);
            return shotsSubJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                listener.onSuccess(s, requestType);
            }else {
                listener.onFailure("load failed");
            }
        }
    }
}
