package com.menglingpeng.designersshow.mvp.interf;

import com.menglingpeng.designersshow.mvp.model.Shots;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mengdroid on 2017/10/16.
 */

public interface RecyclerModel {
    void getShots(String type, HashMap<String, String> map, OnloadShotsListener listener);
}
