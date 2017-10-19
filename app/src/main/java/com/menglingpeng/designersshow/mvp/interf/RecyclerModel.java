package com.menglingpeng.designersshow.mvp.interf;

import com.menglingpeng.designersshow.mvp.model.Shots;

import java.util.ArrayList;

/**
 * Created by mengdroid on 2017/10/16.
 */

public interface RecyclerModel {
    void getShots(OnloadShotsListener listener);
}
