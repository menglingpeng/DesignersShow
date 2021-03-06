package com.menglingpeng.designersshow.mvp.other;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Bucket;
import com.menglingpeng.designersshow.mvp.model.Comment;
import com.menglingpeng.designersshow.mvp.model.Follower;
import com.menglingpeng.designersshow.mvp.model.Following;
import com.menglingpeng.designersshow.mvp.model.Project;
import com.menglingpeng.designersshow.mvp.model.Shot;
import com.menglingpeng.designersshow.mvp.model.User;
import com.menglingpeng.designersshow.mvp.view.ShotCommentsActivity;
import com.menglingpeng.designersshow.mvp.view.UserBucketsActivity;
import com.menglingpeng.designersshow.mvp.view.UserLikesActivity;
import com.menglingpeng.designersshow.mvp.view.UserProfileActivity;
import com.menglingpeng.designersshow.mvp.view.UserFollowingActivity;
import com.menglingpeng.designersshow.mvp.view.UserProjectsActivity;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPrefUtil;
import com.menglingpeng.designersshow.utils.SnackUI;
import com.menglingpeng.designersshow.utils.TextUtil;
import com.menglingpeng.designersshow.utils.TimeUtil;

import java.util.ArrayList;

/**
 * Created by mengdroid on 2017/10/19.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM = 0;
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
    private int viewType;

    public RecyclerAdapter(RecyclerView recyclerView, Context context, Fragment fragment, final String type,
                           OnRecyclerListItemListener listener) {
        this.type = type;
        this.context = context;
        this.fragment = fragment;
        mListener = listener;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int itemcount = layoutManager.getItemCount();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if (itemcount >= Constants.PER_PAGE_VALUE) {
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
        if (list.isEmpty()) {
            Log.i("empty", "true");
            cout = 1;
            return cout;
        } else {
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
        if (list.isEmpty()) {
            viewType = TYPE_ITEM_EMPTY;
        } else {
            if (position == list.size()) {
                viewType = TYPE_ITEM_FOOTER;
            } else {
                viewType = TYPE_ITEM;
            }
        }
        return viewType;
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
        switch (viewType) {
            case TYPE_ITEM_EMPTY:
                view = inflater.inflate(R.layout.recycler_item_empty, parent, false);
                viewHolder = new EmptyViewHolder(view);
                break;
            case TYPE_ITEM_FOOTER:
                view = inflater.inflate(R.layout.recycler_item_footer_loading, parent, false);
                viewHolder = new FooterViewHolder(view);
                break;
            default:
                switch (type) {
                    case Constants.MENU_MY_SHOTS:
                        view = inflater.inflate(R.layout.user_shots_recycler_item, parent, false);
                        viewHolder = new ProfileShotViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_COMMENTS_FOR_A_SHOT:
                        view = inflater.inflate(R.layout.comments_recycler_view_item, parent, false);
                        viewHolder = new CommentViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER:
                        view = inflater.inflate(R.layout.user_buckets_recycler_item, parent, false);
                        viewHolder = new BucketViewHolder(view);
                        break;
                    case Constants.REQUEST_CHOOSE_BUCKET:
                        view = inflater.inflate(R.layout.user_buckets_recycler_item, parent, false);
                        viewHolder = new ChooseBucketViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER:
                        view = inflater.inflate(R.layout.user_detail_recycler_item, parent, false);
                        viewHolder = new DetailOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_SHOTS_FOR_AUTH_USER:
                        view = inflater.inflate(R.layout.user_shots_recycler_item, parent, false);
                        viewHolder = new ProfileShotViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_PROJECTS_FOR_AUTH_USER:
                        view = inflater.inflate(R.layout.user_projects_recycler_item, parent, false);
                        viewHolder = new ProjectOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_FOR_AUTH_USER:
                        view = inflater.inflate(R.layout.user_follow_recycler_item, parent, false);
                        viewHolder = new FollowOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWING_FOR_AUTH_USER:
                        view = inflater.inflate(R.layout.user_follow_recycler_item, parent, false);
                        viewHolder = new FollowOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_DETAIL_FOR_A_USER:
                        view = inflater.inflate(R.layout.user_detail_recycler_item, parent, false);
                        viewHolder = new DetailOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_SHOTS_FOR_A_USER:
                        view = inflater.inflate(R.layout.user_shots_recycler_item, parent, false);
                        viewHolder = new ProfileShotViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_BUCKETS_FOR_A_USER:
                        view = inflater.inflate(R.layout.user_buckets_recycler_item, parent, false);
                        viewHolder = new BucketViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_PROJECTS_FOR_A_USER:
                        view = inflater.inflate(R.layout.user_projects_recycler_item, parent, false);
                        viewHolder = new ProjectOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_FOR_A_USER:
                        view = inflater.inflate(R.layout.user_follow_recycler_item, parent, false);
                        viewHolder = new FollowOfUserViewHolder(view);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWING_FOR_A_USER:
                        view = inflater.inflate(R.layout.user_follow_recycler_item, parent, false);
                        viewHolder = new FollowOfUserViewHolder(view);
                        break;
                    default:
                        if(type.indexOf(Constants.REQUEST_LIST_ATTACHMENTS_FOR_A_SHOT) != -1){
                            view = inflater.inflate(R.layout.attachments_dialog_fragment_viewpager_item, parent,
                                    false);
                            viewHolder = new AttachmentViewHolder(view);
                        }else {
                            view = inflater.inflate(R.layout.recycler_item, parent, false);
                            viewHolder = new ShotViewHolder(view);
                        }
                        break;
                }
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShotViewHolder) {
            final ShotViewHolder viewHolder = (ShotViewHolder) holder;
            final Shot shot = (Shot) list.get(position);
            String shotUrl = null;
            boolean isGif = shot.isAnimated();
            int attachmentsCount = shot.getAttachments_count();
            ImageLoader.loadCricleImage(fragment, shot.getUser().getAvatar_url(), viewHolder.avatarIv);
            viewHolder.avatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(Constants.TYPE, Constants.REQUEST_SINGLE_USER);
                    intent.putExtra(Constants.ID, String.valueOf(shot.getUser().getId()));
                    context.startActivity(intent);
                }
            });
            viewHolder.shotTitleTv.setText(shot.getTitle());
            viewHolder.shotUserNameTv.setText(shot.getUser().getName());
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
        } else if(holder instanceof ShotOfSmallWithInfosViewHolder){
            final ShotViewHolder viewHolder = (ShotViewHolder) holder;
            final Shot shot = (Shot) list.get(position);
            String shotUrl = null;
            boolean isGif = shot.isAnimated();
            if(SharedPrefUtil.getState(Constants.SAVING_LOWER_IMAGE)){
                shotUrl = shot.getImages().getTeaser();
            }else {
                shotUrl = shot.getImages().getNormal();
            }
            if(SharedPrefUtil.getState(Constants.GIFS_AUTO_PLAY)){
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, true);
            }else {
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, false);
            }
            if (isGif) {
                viewHolder.shotGifIv.setVisibility(ImageView.VISIBLE);
            }
            viewHolder.shotLikesCountTv.setText(String.valueOf(shot.getLikes_count()));
            viewHolder.shotCommentsCountTv.setText(String.valueOf(shot.getComments_count()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecyclerFragmentListListener(viewHolder, shot);
                    }
                }
            });

        }else if(holder instanceof ShotOfSmallWithoutInfosViewHolder){
            final ShotViewHolder viewHolder = (ShotViewHolder) holder;
            final Shot shot = (Shot) list.get(position);
            String shotUrl = null;
            boolean isGif = shot.isAnimated();
            if(SharedPrefUtil.getState(Constants.SAVING_LOWER_IMAGE)){
                shotUrl = shot.getImages().getTeaser();
            }else {
                shotUrl = shot.getImages().getNormal();
            }
            if(SharedPrefUtil.getState(Constants.GIFS_AUTO_PLAY)){
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, true);
            }else {
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, false);
            }
            if (isGif) {
                viewHolder.shotGifIv.setVisibility(ImageView.VISIBLE);
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
        else if(holder instanceof ShotOfLargeWithoutInfosViewHolder){
            final ShotViewHolder viewHolder = (ShotViewHolder) holder;
            final Shot shot = (Shot) list.get(position);
            String shotUrl = null;
            boolean isGif = shot.isAnimated();
            if(SharedPrefUtil.getState(Constants.SAVING_LOWER_IMAGE)){
                shotUrl = shot.getImages().getTeaser();
            }else {
                shotUrl = shot.getImages().getNormal();
            }
            if(SharedPrefUtil.getState(Constants.GIFS_AUTO_PLAY)){
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, true);
            }else {
                ImageLoader.load(fragment, shotUrl, viewHolder.shotIv, false, false);
            }
            if (isGif) {
                viewHolder.shotGifIv.setVisibility(ImageView.VISIBLE);
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
        else if (holder instanceof CommentViewHolder) {
            final CommentViewHolder viewHolder = (CommentViewHolder) holder;
            final Comment comment = (Comment) list.get(position);
            String userName = comment.getUser().getName();
            ImageLoader.loadCricleImage(context, comment.getUser().getAvatar_url(), viewHolder.avatarIv);
            viewHolder.avatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(Constants.TYPE, Constants.REQUEST_SINGLE_USER);
                    intent.putExtra(Constants.ID, String.valueOf(comment.getUser().getId()));
                    context.startActivity(intent);
                }
            });
            TextUtil.setHtmlText(viewHolder.commentContentTv, comment.getBody());
            viewHolder.commentContentTv.setMovementMethod(LinkMovementMethod.getInstance());
            if(userName.equals(ShotCommentsActivity.getShotUserName())){
                viewHolder.usernameTv.setTextColor(context.getResources().getColor(R.color.shots_username));
            }
            viewHolder.usernameTv.setText(comment.getUser().getName());
            viewHolder.commentCreateAtTv.setText(TimeUtil.getTimeDifference(comment.getCreated_at()));
            viewHolder.commentLikesCountTv.setText(String.valueOf(comment.getLikes_count()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecyclerFragmentListListener(viewHolder, comment);
                    }
                }
            });
        } else if (holder instanceof OperateCommentViewHolder){
            OperateCommentViewHolder viewHolder = (OperateCommentViewHolder)holder;
            final Comment comment = (Comment) list.get(position);
            final EditText editText = (EditText)fragment.getActivity().findViewById(R.id.add_comment_et);
            viewHolder.likeCommentRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            viewHolder.replyCommentRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    editText.setText(new StringBuilder().append("@").append(comment.getUser().getUsername().toString()
                    ));
                    editText.setSelection(editText.getText().toString().length());
                    InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput( editText, 0);
                }
            });
            viewHolder.copyCommentRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("copy comment", comment.getBody());
                    cm.setPrimaryClip(clipData);
                    SnackUI.showSnackShort(context, fragment.getActivity().findViewById(R.id.comments_cdl), context
                            .getString(R.string.comment_text_copied_in_clipboard));
                }
            });
        }
        else if (holder instanceof BucketViewHolder) {
            final BucketViewHolder viewHolder = (BucketViewHolder) holder;
            final Bucket bucket = (Bucket) list.get(position);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecyclerFragmentListListener(viewHolder, bucket);
                    }
                }
            });
            viewHolder.bucketNameTv.setText(bucket.getName());
            viewHolder.bucketShotsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(bucket.getShots_count()),
                    context.getString(R.string.explore_spinner_list_shots)));

        } else if (holder instanceof ChooseBucketViewHolder) {
            final ChooseBucketViewHolder viewHolder = (ChooseBucketViewHolder) holder;
            final Bucket bucket = (Bucket) list.get(position);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecyclerFragmentListListener(viewHolder, bucket);
                    }
                }
            });
            viewHolder.bucketNameTv.setText(bucket.getName());
            viewHolder.bucketShotsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(bucket.getShots_count()),
                    context.getString(R.string.explore_spinner_list_shots)));

        } else if(holder instanceof ProjectOfUserViewHolder){
            final ProjectOfUserViewHolder viewHolder = (ProjectOfUserViewHolder)holder;
            final Project project = (Project)list.get(position);
            viewHolder.projectnameTv.setText(project.getName());
            viewHolder.projectShotsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(project.getShots_count()),
                    context.getString(R.string.explore_spinner_list_shots)));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onRecyclerFragmentListListener(viewHolder, project);
                    }
                }
            });
        } else if (holder instanceof FollowOfUserViewHolder) {
            final FollowOfUserViewHolder viewHolder = (FollowOfUserViewHolder) holder;
            User user;
            if (type.indexOf(Constants.FOLLOWING) != -1) {
                final Following following = (Following) list.get(position);
                user = following.getFollowee();
            } else {
                final Follower follower = (Follower) list.get(position);
                user = follower.getFollower();
            }
            final User userFollow = user;
            ImageLoader.loadCricleImage(context, userFollow.getAvatar_url(), viewHolder.followerAvatarIv);
            viewHolder.followerNameTv.setText(userFollow.getName());
            viewHolder.followerLocationTv.setText(userFollow.getLocation());
            viewHolder.followerShotsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(userFollow.getShots_count()
            ), context.getString(R.string.explore_spinner_list_shots)));
            viewHolder.followersOfFollowerCountTv.setText(TextUtil.setBeforeBold(String.valueOf(userFollow
                    .getFollowers_count()), context.getText(R.string.followers).toString()));
            viewHolder.followerRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecyclerFragmentListListener(viewHolder, String.valueOf(userFollow.getId()));
                    }
                }
            });

        } else if (holder instanceof DetailOfUserViewHolder) {
            final DetailOfUserViewHolder viewHolder = (DetailOfUserViewHolder) holder;
            final User user = (User) list.get(position);
            final ViewPager viewPager = fragment.getActivity().findViewById(R.id.profile_vp);
            viewHolder.profileTablayoutDetailShotsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(user
                    .getShots_count()), context.getString(R.string.explore_spinner_list_shots)));
            viewHolder.profileTablayoutDetailLikesCountTv.setText(TextUtil.setBeforeBold(String.valueOf(user
                    .getLikes_count()), context.getString(R.string.detail_likes_tv_text)));
            viewHolder.profileTablayoutDetailBucketsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(user
                    .getBuckets_count()), context.getString(R.string.detail_buckets_tv_text)));
            viewHolder.profileTablayoutDetailProjectsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(user
                    .getProjects_count()), context.getString(R.string.project)));
            viewHolder.profileTablayoutDetailFollowersCountTv.setText(TextUtil.setBeforeBold(String.valueOf(user
                    .getFollowers_count()), context.getString(R.string.followers)));
            viewHolder.profileTablayoutDetailFollowingsCountTv.setText(TextUtil.setBeforeBold(String.valueOf(user
                    .getFollowings_count()), context.getString(R.string.following)));
            viewHolder.profileTablayoutDetailShotsRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(1);
                }
            });
            viewHolder.profileTablayoutDetailFollowersRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(2);
                }
            });
            viewHolder.profileTablayoutDetailBucketsRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserBucketsActivity.class);
                    intent.putExtra(Constants.NAME, user.getUsername());
                    if (type.equals(Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER)) {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER);
                    } else {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_BUCKETS_FOR_A_USER);
                        intent.putExtra(Constants.ID, String.valueOf(user.getId()));
                    }
                    context.startActivity(intent);
                }
            });
            viewHolder.profileTablayoutDetailLikesRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserLikesActivity.class);
                    intent.putExtra(Constants.NAME, user.getUsername());
                    if (type.equals(Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER)) {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_LIKES_FOR_AUTH_USER);
                    } else {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_LIKES_FOR_A_USER);
                        intent.putExtra(Constants.ID, String.valueOf(user.getId()));
                    }
                    context.startActivity(intent);
                }
            });
            viewHolder.profileTablayoutDetailProjectsRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProjectsActivity.class);
                    intent.putExtra(Constants.NAME, user.getUsername());
                    if (type.equals(Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER)) {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER);
                    } else {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_BUCKETS_FOR_A_USER);
                        intent.putExtra(Constants.ID, String.valueOf(user.getId()));
                    }
                    context.startActivity(intent);
                }
            });
            viewHolder.profileTablayoutDetailFollowingsRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserFollowingActivity.class);
                    intent.putExtra(Constants.NAME, user.getUsername());
                    if (type.equals(Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER)) {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_FOLLOWING_FOR_AUTH_USER);
                    } else {
                        intent.putExtra(Constants.TYPE, Constants.REQUEST_LIST_FOLLOWING_FOR_A_USER);
                        intent.putExtra(Constants.ID, String.valueOf(user.getId()));
                    }
                    context.startActivity(intent);
                }
            });

        } else if (holder instanceof ProfileShotViewHolder) {
            final ProfileShotViewHolder viewHolder = (ProfileShotViewHolder) holder;
            final Shot shot = (Shot) list.get(position);
            boolean isGif = shot.isAnimated();
            int attachmentsCount = shot.getAttachments_count();
            viewHolder.profileShotTitleTv.setText(shot.getTitle());
            viewHolder.profileShotCreatedTimeTv.setText(TimeUtil.getTimeDifference(shot.getUpdated_at()));
            if(SharedPrefUtil.getState(Constants.GIFS_AUTO_PLAY)){
                ImageLoader.load(fragment, shot.getImages().getNormal(), viewHolder.profileShotIv, false,
                        true);
            }else {
                ImageLoader.load(fragment, shot.getImages().getNormal(), viewHolder.profileShotIv, false,
                        false);
            }
            if (isGif) {
                viewHolder.profileShotGifIv.setVisibility(TextView.VISIBLE);
            }
            viewHolder.profileShotLikesCountTv.setText(String.valueOf(shot.getLikes_count()));
            viewHolder.profileShotCommentsCountTv.setText(String.valueOf(shot.getComments_count()));
            viewHolder.profileShotViewsCountTv.setText(String.valueOf(shot.getViews_count()));
            if (attachmentsCount != 0) {
                viewHolder.profileShotAttachmentsCountIv.setVisibility(ImageView.VISIBLE);
                viewHolder.profileShotAttachmentsCountTv.setVisibility(TextView.VISIBLE);
                viewHolder.profileShotAttachmentsCountTv.setText(String.valueOf(attachmentsCount));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecyclerFragmentListListener(viewHolder, shot);
                    }
                }
            });
        } else if(holder instanceof AttachmentViewHolder){
            AttachmentViewHolder viewHolder = (AttachmentViewHolder)holder;
            String url = (String)list.get(position);
            ImageLoader.load(fragment, url, viewHolder.attachmentIv, false, false);
        } else if (holder instanceof EmptyViewHolder) {
            int ivId = 0;
            int tvId = 0;
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            switch (type) {
                case Constants.TAB_FOLLOWING:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_shot_here;
                    break;
                case Constants.MENU_MY_SHOTS:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_shot_here;
                    break;
                case Constants.MENU_MY_LIKES:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_liked_shot_here;
                    break;
                case Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_bucket_here;
                    break;
                case Constants.REQUEST_LIST_SHOTS_FOR_AUTH_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_shot_here;
                    break;
                case Constants.REQUEST_LIST_LIKES_FOR_AUTH_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_liked_shot_here;
                    break;
                case Constants.REQUEST_LIST_PROJECTS_FOR_AUTH_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_project_here;
                    break;
                case Constants.REQUEST_LIST_FOLLOWERS_FOR_AUTH_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_follower_here;
                    break;
                case Constants.REQUEST_LIST_FOLLOWING_FOR_AUTH_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_following_here;
                    break;
                case Constants.REQUEST_LIST_BUCKETS_FOR_A_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_bucket_here;
                    break;
                case Constants.REQUEST_LIST_SHOTS_FOR_A_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_shot_here;
                    break;
                case Constants.REQUEST_LIST_LIKES_FOR_A_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_liked_shot_here;
                    break;
                case Constants.REQUEST_LIST_FOLLOWERS_FOR_A_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_follower_here;
                    break;
                case Constants.REQUEST_LIST_FOLLOWING_FOR_A_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_following_here;
                    break;
                case Constants.REQUEST_LIST_PROJECTS_FOR_A_USER:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_project_here;
                    break;
                case Constants.REQUEST_LIST_COMMENTS_FOR_A_SHOT:
                    ivId = R.drawable.ic_image_grey_400_48dp;
                    tvId = R.string.no_comment_here;
                    break;
                default:
                    break;
            }
            viewHolder.emptyIv.setImageResource(ivId);
            viewHolder.emptyTv.setText(context.getString(tvId));
        }
    }

    public class ShotViewHolder extends RecyclerView.ViewHolder {
        public final ImageView avatarIv;
        public final TextView shotTitleTv;
        public final TextView shotUserNameTv;
        public final TextView shotCreatedTimeTv;
        public final ImageView shotIv;
        public final ImageView shotGifIv;
        public final ImageView shotAttachmentsCountIv;
        public final TextView shotLikesCountTv;
        public final TextView shotCommentsCountTv;
        public final TextView shotViewsCountTv;
        public final TextView shotAttachmentsCountTv;

        public ShotViewHolder(View view) {
            super(view);
            avatarIv = (ImageView) view.findViewById(R.id.avatar_iv);
            shotTitleTv = (TextView) view.findViewById(R.id.shot_title_tv);
            shotUserNameTv = (TextView) view.findViewById(R.id.shot_user_name_tv);
            shotCreatedTimeTv = (TextView) view.findViewById(R.id.shot_create_time_tv);
            shotIv = (ImageView) view.findViewById(R.id.shot_iv);
            shotGifIv = (ImageView) view.findViewById(R.id.shot_gif_iv);
            shotLikesCountTv = (TextView) view.findViewById(R.id.shot_likes_count_tv);
            shotCommentsCountTv = (TextView) view.findViewById(R.id.shot_comments_count_tv);
            shotViewsCountTv = (TextView) view.findViewById(R.id.shot_views_count_tv);
            shotAttachmentsCountIv = (ImageView) view.findViewById(R.id.shot_attachments_count_iv);
            shotAttachmentsCountTv = (TextView) view.findViewById(R.id.shot_attachments_count_tv);
        }
    }

    public class ShotOfLargeWithoutInfosViewHolder extends RecyclerView.ViewHolder{

        public final ImageView shotIv;
        public final ImageView shotGifIv;

        public ShotOfLargeWithoutInfosViewHolder(View view) {
            super(view);
            shotIv = (ImageView)view.findViewById(R.id.large_without_infos_shot_iv);
            shotGifIv = (ImageView)view.findViewById(R.id.large_without_infos_shot_gif_iv);
        }
    }

    public class ShotOfSmallWithInfosViewHolder extends RecyclerView.ViewHolder{

        public final ImageView shotIv;
        public final ImageView shotGifIv;
        public final TextView shotLikesCountTv;
        public final TextView shotCommentsCountTv;

        public ShotOfSmallWithInfosViewHolder(View view) {
            super(view);
            shotIv = (ImageView)view.findViewById(R.id.small_with_infos_shot_iv);
            shotGifIv = (ImageView)view.findViewById(R.id.small_with_infos_shot_gif_iv);
            shotLikesCountTv = (TextView) view.findViewById(R.id.small_with_infos_shot_likes_count_tv);
            shotCommentsCountTv = (TextView) view.findViewById(R.id.small_with_infos_shot_comments_count_tv);
        }
    }

    public class ShotOfSmallWithoutInfosViewHolder extends RecyclerView.ViewHolder{

        public final ImageView shotIv;
        public final ImageView shotGifIv;

        public ShotOfSmallWithoutInfosViewHolder(View view) {
            super(view);
            shotIv = (ImageView)view.findViewById(R.id.small_without_infos_shot_iv);
            shotGifIv = (ImageView)view.findViewById(R.id.small_without_infos_shot_gif_iv);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout commentsItemRl;
        public final ImageView avatarIv;
        public final TextView usernameTv;
        public final TextView commentContentTv;
        public final TextView commentCreateAtTv;
        public final ImageView commentLikesIv;
        public final TextView commentLikesCountTv;

        public CommentViewHolder(View view) {
            super(view);
            commentsItemRl = (RelativeLayout)view.findViewById(R.id.detial_comments_item_rl);
            avatarIv = (ImageView) view.findViewById(R.id.detail_comment_avatar_iv);
            usernameTv = (TextView) view.findViewById(R.id.detail_comment_username_tv);
            commentContentTv = (TextView) view.findViewById(R.id.detail_comment_content_tv);
            commentCreateAtTv = (TextView) view.findViewById(R.id.detail_comment_create_at_tv);
            commentLikesIv = (ImageView) view.findViewById(R.id.detail_comment_likes_iv);
            commentLikesCountTv = (TextView) view.findViewById(R.id.detail_comment_likes_count_tv);
        }
    }

    public class OperateCommentViewHolder extends RecyclerView.ViewHolder{
        public final RelativeLayout likeCommentRl;
        public final RelativeLayout replyCommentRl;
        public final RelativeLayout copyCommentRl;

        public OperateCommentViewHolder(View view) {
            super(view);
            likeCommentRl = (RelativeLayout)view.findViewById(R.id.like_comment_rl);
            replyCommentRl = (RelativeLayout)view.findViewById(R.id.reply_comment_rl);
            copyCommentRl = (RelativeLayout)view.findViewById(R.id.copy_comment_rl);
        }
    }

    public class DetailOfUserViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout profileTablayoutDetailShotsRl;
        public final RelativeLayout profileTablayoutDetailLikesRl;
        public final RelativeLayout profileTablayoutDetailBucketsRl;
        public final RelativeLayout profileTablayoutDetailProjectsRl;
        public final RelativeLayout profileTablayoutDetailFollowersRl;
        public final RelativeLayout profileTablayoutDetailFollowingsRl;
        public final TextView profileTablayoutDetailShotsCountTv;
        public final TextView profileTablayoutDetailLikesCountTv;
        public final TextView profileTablayoutDetailBucketsCountTv;
        public final TextView profileTablayoutDetailProjectsCountTv;
        public final TextView profileTablayoutDetailFollowersCountTv;
        public final TextView  profileTablayoutDetailFollowingsCountTv;

        public DetailOfUserViewHolder(View view) {
            super(view);
            profileTablayoutDetailShotsRl = (RelativeLayout) view.findViewById(R.id.profile_tablayout_detail_shots_rl);
            profileTablayoutDetailLikesRl = (RelativeLayout) view.findViewById(R.id.profile_tablayout_detail_likes_rl);
            profileTablayoutDetailBucketsRl = (RelativeLayout) view.findViewById(R.id
                    .profile_tablayout_detail_buckets_rl);
            profileTablayoutDetailProjectsRl = (RelativeLayout) view.findViewById(R.id
                    .profile_tablayout_detail_projects_rl);
            profileTablayoutDetailFollowersRl = (RelativeLayout) view.findViewById(R.id
                    .profile_tablayout_detail_followers_rl);
            profileTablayoutDetailFollowingsRl = (RelativeLayout) view.findViewById(R.id
                    .profile_tablayout_detail_followings_rl);
            profileTablayoutDetailShotsCountTv = (TextView) view.findViewById(R.id
                    .profile_tablayout_detail_shots_count_tv);
            profileTablayoutDetailLikesCountTv = (TextView) view.findViewById(R.id
                    .profile_tablayout_detail_likes_count_tv);
            profileTablayoutDetailBucketsCountTv = (TextView) view.findViewById(R.id
                    .profile_tablayout_detail_buckets_count_tv);
            profileTablayoutDetailProjectsCountTv = (TextView) view.findViewById(R.id
                    .profile_tablayout_detail_projects_count_tv);
            profileTablayoutDetailFollowersCountTv = (TextView) view.findViewById(R.id
                    .profile_tablayout_detail_followers_count_tv);
            profileTablayoutDetailFollowingsCountTv = (TextView) view.findViewById(R.id
                    .profile_tablayout_detail_followings_count_tv);

        }
    }

    public class ProfileShotViewHolder extends RecyclerView.ViewHolder {
        public final TextView profileShotTitleTv;
        public final TextView profileShotCreatedTimeTv;
        public final ImageView profileShotIv;
        public final ImageView profileShotGifIv;
        public final ImageView profileShotAttachmentsCountIv;
        public final TextView profileShotLikesCountTv;
        public final TextView profileShotCommentsCountTv;
        public final TextView profileShotViewsCountTv;
        public final TextView  profileShotAttachmentsCountTv;

        public ProfileShotViewHolder(View view) {
            super(view);
            profileShotTitleTv = (TextView) view.findViewById(R.id.profile_shot_title_tv);
            profileShotCreatedTimeTv = (TextView) view.findViewById(R.id.profile_shot_create_time_tv);
            profileShotIv = (ImageView) view.findViewById(R.id.profile_shot_iv);
            profileShotGifIv = (ImageView) view.findViewById(R.id.profile_shot_gif_iv);
            profileShotLikesCountTv = (TextView) view.findViewById(R.id.profile_shot_likes_count_tv);
            profileShotCommentsCountTv = (TextView) view.findViewById(R.id.profile_shot_comments_count_tv);
            profileShotViewsCountTv = (TextView) view.findViewById(R.id.profile_shot_views_count_tv);
            profileShotAttachmentsCountIv = (ImageView) view.findViewById(R.id.profile_shot_attachments_count_iv);
            profileShotAttachmentsCountTv = (TextView) view.findViewById(R.id.profile_shot_attachments_count_tv);
        }
    }

    public class BucketViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout bucketRl;
        public final TextView bucketNameTv;
        public final TextView bucketShotsCountTv;

        public BucketViewHolder(View view) {
            super(view);
            bucketRl = (RelativeLayout) view.findViewById(R.id.bucket_rl);
            bucketNameTv = (TextView) view.findViewById(R.id.bucket_name_tv);
            bucketShotsCountTv = (TextView) view.findViewById(R.id.bucket_shots_count_tv);
        }
    }

    public class ChooseBucketViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout bucketRl;
        public final TextView bucketNameTv;
        public final TextView bucketShotsCountTv;
        public final FloatingActionButton chooseBucketFab;

        public ChooseBucketViewHolder(View view) {
            super(view);
            bucketRl = (RelativeLayout) view.findViewById(R.id.bucket_rl);
            bucketNameTv = (TextView) view.findViewById(R.id.bucket_name_tv);
            bucketShotsCountTv = (TextView) view.findViewById(R.id.bucket_shots_count_tv);
            chooseBucketFab = (FloatingActionButton) view.findViewById(R.id.buckets_recycer_item_fab);
        }
    }

    public class ProjectOfUserViewHolder extends RecyclerView.ViewHolder{
        public final RelativeLayout projectRl;
        public final TextView projectnameTv;
        public final TextView projectShotsCountTv;

        public ProjectOfUserViewHolder(View view) {
            super(view);
            projectRl = (RelativeLayout)view.findViewById(R.id.project_rl);
            projectnameTv = (TextView)view.findViewById(R.id.project_name_tv);
            projectShotsCountTv = (TextView)view.findViewById(R.id.project_shots_count_tv);
        }
    }

    public class FollowOfUserViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout followerRl;
        public final ImageView followerAvatarIv;
        public final TextView followerNameTv;
        public final TextView followerLocationTv;
        public final TextView followerShotsCountTv;
        public final TextView followersOfFollowerCountTv;

        public FollowOfUserViewHolder(View view) {
            super(view);
            followerRl = (RelativeLayout) view.findViewById(R.id.profile_tablayout_follow_rl);
            followerAvatarIv = (ImageView) view.findViewById(R.id.profile_tablayout_follow_avatar_iv);
            followerLocationTv = (TextView) view.findViewById(R.id.profile_tablayout_follow_location_tv);
            followerNameTv = (TextView) view.findViewById(R.id.profile_tablayout_follow_name_tv);
            followerShotsCountTv = (TextView) view.findViewById(R.id.profile_tablayout_follow_shots_count_tv);
            followersOfFollowerCountTv = (TextView) view.findViewById(R.id
                    .profile_tablayout_followers_of_follow_count_tv);
        }
    }

    public class AttachmentViewHolder extends RecyclerView.ViewHolder{

        private final ImageView attachmentIv;
        public AttachmentViewHolder(View view) {
            super(view);
            attachmentIv = (ImageView)view.findViewById(R.id.attachments_dialog_viewpager_item_iv);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView emptyIv;
        public final TextView emptyTv;

        public EmptyViewHolder(View view) {
            super(view);
            emptyIv = (ImageView) view.findViewById(R.id.recycler_item_empty_iv);
            emptyTv = (TextView) view.findViewById(R.id.recycler_item_empty_tv);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }
    }

    public <T> void addData(T d) {
        list.add(d);
        notifyDataSetChanged();
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setLoadingMore(onLoadingMore loadingMore) {
        this.loadingMore = loadingMore;
    }

    public interface onLoadingMore {
        void onLoadMore();
    }

}
