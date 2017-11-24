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

    private RecyclerView<Shots> recyclerViewIf;
    private RecyclerModel recyclerModel;
    private String type;
    private String requestType;
    private HashMap<String, String> map;
    public RecyclerPresenter(RecyclerView<Shots> recyclerViewIf, String type, String requestType, HashMap<String, String> map, BaseActivity activity){
        this.recyclerViewIf = recyclerViewIf;
        this.type = type;
        this.requestType = requestType;
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
    public void loadShots() {
        recyclerModel.getShots(type, requestType, map, this);
    }

}
