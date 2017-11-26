package com.menglingpeng.designersshow.mvp.interf;

import android.support.v7.widget.*;

import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.mvp.other.RecyclerAdapter;

/**
 * Created by mengdroid on 2017/10/19.
 */

public interface OnRecyclerListItemListener {
    <T> void onRecyclerFragmentListListener(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, T t);
}
