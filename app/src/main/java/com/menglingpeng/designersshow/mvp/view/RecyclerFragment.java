package com.menglingpeng.designersshow.mvp.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

/**
 * Created by mengdroid on 2017/10/13.
 */

public class RecyclerFragment extends BaseFragment {

    RecyclerView recyclerView;
    public static final String POPULAR = "Popular";
    public static final String RECENT = "Recent";
    public static final String FOLLOWING = "Following";

    public static RecyclerFragment newInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        RecyclerFragment fragment = new RecyclerFragment() ;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
