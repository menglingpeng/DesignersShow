package com.menglingpeng.designersshow.mvp.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.menglingpeng.designersshow.mvp.view.RecyclerFragment;

import java.util.List;

/**
 * Created by mengdroid on 2017/11/9.
 */

public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<RecyclerFragment> fragments;
    private List<String> titles;
    private static RecyclerFragment fragment;

    public TabPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<RecyclerFragment> fragments, List<String> titles){
        this.fragments = fragments;
        this.titles = titles;
    }

    public void clearFragments(){
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isAdded()){
                fragment.onDestroy();
            }
        }
        fragments.clear();
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
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        fragment = (RecyclerFragment)object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public static RecyclerFragment getCurrentPagerViewFragment(){
        return  fragment;
    }
}
