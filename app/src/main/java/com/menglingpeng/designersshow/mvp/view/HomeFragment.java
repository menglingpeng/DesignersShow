package com.menglingpeng.designersshow.mvp.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.menglingpeng.designersshow.BaseApplication;
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
    private static final int SMOOTHSCROLL_TOP_POSITION = 50;
    private String menuType;

    private TabLayout tabLayout;
    private ViewPager pagerView;
    private List<RecyclerFragment> fragments;
    private TabPagerAdapter adapter;
    private Spinner sortSpinner, listSpinner;
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
        if(menuType.equals(MENU_HOME)){
            layoutId = R.layout.fragment_home;
        }else {
            layoutId = R.layout.fragment_explore;
        }
    }

    @Override
    protected void initView() {
        if(menuType.equals(MENU_HOME)){
            initTabPager();
        }else {
            initSpinner();
        }
    }

    private void initTabPager(){
        tabLayout = (TabLayout)rootView.findViewById(R.id.tab_layout);
        pagerView = (ViewPager)rootView.findViewById(R.id.pager_view);
        fragments = new ArrayList<>();
        adapter = new TabPagerAdapter(getChildFragmentManager());
        initFragments();
        pagerView.setAdapter(adapter);
        tabLayout.setupWithViewPager(pagerView);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerView.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                scrollToTop(fragments.get(tab.getPosition()).getRecyclerView());
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
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_FOLLOWING));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_POPULAR));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_RECENT));

        } else {
            titles.add(getString(R.string.home_popular));
            titles.add(getString(R.string.home_recent));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_POPULAR));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_RECENT));
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

    private void initSpinner(){
        sortSpinner = (Spinner)rootView.findViewById(R.id.sort_spinner);
        listSpinner = (Spinner)rootView.findViewById(R.id.list_spinner);
        String[] sort = getResources().getStringArray(R.array.sort);
        String[] list = getResources().getStringArray(R.array.list);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(BaseApplication.getContext(), android.R.layout.simple_spinner_item, sort);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(BaseApplication.getContext(), android.R.layout.simple_spinner_item, list);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        listSpinner.setAdapter(listAdapter);
        sortSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        listSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    public static class TabPagerAdapter extends FragmentPagerAdapter{

        private List<RecyclerFragment> fragments;
        private List<String> titles;
        private static RecyclerFragment fragment;

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

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            fragment = (RecyclerFragment)object;
            super.setPrimaryItem(container, position, object);
        }

        public static RecyclerFragment getCurrentFragment(){
            return  fragment;
        }
    }
}
