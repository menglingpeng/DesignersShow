package com.menglingpeng.designersshow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mengdroid on 2017/10/11.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    protected int layoutId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            initLayoutId();
            rootView = inflater.inflate(layoutId, container, false);
            initView();
        }
        return rootView;
    }

    @Nullable


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    protected abstract void initLayoutId();

    protected abstract void initView();

    protected abstract void initData();


    public boolean isAlive(){
        return getActivity() != null;
    }

}
