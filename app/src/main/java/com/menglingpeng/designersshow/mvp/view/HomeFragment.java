package com.menglingpeng.designersshow.mvp.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

/**
 * Created by mengdroid on 2017/10/11.
 */

public class HomeFragment extends BaseFragment {

    public static final String MENU_HOME = "Home";
    public static final String MENU_EXPLORE = "Explore";

    private String menuType;
    protected Toolbar toolbar;
    protected int toolbarOverflowMenuId;
    protected String toolbarTitle;
    protected TabLayout tabs;
    protected ViewPager pager;






   public static HomeFragment newInstance(String type){
       Bundle bundle = new Bundle();
       bundle.putString(Constants.TYPE, type);
       HomeFragment fragment = new HomeFragment();
       fragment.setArguments(bundle);
       return fragment;
   }

    @Override
    protected void initLayoutId() {
        fragmentLayoutId = R.layout.fragment_home;


    }

    @Override
    protected void initToolbar() {
        menuType = getArguments().getString(Constants.TYPE);
        if(MENU_HOME.equals(menuType)){
            toolbarTitle = getString(R.string.home_toolbar_title);
            toolbarOverflowMenuId = R.menu.home_overflow_menu;
        }else {
            toolbarTitle = getString(R.string.explore_toolbar_title);
            toolbarOverflowMenuId = R.menu.explore_overflow_menu;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(toolbarOverflowMenuId, menu);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
