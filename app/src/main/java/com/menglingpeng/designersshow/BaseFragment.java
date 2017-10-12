package com.menglingpeng.designersshow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mengdroid on 2017/10/11.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected int fragmentLayoutId;
    protected Toolbar toolbar;
    protected String toolbarTitle = "Home";
    protected TabLayout tabs;
    protected ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            initLayoutId();
            rootView = inflater.inflate(fragmentLayoutId, container, false);
            setHasOptionsMenu(true);
            toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
            if(toolbar != null){
                ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            }
            if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
                initToolbar();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(toolbarTitle);
                toolbar.setTitleTextColor(Color.WHITE);
                toolbar.setNavigationIcon(R.mipmap.ic_toobar_nav_menu);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
            tabs = (TabLayout)rootView.findViewById(R.id.tabs);
            pager = (ViewPager)rootView.findViewById(R.id.pager);
            initView();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract void initLayoutId();

    protected abstract void initToolbar();

    protected abstract void initView();

    protected abstract void initData();


    public boolean isAlive(){
        return getActivity() != null;
    }

}
