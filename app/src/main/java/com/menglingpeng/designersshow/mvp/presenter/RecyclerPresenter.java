package com.menglingpeng.designersshow.mvp.presenter;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.mvp.interf.RecyclerModel;
import com.menglingpeng.designersshow.mvp.interf.RecyclerPresenterIf;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.model.Shots;


/**
 * Created by mengdroid on 2017/10/15.
 */

public class RecyclerPresenter implements RecyclerPresenterIf, OnloadShotsListener {

    private RecyclerView<Shots> recyclerView;
    private RecyclerModel recyclerModel;
    public RecyclerPresenter(RecyclerView<Shots> recyclerView, BaseActivity activity){
        this.recyclerView = recyclerView;


    }

    @Override
    public void onSuccess() {
        recyclerView.hideProgress();

    }

    @Override
    public void onFailure(String msg) {
        recyclerView.hideProgress();
        recyclerView.loadFailed(msg);

    }

    @Override
    public void loadShots() {
        recyclerView.showProgress();
        recyclerModel.getShots(this);
    }

}
