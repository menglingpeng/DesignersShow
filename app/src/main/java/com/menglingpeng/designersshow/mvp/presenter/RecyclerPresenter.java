package com.menglingpeng.designersshow.mvp.presenter;

import android.widget.RemoteViews;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.mvp.interf.RecyclerModel;
import com.menglingpeng.designersshow.mvp.interf.RecyclerPresenterIf;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.mvp.other.RecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mengdroid on 2017/10/15.
 */

public class RecyclerPresenter implements RecyclerPresenterIf, OnloadShotsListener {

    private RecyclerView<Shots> recyclerViewIf;
    private RecyclerModel recyclerModel;
    private String type;
    private HashMap<String, String> map;
    public RecyclerPresenter(RecyclerView<Shots> recyclerViewIf, String requestType, HashMap<String, String> map, BaseActivity activity){
        this.recyclerViewIf = recyclerViewIf;
        this.type = requestType;
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
        recyclerViewIf.showProgress();
        recyclerModel.getShots(type, map, this);
    }

}
