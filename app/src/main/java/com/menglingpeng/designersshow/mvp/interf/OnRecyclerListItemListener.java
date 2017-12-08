package com.menglingpeng.designersshow.mvp.interf;

/**
 * Created by mengdroid on 2017/10/19.
 */

public interface OnRecyclerListItemListener {
    <T> void onRecyclerFragmentListListener(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, T t);
}
