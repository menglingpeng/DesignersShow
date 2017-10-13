package com.menglingpeng.designersshow.mvp.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengdroid on 2017/10/11.
 */

public class HomeFragment extends BaseFragment {

    public static final String MENU_HOME = "Home";
    public static final String MENU_EXPLORE = "Explore";
    public static final String MENU_MY_LIKES = "My likes";
    public static final String MENU_MY_BUCKETS = "My buckets";
    public static final String MENU_MY_SHOTS = "My shots";
    private static final int SMOOTHSCROLL_TOP_POSITION = 50;
    private String menuType;

    private TabLayout tabs;
    private ViewPager pager;
    private List<RecyclerFragment> fragments;
    private TabPagerAdapter adapter;
    private boolean isLogin = false;

   public static HomeFragment newInstance(String type){
       Bundle bundle = new Bundle();
       bundle.putString(Constants.TYPE, type);
       HomeFragment fragment = new HomeFragment();
       fragment.setArguments(bundle);
       return fragment;
   }

    @Override
    protected void initLayoutId() {
        menuType = getArguments().getString(Constants.TYPE);
        if(MENU_HOME.equals(menuType)){
            layoutId = R.layout.fragment_home;
        }else {
        }
    }

    @Override
    protected void initView() {
        tabs = (TabLayout)rootView.findViewById(R.id.tabs);
        pager = (ViewPager)rootView.findViewById(R.id.pager);
        fragments = new ArrayList<>();
        adapter = new TabPagerAdapter(getChildFragmentManager());
        initFragments();
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                /*scrollToTop(fragments.get(tab.getPosition()).getRecyclerView());*/
            }
        });
    }

    private void initFragments() {
        List<String> titles = new ArrayList<>();
        if (isLogin) {
            titles.add(getString(R.string.home_following));
            titles.add(getString(R.string.home_popular));
            titles.add(getString(R.string.home_recent));
            fragments.add(new PopRecFragment().newInstance(PopRecFragment.FOLLOWING));
            fragments.add(new PopRecFragment().newInstance(PopRecFragment.POPULAR));
            fragments.add(new PopRecFragment().newInstance(PopRecFragment.RECENT));

        } else {
            titles.add(getString(R.string.home_popular));
            titles.add(getString(R.string.home_recent));
            fragments.add(new PopRecFragment().newInstance(PopRecFragment.POPULAR));
            fragments.add(new PopRecFragment().newInstance(PopRecFragment.RECENT));
        }
        adapter.setFragments(fragments, titles);
    }

    private void scrollToTop(RecyclerView list) {
        int lastPosition;
        if (null != list) {
            LinearLayoutManager manager = (LinearLayoutManager) list.getLayoutManager();
            lastPosition = manager.findLastVisibleItemPosition();
            if (lastPosition < SMOOTHSCROLL_TOP_POSITION) {
                list.smoothScrollToPosition(0);
            } else {
                list.scrollToPosition(0);
            }
        }
        }

    @Override
    protected void initData() {

    }

    public static class TabPagerAdapter extends FragmentPagerAdapter{

        private List<RecyclerFragment> fragments;
        private List<String> titles;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private void setFragments(List<RecyclerFragment> fragments, List<String> titles){
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(position);
        }
    }
}
