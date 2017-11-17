package com.menglingpeng.designersshow.mvp.presenter;

import com.menglingpeng.designersshow.mvp.interf.CommentsModelIf;
import com.menglingpeng.designersshow.mvp.interf.CommentsPresenterIf;
import com.menglingpeng.designersshow.mvp.interf.CommentsView;
import com.menglingpeng.designersshow.mvp.interf.OnloadShotsListener;
import com.menglingpeng.designersshow.mvp.model.CommentsModel;

/**
 * Created by mengdroid on 2017/11/17.
 */

public class CommentsPresenter implements CommentsPresenterIf, OnloadShotsListener{

    private CommentsView commentsView;
    private CommentsModel commentsModel;
    private int id;
    private String requestType;

    public CommentsPresenter(CommentsView commentsView, int id, String requestType){
        this.commentsView = commentsView;
        this.id = id;
        this.requestType = requestType;
        this.commentsModel = new CommentsModel();
    }

    @Override
    public void onSuccess(String json, String requestType) {
        commentsView.hideProgressBar();
        commentsView.loadSuccess(json, requestType);
    }

    @Override
    public void onFailure(String msg) {
        commentsView.hideProgressBar();
        commentsView.loadFailed(msg);
    }

    @Override
    public void loadComments() {
        commentsModel.getComments(id, requestType, this);
    }
}
