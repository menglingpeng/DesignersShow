package com.menglingpeng.designersshow.mvp.interf;

import java.util.HashMap;

/**
 * Created by mengdroid on 2017/10/16.
 */

public interface RecyclerModel {
    void getShots(String type, HashMap<String, String> map, OnloadJsonListener listener);
}
