package com.menglingpeng.designersshow.mvp.other;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Shot;
import com.menglingpeng.designersshow.mvp.view.UserProfileActivity;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPrefUtil;
import com.menglingpeng.designersshow.utils.TimeUtil;

import java.util.ArrayList;


public class FavoritesRecyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OnRecyclerListItemListener mListener;
    private ArrayList list = new ArrayList<>();
    private Fragment fragment;
    private Context context;
    private String type;

    public FavoritesRecyclerAdapter(RecyclerView recyclerView, Context context, Fragment fragment, final String type,
                           OnRecyclerListItemListener listener) {
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
        view = inflater.inflate(R.layout.user_favorites_recycler_item, parent, false);
        viewHolder = new FavoritesRecyclerAdapter.BookViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BookViewHolder){
            final BookViewHolder viewHolder = (BookViewHolder)holder;
            final Shot shot = (Shot) list.get(position);
            String shotUrl = null;
            boolean isGif = shot.isAnimated();
            int attachmentsCount = shot.getAttachments_count();
            viewHolder.shotTitleTv.setText(shot.getTitle());
            viewHolder.authorTv.setText(shot.getUser().getName());
            viewHolder.shotCreatedTimeTv.setText(TimeUtil.getTimeDifference(shot.getUpdated_at()));
            if(SharedPrefUtil.getState(Constants.SAVING_LOWER_IMAGE)){
                shotUrl = shot.getImages().getTeaser();
            }else {
                shotUrl = shot.getImages().getNormal();
            }
            if(SharedPrefUtil.getState(Constants.GIFS_AUTO_PLAY) && isGif){
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, true);
            }else {
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, false);
            }
            if (isGif) {
                viewHolder.shotGifIv.setVisibility(ImageView.VISIBLE);
            }
            viewHolder.shotLikesCountTv.setText(String.valueOf(shot.getLikes_count()));
            viewHolder.shotCommentsCountTv.setText(String.valueOf(shot.getComments_count()));
            viewHolder.shotViewsCountTv.setText(String.valueOf(shot.getViews_count()));
            if (attachmentsCount != 0) {
                viewHolder.shotAttachmentsCountIv.setVisibility(ImageView.VISIBLE);
                viewHolder.shotAttachmentsCountTv.setVisibility(TextView.VISIBLE);
                viewHolder.shotAttachmentsCountTv.setText(String.valueOf(attachmentsCount));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecyclerFragmentListListener(viewHolder, shot);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

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

        public BookViewHolder(View view) {
            super(view);
            authorTv = (TextView) view.findViewById(R.id.favorites_shot_author_tv);
            shotTitleTv = (TextView) view.findViewById(R.id.favorites_shot_title_tv);
            shotCreatedTimeTv = (TextView) view.findViewById(R.id.favorites_shot_create_time_tv);
            shotIv = (ImageView) view.findViewById(R.id.favorites_shot_iv);
            shotGifIv = (ImageView) view.findViewById(R.id.favorites_shot_gif_iv);
            shotLikesCountTv = (TextView) view.findViewById(R.id.favorites_shot_likes_count_tv);
            shotCommentsCountTv = (TextView) view.findViewById(R.id.favorites_shot_comments_count_tv);
            shotViewsCountTv = (TextView) view.findViewById(R.id.favorites_shot_views_count_tv);
            shotAttachmentsCountIv = (ImageView) view.findViewById(R.id.favorites_shot_attachments_count_iv);
            shotAttachmentsCountTv = (TextView) view.findViewById(R.id.favorites_shot_attachments_count_tv);
        }
    }

    public <T> void addData(T d) {
        list.add(d);
        notifyDataSetChanged();
    }
}
