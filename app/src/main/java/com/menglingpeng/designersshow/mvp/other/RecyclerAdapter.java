package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Buckets;
import com.menglingpeng.designersshow.mvp.model.Comments;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.mvp.model.User;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.TextUtil;
import com.menglingpeng.designersshow.utils.TimeUtil;

import java.util.ArrayList;

/**
 * Created by mengdroid on 2017/10/19.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ITEM_FOOTER = 1;
    private static final int TYPE_ITEM_EMPTY = 2;
    private OnRecyclerListItemListener mListener;
    private ArrayList list = new ArrayList<>();
    private Fragment fragment;
    private Context context;
    private String type;
    private boolean isLoading;
    onLoadingMore loadingMore;
    //加载更多的提前量
    private int visibleThreshold = 2;


    public  RecyclerAdapter(RecyclerView recyclerView, Context context, Fragment fragment, final String type, OnRecyclerListItemListener listener){
        this.type = type;
        this.context = context;
        this.fragment = fragment;
        mListener = listener;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int itemcount = layoutManager.getItemCount();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if(!type.equals(Constants.REQUEST_LIST_DETAIL_OF_AUTH_USER)) {
                    if (!isLoading && lastPosition >= (itemcount - visibleThreshold)) {
                        if (loadingMore != null) {
                            isLoading = true;
                            loadingMore.onLoadMore();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int cout = 0;
        if(list.isEmpty()){
                Log.i("empty", "true");
                cout = 1;
                return cout;
        }else {
            if (list.size() < 12) {
                cout = list.size();
            } else {
                cout = list.size() + 1;
            }
        }
        return cout;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if(list.isEmpty()){
            type = TYPE_ITEM_EMPTY;
        }else {
            if (position == list.size()) {
                type = TYPE_ITEM_FOOTER;
            } else {
                type = TYPE_ITEM;
            }
        }
        return type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case TYPE_ITEM_EMPTY:
                view = inflater.inflate(R.layout.recycler_item_empty, parent, false);
                viewHolder = new EmptyiewHolder(view);
                break;
            case TYPE_ITEM_FOOTER:
                view = inflater.inflate(R.layout.recycler_item_footer_loading, parent, false);
                viewHolder = new FooterViewHolder(view);
                break;
            default:
                switch (type) {
                    case Constants.REQUEST_LIST_COMMENTS:
                        view = inflater.inflate(R.layout.comments_recycler_view_item, parent, false);
                        viewHolder = new CommentsViewHolder(view);
                        break;
                    case Constants.MENU_MY_BUCKETS:
                        view = inflater.inflate(R.layout.buckets_recycler_item, parent, false);
                        viewHolder = new BucketsViewHolder(view);
                        break;
                    case Constants.REQUEST_CHOOSE_BUCKET:
                        view = inflater.inflate(R.layout.buckets_recycler_item, parent, false);
                        viewHolder = new ChooseBucketViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_DETAIL_OF_AUTH_USER:
                        view = inflater.inflate(R.layout.profile_tablayout_detail_item, parent, false);
                        viewHolder = new DetailOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_OF_AUTH_USER:
                        view = inflater.inflate(R.layout.profile_tablayout_followers_item, parent, false);
                        viewHolder = new FollowersOfAuthUserViewHolder(view);
                        break;
                    default:
                        view = inflater.inflate(R.layout.recycler_item, parent, false);
                        viewHolder = new ShotsViewHolder(view);
                        break;
                }
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ShotsViewHolder){
            final ShotsViewHolder viewHolder = (ShotsViewHolder)holder;
            final Shots shots = (Shots)list.get(position);
            boolean isGif = shots.isAnimated();
            int attachments = shots.getAttachments_count();
            ImageLoader.loadCricleImage(fragment, shots.getUser().getAvatar_url(), viewHolder.avatarIv);
            viewHolder.shotsTitleTx.setText(shots.getTitle());
            viewHolder.shots_userTx.setText(shots.getUser().getUsername());
            viewHolder.shotsCreatedTimeTx.setText(TimeUtil.getTimeDifference(shots.getUpdated_at()));
            ImageLoader.load(fragment, shots.getImages().getNormal(), viewHolder.shotsIm, false);
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
            Comments comment = (Comments)list.get(position);
            ImageLoader.loadCricleImage(context, comment.getUser().getAvatar_url(), viewHolder.commentsAvatarIm);
            viewHolder.commentsContentTv.setText(comment.getBody());
            viewHolder.commentsUsernameTv.setText(comment.getUser().getUsername());
            viewHolder.commentsCreateAtTv.setText(comment.getCreated_at());
            viewHolder.commentsLikesCountTv.setText(comment.getLikes_count());
        }else if(holder instanceof BucketsViewHolder){
            final BucketsViewHolder viewHolder = (BucketsViewHolder)holder;
            final Buckets buckets = (Buckets)list.get(position);
            viewHolder.bucketRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onRecyclerFragmentListListener(viewHolder, buckets);
                    }
                }
            });
            viewHolder.bucketNameTx.setText(buckets.getName());
            if(buckets.getDescription() != null) {
                viewHolder.bucketDescTx.setText(buckets.getDescription());
            }else {
                viewHolder.bucketDescTx.setVisibility(TextView.GONE);
            }
            viewHolder.bucketShotsCountTx.setText(TextUtil.setBeforeBold(String.valueOf(buckets.getShots_count()), context.getString(R.string.explore_spinner_list_shots)));
        }else if(holder instanceof ChooseBucketViewHolder){
            final ChooseBucketViewHolder viewHolder = (ChooseBucketViewHolder)holder;
            final Buckets buckets = (Buckets)list.get(position);
            viewHolder.bucketRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onRecyclerFragmentListListener(viewHolder, buckets);
                    }
                }
            });
            viewHolder.bucketNameTx.setText(buckets.getName());
            if(buckets.getDescription() != null) {
                viewHolder.bucketDescTx.setText(buckets.getDescription());
            }else {
                viewHolder.bucketDescTx.setVisibility(TextView.GONE);
            }
            viewHolder.bucketShotsCountTx.setText(TextUtil.setBeforeBold(String.valueOf(buckets.getShots_count()), context.getString(R.string.explore_spinner_list_shots)));
        }else if(holder instanceof FollowersOfAuthUserViewHolder){
            final FollowersOfAuthUserViewHolder viewHolder = (FollowersOfAuthUserViewHolder)holder;
            final User user = (User)list.get(position);
            ImageLoader.loadCricleImage(context, user.getAvatar_url(), viewHolder.followerAvatarIm);
            viewHolder.followerNameTx.setText(user.getUsername());
            viewHolder.followerLocationTx.setText(user.getLocation());
            viewHolder.followerShotsCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getShots_count()), context.getString(R.string.explore_spinner_list_shots)));
            viewHolder.followersOfFollowerCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getFollowers_count()), context.getText(R.string.followers).toString()));
            viewHolder.followerRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onRecyclerFragmentListListener(viewHolder, user);
                    }
                }
            });
        }else if(holder instanceof DetailOfUserViewHolder){
            final DetailOfUserViewHolder viewHolder = (DetailOfUserViewHolder)holder;
            final User user = (User)list.get(position);
            viewHolder.profileTablayoutDetailShotsCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getShots_count()), context.getString(R.string.explore_spinner_list_shots)));
            viewHolder.profileTablayoutDetailLikesCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getLikes_count()), context.getString(R.string.detail_likes_tx)));
            viewHolder.profileTablayoutDetailBucketsCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getBuckets_count()), context.getString(R.string.detail_buckets_tx)));
            viewHolder.profileTablayoutDetailProjectsCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getProject_count()), context.getString(R.string.project)));
            viewHolder.profileTablayoutDetailFollowersCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getFollowers_count()), context.getString(R.string.followers)));
            viewHolder.profileTablayoutDetailFollowingsCountTx.setText(TextUtil.setBeforeBold(String.valueOf(user.getFollowing_count()), context.getString(R.string.following)));
        }else if(holder instanceof EmptyiewHolder){
            int imId = 0;
            int txId = 0;
            EmptyiewHolder viewHolder = (EmptyiewHolder)holder;
            switch (type){
                case Constants.MENU_MY_BUCKETS:
                    imId = R.drawable.ic_image_grey_400_48dp;
                    txId = R.string.no_bucket_here;
                    break;
                case Constants.MENU_MY_SHOTS:
                    imId = R.drawable.ic_image_grey_400_48dp;
                    txId = R.string.no_shot_here;
                    break;
                case Constants.MENU_MY_LIKES:
                    imId = R.drawable.ic_image_grey_400_48dp;
                    txId = R.string.no_liked_shot_here;
                    break;
            }
            viewHolder.emptyIm.setImageResource(imId);
            viewHolder.emptyTx.setText(context.getString(txId));
        }
    }

    public class ShotsViewHolder extends RecyclerView.ViewHolder {
        public final ImageView avatarIv;
        public final TextView shotsTitleTx, shots_userTx, shotsCreatedTimeTx;
        public final ImageView shotsIm, shotsGifIm, itemAttachmentsCountIm;
        public final TextView itemLikesCountTx, itemCommentsCountTx, itemViewsCountTx, itemAttachmentsCountTx;
        public ShotsViewHolder(View view) {
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

    public class DetailOfUserViewHolder extends RecyclerView.ViewHolder{
        public final RelativeLayout profileTablayoutDetailShotsRl, profileTablayoutDetailLikesRl, profileTablayoutDetailBucketsRl,
                profileTablayoutDetailProjectsRl, profileTablayoutDetailFollowersRl, profileTablayoutDetailFollowingsRl;
        public final TextView profileTablayoutDetailShotsCountTx, profileTablayoutDetailLikesCountTx, profileTablayoutDetailBucketsCountTx,
                profileTablayoutDetailProjectsCountTx, profileTablayoutDetailFollowersCountTx, profileTablayoutDetailFollowingsCountTx;
        public DetailOfUserViewHolder(View view) {
            super(view);
            profileTablayoutDetailShotsRl = (RelativeLayout)view.findViewById(R.id.profile_tablayout_detail_shots_rl);
            profileTablayoutDetailLikesRl = (RelativeLayout)view.findViewById(R.id.profile_tablayout_detail_likes_rl);
            profileTablayoutDetailBucketsRl = (RelativeLayout)view.findViewById(R.id.profile_tablayout_detail_buckets_rl);
            profileTablayoutDetailProjectsRl = (RelativeLayout)view.findViewById(R.id.profile_tablayout_detail_projects_rl);
            profileTablayoutDetailFollowersRl = (RelativeLayout)view.findViewById(R.id.profile_tablayout_detail_followers_rl);
            profileTablayoutDetailFollowingsRl = (RelativeLayout)view.findViewById(R.id.profile_tablayout_detail_followings_rl);
            profileTablayoutDetailShotsCountTx = (TextView)view.findViewById(R.id.profile_tablayout_detail_shots_count_tx);
            profileTablayoutDetailLikesCountTx = (TextView)view.findViewById(R.id.profile_tablayout_detail_likes_count_tx);
            profileTablayoutDetailBucketsCountTx = (TextView)view.findViewById(R.id.profile_tablayout_detail_buckets_count_tx);
            profileTablayoutDetailProjectsCountTx = (TextView)view.findViewById(R.id.profile_tablayout_detail_projects_count_tx);
            profileTablayoutDetailFollowersCountTx = (TextView)view.findViewById(R.id.profile_tablayout_detail_followers_count_tx);
            profileTablayoutDetailFollowingsCountTx = (TextView)view.findViewById(R.id.profile_tablayout_detail_followings_count_tx);


        }
    }

    public class FollowersOfAuthUserViewHolder extends RecyclerView.ViewHolder{
        public final RelativeLayout followerRl;
        public final ImageView followerAvatarIm;
        public final TextView followerNameTx, followerLocationTx, followerShotsCountTx, followersOfFollowerCountTx;

        public FollowersOfAuthUserViewHolder(View view) {
            super(view);
            followerRl = (RelativeLayout)view.findViewById(R.id.profile_tablayout_followers_rl);
            followerAvatarIm = (ImageView)view.findViewById(R.id.profile_tablayout_followers_avatar_im);
            followerLocationTx = (TextView)view.findViewById(R.id.profile_tablayout_followers_location_tx);
            followerNameTx = (TextView)view.findViewById(R.id.profile_tablayout_followers_name_tx);
            followerShotsCountTx = (TextView)view.findViewById(R.id.profile_tablayout_followers_shots_count_tx);
            followersOfFollowerCountTx = (TextView)view.findViewById(R.id.profile_tablayout_followers_followers_count_tx);
        }
    }

    public class BucketsViewHolder extends RecyclerView.ViewHolder{
        public final RelativeLayout bucketRl;
        public final TextView bucketNameTx, bucketDescTx, bucketShotsCountTx;

        public BucketsViewHolder(View view) {
            super(view);
            bucketRl = (RelativeLayout)view.findViewById(R.id.bucket_rl);
            bucketNameTx = (TextView)view.findViewById(R.id.bucket_name_tx);
            bucketDescTx = (TextView)view.findViewById(R.id.bucket_desc_tx);
            bucketShotsCountTx = (TextView)view.findViewById(R.id.bucket_shots_count_tx);
        }
    }
    public class ChooseBucketViewHolder extends RecyclerView.ViewHolder{
        public final RelativeLayout bucketRl;
        public final TextView bucketNameTx, bucketDescTx, bucketShotsCountTx;
        public final FloatingActionButton chooseBucketFab;

        public ChooseBucketViewHolder(View view) {
            super(view);
            bucketRl = (RelativeLayout)view.findViewById(R.id.bucket_rl);
            bucketNameTx = (TextView)view.findViewById(R.id.bucket_name_tx);
            bucketDescTx = (TextView)view.findViewById(R.id.bucket_desc_tx);
            bucketShotsCountTx = (TextView)view.findViewById(R.id.bucket_shots_count_tx);
            chooseBucketFab = (FloatingActionButton)view.findViewById(R.id.buckets_recycer_item_fab);
        }
    }

    public class EmptyiewHolder extends RecyclerView.ViewHolder{
        public final ImageView emptyIm;
        public final TextView emptyTx;

        public EmptyiewHolder(View view) {
            super(view);
            emptyIm = (ImageView)view.findViewById(R.id.recycler_item_empty_im);
            emptyTx = (TextView)view.findViewById(R.id.recycler_item_empty_tx);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View view) {
            super(view);
        }
    }

    public <T> void addData(T d){
        list.add(d);
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
