package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Comments;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.TimeUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mengdroid on 2017/10/19.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private OnRecyclerListItemListener mListener;
    private ArrayList<Shots> shotses = new ArrayList<Shots>();
    private ArrayList<Comments> comments = new ArrayList<Comments>();
    private Fragment fragment;
    private Context context;
    private String type;
    private boolean isLoading;
    onLoadingMore loadingMore;
    //加载更多的提前量
    private int visibleThreshold = 2;


    public RecyclerAdapter(RecyclerView recyclerView, Fragment fragment, String type, OnRecyclerListItemListener listener){
        this.fragment = fragment;
        mListener = listener;
        this.type = type;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int itemcount = layoutManager.getItemCount();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if(!isLoading && lastPosition >= (itemcount - visibleThreshold)){
                    if(loadingMore != null){
                        isLoading = true;
                        loadingMore.onLoadMore();
                    }
                }
            }
        });
    }

    public RecyclerAdapter(RecyclerView recyclerView, Context context, String type, OnRecyclerListItemListener listener){
        this.context = context;
        mListener = listener;
        this.type = type;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int itemcount = layoutManager.getItemCount();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if(!isLoading && lastPosition >= (itemcount - visibleThreshold)){
                    if(loadingMore != null){
                        isLoading = true;
                        loadingMore.onLoadMore();
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return shotses.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == shotses.size()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == TYPE_FOOTER){
            view = inflater.inflate(R.layout.recycler_item_footer_loading, parent, false);
            return new FooterViewHolder(view);
        }else {
            if (type != Constants.REQUEST_COMMENTS) {
                view = inflater.inflate(R.layout.recycler_item, parent, false);
                return new ViewHolder(view);
            } else {
                view = inflater.inflate(R.layout.comments_recycler_view_item, parent, false);
                return new CommentsViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder)holder;
            final Shots shots = shotses.get(position);
            boolean isGif = shots.isAnimated();
            int attachments = shots.getAttachments_count();
            ImageLoader.loadCricleImage(fragment, shots.getUser().getAvatar_url(), viewHolder.avatarIv);
            viewHolder.shotsTitleTx.setText(shots.getTitle());
            viewHolder.shots_userTx.setText(shots.getUser().getUsername());
            viewHolder.shotsCreatedTimeTx.setText(TimeUtil.getTimeDifference(shots.getUpdated_at()));
            ImageLoader.load(fragment, shots.getImages().getNormal(), viewHolder.shotsIm);
            if(isGif){
                viewHolder.shotsGifIm.setVisibility(TextView.VISIBLE);
            }
            viewHolder.itemLikesCountTx.setText(String.valueOf(shots.getLikes_count()));
            viewHolder.itemCommentsCountTx.setText(String.valueOf(shots.getComments_count()));
            viewHolder.itemViewsCountTx.setText(String.valueOf(shots.getViews_count()));
            if(attachments !=0){
                viewHolder.itemAttachmentsCountIm.setVisibility(ImageView.VISIBLE);
                viewHolder.itemAttachmentsCountTx.setVisibility(TextView.VISIBLE);
                viewHolder.itemAttachmentsCountTx.setText(String.valueOf(attachments));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onRecyclerFragmentListListener(viewHolder, shots);
                    }
                }
            });
        }else if(holder instanceof CommentsViewHolder){
            CommentsViewHolder viewHolder = (CommentsViewHolder)holder;
            Comments comment = comments.get(position);
            ImageLoader.loadCricleImage(context, comment.getUser().getAvatar_url(), viewHolder.commentsAvatarIm);
            viewHolder.commentsContentTv.setText(comment.getBody());
            viewHolder.commentsUsernameTv.setText(comment.getUser().getUsername());
            viewHolder.commentsCreateAtTv.setText(comment.getCreated_at());
            viewHolder.commentsLikesCountTv.setText(comment.getLikes_count());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView avatarIv;
        public final TextView shotsTitleTx, shots_userTx, shotsCreatedTimeTx;
        public final ImageView shotsIm, shotsGifIm, itemAttachmentsCountIm;
        public final TextView itemLikesCountTx, itemCommentsCountTx, itemViewsCountTx, itemAttachmentsCountTx;
        public ViewHolder(View view) {
            super(view);
            avatarIv = (ImageView) view.findViewById(R.id.avatar_im);
            shotsTitleTx = (TextView) view.findViewById(R.id.shots_title_tx);
            shots_userTx = (TextView) view.findViewById(R.id.shots_user_name_tx);
            shotsCreatedTimeTx = (TextView) view.findViewById(R.id.shots_create_time_tx);
            shotsIm = (ImageView) view.findViewById(R.id.shots_im);
            shotsGifIm = (ImageView) view.findViewById(R.id.shots_gif_im);
            itemLikesCountTx = (TextView) view.findViewById(R.id.item_likes_count_tx);
            itemCommentsCountTx = (TextView) view.findViewById(R.id.item_comments_count_tx);
            itemViewsCountTx = (TextView) view.findViewById(R.id.item_views_count_tx);
            itemAttachmentsCountIm = (ImageView)view.findViewById(R.id.item_attachments_count_im);
            itemAttachmentsCountTx = (TextView)view.findViewById(R.id.item_attachments_count_tx);
        }
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        public final  ImageView commentsAvatarIm, commentsLikesIm;
        public final  TextView commentsUsernameTv, commentsContentTv, commentsCreateAtTv
                ,commentsLikesCountTv;
        public CommentsViewHolder(View view) {
            super(view);
            commentsAvatarIm = (ImageView)view.findViewById(R.id.detail_comments_avatar_im);
            commentsUsernameTv= (TextView)view.findViewById(R.id.detail_comments_username_tx);
            commentsContentTv = (TextView)view.findViewById(R.id.detail_comments_content_tx);
            commentsCreateAtTv = (TextView)view.findViewById(R.id.detail_comments_create_at_tx);
            commentsLikesIm = (ImageView)view.findViewById(R.id.detail_comments_likes_im);
            commentsLikesCountTv = (TextView)view.findViewById(R.id.detail_comments_likes_count_tx);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void addShotsData(Shots data){
        shotses.add(data);
        notifyDataSetChanged();
    }

    public void addCommentsData(Comments data){
        comments.add(data);
        notifyDataSetChanged();
    }

    public void setLoading(boolean isLoading){
        this.isLoading = isLoading;
    }

    public void setLoadingMore(onLoadingMore loadingMore){
        this.loadingMore = loadingMore;
    }

    public interface onLoadingMore{
        void onLoadMore();
    }

}
