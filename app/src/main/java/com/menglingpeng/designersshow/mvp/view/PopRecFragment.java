package com.menglingpeng.designersshow.mvp.view;

import android.os.Bundle;

import com.menglingpeng.designersshow.utils.Constants;

/**
 * Created by mengdroid on 2017/10/13.
 */

public class PopRecFragment extends RecyclerFragment {

    public static final String POPULAR = "Popular";
    public static final String RECENT = "Recent";
    public static final String FOLLOWING = "Following";


    public static PopRecFragment newInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        PopRecFragment fragment = new PopRecFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
