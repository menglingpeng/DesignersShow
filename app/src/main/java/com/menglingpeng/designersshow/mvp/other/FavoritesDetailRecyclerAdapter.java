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
import com.menglingpeng.designersshow.mvp.model.Shot;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPrefUtil;
import com.menglingpeng.designersshow.utils.TimeUtil;

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
            case Constants.FAVORITES_DETAIL_THIRD_TYPE:
                view = inflater.inflate(R.layout.user_favorites_detail_recycler_item_third, parent, false);
                viewHolder = new FavoritesDetailRecyclerAdapter.ThirdDetailViewHolder(view);
                break;
             default:
                 break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BaseDetailViewHolder){
            final BaseDetailViewHolder viewHolder = (BaseDetailViewHolder)holder;
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
        }else if(holder instanceof BookDetailViewHolder){
            final BookDetailViewHolder viewHolder = (BookDetailViewHolder)holder;
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

        }else if (holder instanceof ThirdDetailViewHolder){
            final ThirdDetailViewHolder viewHolder = (ThirdDetailViewHolder)holder;
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

    public class BookDetailViewHolder extends RecyclerView.ViewHolder {

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

        public BookDetailViewHolder(View view) {
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

    public class ThirdDetailViewHolder extends RecyclerView.ViewHolder {

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

        public ThirdDetailViewHolder(View view) {
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
