package com.menglingpeng.designersshow.mvp.interf;

import java.util.HashMap;

/**
 * Created by mengdroid on 2017/10/16.
 */

public interface RecyclerModel {
    void getJson(String type, String requestType, String requestMethod, HashMap<String, String> map, OnloadJsonListener
            listener);
}
