package com.menglingpeng.designersshow.mvp.presenter;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.mvp.interf.OnloadJsonListener;
import com.menglingpeng.designersshow.mvp.interf.RecyclerModel;
import com.menglingpeng.designersshow.mvp.interf.RecyclerPresenterIf;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.model.Shots;

import java.util.HashMap;


/**
 * Created by mengdroid on 2017/10/15.
 */

public class RecyclerPresenter implements RecyclerPresenterIf, OnloadJsonListener {

    private RecyclerView recyclerViewIf;
    private RecyclerModel recyclerModel;
    private String type;
    private String requestType;
    private String requestMethod;
    private HashMap<String, String> map;
    public RecyclerPresenter(RecyclerView recyclerViewIf, String type, String requestType, String requestMethod, HashMap<String, String> map, BaseActivity activity){
        this.recyclerViewIf = recyclerViewIf;
        this.type = type;
        this.requestType = requestType;
        this.requestMethod = requestMethod;
        this.map = map;
        recyclerModel = new com.menglingpeng.designersshow.mvp.model.RecyclerModel(activity);
    }

    @Override
    public void onSuccess(String shotsJson, String requestType) {
        recyclerViewIf.hideProgress();
        recyclerViewIf.loadSuccess(shotsJson, requestType);
    }

    @Override
    public void onFailure(String msg) {
        recyclerViewIf.hideProgress();
        recyclerViewIf.loadFailed(msg);

    }

    @Override
    public void loadJson() {
        recyclerModel.getJson(type, requestType, requestMethod, map, this);
    }

}
