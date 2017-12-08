package com.menglingpeng.designersshow.mvp.interf;

/**
 * Created by mengdroid on 2017/10/15.
 */

public interface OnloadJsonListener {
    void onSuccess(String json, String requestType);
    void onFailure(String msg);
}
