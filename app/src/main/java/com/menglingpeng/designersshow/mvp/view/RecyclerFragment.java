package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.widget.RecyclerView;

import com.menglingpeng.designersshow.BaseFragment;

/**
 * Created by mengdroid on 2017/10/13.
 */

public abstract class RecyclerFragment extends BaseFragment {

    RecyclerView recyclerView;

    @Override
    protected void initLayoutId() {

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
