package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.utils.Constants;

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
        switch (type){
            case Constants.FAVORITES_DETAIL_BASE_TYPE:
                view = inflater.inflate(R.layout.user_favorites_detail_recycler_item_base, parent, false);
                viewHolder = new FavoritesDetailRecyclerAdapter.BaseDetailViewHolder(view);
                break;
            case Constants.FAVORITES_DETAIL_BOOK_TYPE:
                view = inflater.inflate(R.layout.user_favorites_detail_recycler_item_book, parent, false);
                viewHolder = new FavoritesDetailRecyclerAdapter.BookDetailViewHolder(view);
                break;
        }

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

    public class BaseDetailViewHolder extends RecyclerView.ViewHolder {

        public final TextView authorTv;
        public final TextView shotTitleTv;
        public final TextView shotCreatedTimeTv;
        public final ImageView shotIv;
        public final ImageView shotGifIv;
        public final ImageView shotAttachmentsCountIv;
        public final TextView shotLikesCountTv;
        public final TextView shotCommentsCountTv;
        public final TextView shotViewsCountTv;
        public final TextView shotAttachmentsCountTv;

        public BaseDetailViewHolder(View view) {
            super(view);
            authorTv = (TextView) view.findViewById(R.id.favorites_detail_base_item_shot_author_tv);
            shotTitleTv = (TextView) view.findViewById(R.id.favorites_detail_base_item_shot_title_tv);
            shotCreatedTimeTv = (TextView) view.findViewById(R.id.favorites_detail_base_item_shot_create_time_tv);
            shotIv = (ImageView) view.findViewById(R.id.favorites_detail_base_item_shot_iv);
            shotGifIv = (ImageView) view.findViewById(R.id.favorites_detail_base_item_shot_gif_iv);
            shotLikesCountTv = (TextView) view.findViewById(R.id.favorites_detail_base_item_shot_likes_count_tv);
            shotCommentsCountTv = (TextView) view.findViewById(R.id.favorites_detail_base_item_shot_comments_count_tv);
            shotViewsCountTv = (TextView) view.findViewById(R.id.favorites_detail_base_item_shot_views_count_tv);
            shotAttachmentsCountIv = (ImageView) view.findViewById(
                    R.id.favorites_detail_base_item_shot_attachments_count_iv);
            shotAttachmentsCountTv = (TextView) view.findViewById(
                    R.id.favorites_detail_base_item_shot_attachments_count_tv);
        }
    }

    public <T> void addData(T d) {
        list.add(d);
        notifyDataSetChanged();
    }
}
