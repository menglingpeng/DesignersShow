package com.menglingpeng.designersshow.mvp.interf;

/**
 * Created by mengdroid on 2017/11/16.
 */

public interface CommentsModelIf {
    void getComments(int id, String requestType, OnloadShotsListener listener);
}
