package com.menglingpeng.designersshow.mvp.interf;

/**
 * Created by mengdroid on 2017/11/17.
 */

public interface CommentsView {
    void hideProgressBar();
    void loadFailed(String msg);
    void loadSuccess(String shotsJson, String requestType);
}
