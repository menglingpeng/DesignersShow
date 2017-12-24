package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;

/**
 * Created by mengdroid on 2017/12/24.
 */

public class AboutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private OnRecyclerListItemListener listener;
    public AboutRecyclerAdapter (Context context, OnRecyclerListItemListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public
}
