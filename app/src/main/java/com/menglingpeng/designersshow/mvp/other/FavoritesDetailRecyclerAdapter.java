package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;

import java.util.ArrayList;

public class FavoritesDetailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnRecyclerListItemListener mListener;
    private ArrayList list = new ArrayList<>();
    private Fragment fragment;
    private Context context;
    private String type;

    public FavoritesDetailRecyclerAdapter(RecyclerView recyclerView, Context context, Fragment fragment,
                                          final String type, OnRecyclerListItemListener listener){
        this.type = type;
        this.context = context;
        this.fragment = fragment;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        view = inflater.inflate(R.layout.user_favorites_detail_recycler_item_base, parent, false);
        viewHolder = new FavoritesDetailRecyclerAdapter.BookDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BookDetailViewHolder extends RecyclerView.ViewHolder {


        public BookDetailViewHolder(View itemView) {
            super(itemView);
        }
    }

    public <T> void addData(T d) {
        list.add(d);
        notifyDataSetChanged();
    }
}
