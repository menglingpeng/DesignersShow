package com.menglingpeng.designersshow.mvp.view;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.MainActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Buckets;
import com.menglingpeng.designersshow.mvp.model.Comment;
import com.menglingpeng.designersshow.mvp.model.Follower;
import com.menglingpeng.designersshow.mvp.model.Following;
import com.menglingpeng.designersshow.mvp.model.Likes;
import com.menglingpeng.designersshow.mvp.model.Project;
import com.menglingpeng.designersshow.mvp.model.Shot;
import com.menglingpeng.designersshow.mvp.model.User;
import com.menglingpeng.designersshow.mvp.other.RecyclerAdapter;
import com.menglingpeng.designersshow.mvp.other.TabPagerFragmentAdapter;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.SharedPrefUtil;
import com.menglingpeng.designersshow.utils.SnackUI;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mengdroid on 2017/10/13.
 */

public class RecyclerFragment extends BaseFragment implements com.menglingpeng.designersshow.mvp.interf
        .RecyclerView<Shot>, OnRecyclerListItemListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerPresenter presenter;
    private RecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private BaseActivity mActivity;
    private HashMap<String, String> map;
    private String mRequestType = Constants.REQUEST_NORMAL;
    private String type;
    private String list = null;
    private String timeframe = null;
    private String date = null;
    private String sort = null;
    private int page = 1;
    private ArrayList jsonList = new ArrayList();
    private Fragment fragment = null;
    private Context context;
    private HashMap<String, String> addShotTobucketMap;
    private String text;
    private Snackbar snackbar = null;
    private CoordinatorLayout coordinatorLayout = null;
    private FloatingActionButton floatingActionButton = null;
    private String id;
    private int count = 0;
    private String bucketName;
    private User user;
    private String attachmentUrl;
    private Dialog operateCommentDilaog = null;

    public static RecyclerFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RecyclerFragment newInstance(String id, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        bundle.putString(Constants.ID, id);
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RecyclerFragment newInstance(User user, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        bundle.putSerializable(Constants.USER, user);
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
        type = getArguments().get(Constants.TYPE).toString();
        context = getActivity().getApplicationContext();
    }

    @Override
    protected void initData() {
        initParameters();
        if(!type.equals(Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER) && !type.equals(
                Constants.REQUEST_LIST_DETAIL_FOR_A_USER) &&
                !(type.contains(Constants.REQUEST_LIST_ATTACHMENTS_FOR_A_SHOT)) ) {
            presenter = new RecyclerPresenter(this, type, mRequestType, Constants.REQUEST_GET_MEIHOD,
                    map, context);
            presenter.loadJson();
        }
    }

    @Override
    protected void initView() {
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //确保item大小固定，可以提升性能
        recyclerView.setHasFixedSize(true);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(this);
        if(type.equals(Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER) || type.equals(
                Constants.REQUEST_LIST_DETAIL_FOR_A_USER)){
            setUserAdapter(user);
        }else if(type.indexOf(Constants.REQUEST_LIST_ATTACHMENTS_FOR_A_SHOT) != -1){
            setAttachmentAdapter(attachmentUrl);
        }
    }

    private void initParameters() {
        map = new HashMap<>();
        if (type.indexOf(Constants.EXPLORE) != -1) {
            map = SharedPrefUtil.getParameters();
        }
        switch (type) {
            case Constants.TAB_FOLLOWING:
                sort = Constants.SORT_RECENT;
                break;
            case Constants.TAB_POPULAR:
                break;
            case Constants.TAB_RECENT:
                sort = Constants.SORT_RECENT;
                break;
            case Constants.REQUEST_SORT_POPULAR:
                map = SharedPrefUtil.getParameters();
                break;
            case Constants.REQUEST_SORT_COMMENTS:
                sort = Constants.SORT_COMMENTS;
                break;
            case Constants.REQUEST_SORT_RECENT:
                sort = Constants.SORT_RECENT;
                break;
            case Constants.REQUEST_SORT_VIEWS:
                sort = Constants.SORT_VIEWS;
                break;
            case Constants.REQUEST_LIST_SHOTS:
                map = SharedPrefUtil.getParameters();
                break;
            case Constants.REQUEST_LIST_ANIMTED:
                list = Constants.LIST_ANIMTED;
                break;
            case Constants.REQUEST_LIST_ATTACHMENTS:
                list = Constants.LIST_ATTACHMENTS;
                break;
            case Constants.REQUEST_LIST_DEBUTS:
                list = Constants.LIST_DEBUTS;
                break;
            case Constants.REQUEST_LIST_PLAYOFFS:
                list = Constants.LIST_PLAYOFFS;
                break;
            case Constants.REQUEST_LIST_REBOUNDS:
                list = Constants.LIST_REBOUNDS;
                break;
            case Constants.REQUEST_LIST_TEAM:
                list = Constants.LIST_TEAM;
                break;
            case Constants.REQUEST_TIMEFRAME_NOW:
                map = SharedPrefUtil.getParameters();
                timeframe = null;
                break;
            case Constants.REQUEST_TIMEFRAME_WEEK:
                timeframe = Constants.TIMEFRAME_WEEK;
                break;
            case Constants.REQUEST_TIMEFRAME_MONTH:
                timeframe = Constants.TIMEFRAME_MONTH;
                break;
            case Constants.REQUEST_TIMEFRAME_YEAR:
                timeframe = Constants.TIMEFRAME_YEAR;
                break;
            case Constants.REQUEST_TIMEFRAME_EVER:
                timeframe = Constants.TIMEFRAME_EVER;
                break;
            case Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER:
                user = (User)getArguments().get(Constants.USER);
                break;
            case Constants.MENU_MY_SHOTS:
                sort = Constants.REQUEST_SORT_RECENT;
                break;
            case Constants.MENU_MY_LIKES:
                sort = Constants.SORT_RECENT;
                break;
            case Constants.REQUEST_LIST_SHOTS_FOR_AUTH_USER:
                sort = Constants.REQUEST_SORT_RECENT;
                break;
            case Constants.REQUEST_LIST_LIKES_FOR_AUTH_USER:
                sort = Constants.SORT_RECENT;
                break;
            case Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER:
                user = (User)getArguments().get(Constants.USER);
                break;
            case Constants.REQUEST_LIST_SHOTS_FOR_A_BUCKET:
                map.put(Constants.ID, getArguments().get(Constants.ID).toString());
                break;
            case Constants.REQUEST_LIST_SHOTS_FOR_A_PROJECT:
                map.put(Constants.ID, getArguments().get(Constants.ID).toString());
            case Constants.REQUEST_CHOOSE_BUCKET:
                addShotTobucketMap = new HashMap<>();
                id = getArguments().get(Constants.ID).toString();
                break;
            case Constants.REQUEST_LIST_COMMENTS_FOR_A_SHOT:
                id = getArguments().get(Constants.ID).toString();
                map.put(Constants.ID, id);
                break;
            case Constants.REQUEST_LIST_PROJECTS_FOR_AUTH_USER:
                break;
            case Constants.REQUEST_LIST_FOLLOWERS_FOR_AUTH_USER:
                break;
            case Constants.REQUEST_LIST_FOLLOWING_FOR_AUTH_USER:
                break;
            case Constants.REQUEST_LIST_DETAIL_FOR_A_USER:
                user = (User)getArguments().get(Constants.USER);
                break;
            case Constants.REQUEST_LIST_SHOTS_FOR_A_USER:
                sort = Constants.SORT_RECENT;
                id = String.valueOf(((User) getArguments().getSerializable(Constants.USER)).getId());
                map.put(Constants.ID, id);
                break;
            case Constants.REQUEST_LIST_LIKES_FOR_A_USER:
                sort = Constants.SORT_RECENT;
                id = getArguments().get(Constants.ID).toString();
                map.put(Constants.ID, id);
                break;
            case Constants.REQUEST_LIST_BUCKETS_FOR_A_USER:
                id = getArguments().get(Constants.ID).toString();
                map.put(Constants.ID, id);
                break;
            case Constants.REQUEST_LIST_PROJECTS_FOR_A_USER:
                id = getArguments().get(Constants.ID).toString();
                map.put(Constants.ID, id);
                break;
            case Constants.REQUEST_LIST_FOLLOWERS_FOR_A_USER:
                id = getArguments().get(Constants.ID).toString();
                map.put(Constants.ID, id);
                break;
            case Constants.REQUEST_LIST_FOLLOWING_FOR_A_USER:
                id = getArguments().get(Constants.ID).toString();
                map.put(Constants.ID, id);
                break;
            default:
                if(type.indexOf(Constants.REQUEST_LIST_ATTACHMENTS_FOR_A_SHOT) != -1){
                    attachmentUrl = getArguments().getString(Constants.ID);
                }
                break;
        }
        if (SharedPrefUtil.getState(Constants.IS_LOGIN)) {
            map.put(Constants.ACCESS_TOKEN, SharedPrefUtil.getAuthToken());
        } else {
            map.put(Constants.ACCESS_TOKEN, Constants.APP_ACCESS_TOKEN);
        }
        if (list != null) {
            map.put(Constants.LIST, list);
        }
        if (timeframe != null) {
            map.put(Constants.TIMEFRAME, timeframe);
        }
        if (date != null) {
            map.put(Constants.DATE, date);
        }
        if (sort != null) {
            map.put(Constants.SORT, sort);
        }
        map.put(Constants.PAGE, String.valueOf(page));
        if (type.indexOf(Constants.EXPLORE) != -1) {
            SharedPrefUtil.saveParameters(map);
        }
    }

    private void setUserAdapter(User user){
        fragment = TabPagerFragmentAdapter.getCurrentPagerViewFragment();
        adapter = new RecyclerAdapter(recyclerView, context, fragment, type, this);
        recyclerView.setAdapter(adapter);
        adapter.addData(user);
        progressBar.setVisibility(ProgressBar.GONE);
    }

    private void setAttachmentAdapter(String url){
        fragment = TabPagerFragmentAdapter.getCurrentPagerViewFragment();
        adapter = new RecyclerAdapter(recyclerView, context, fragment, type, this);
        recyclerView.setAdapter(adapter);
        adapter.addData(url);
    }

    @Override
    public void onRefresh() {
        if (type.indexOf(Constants.DETAIL) != -1) {
            showRefreshProgress(false);
        } else {
            page = 1;
            map.put("page", String.valueOf(page));
            mRequestType = Constants.REQUEST_REFRESH;
            initData();
        }
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(ProgressBar.GONE);

    }

    @Override
    public void loadFailed(String msg) {
        showRefreshProgress(false);
    }

    @Override
    public void loadSuccess(String json, String requestType) {
        if (json.indexOf(Constants.TIME_OUT) != -1) {
            SnackUI.showErrorSnackShort(context, getActivity().findViewById(R.id.drawer_layout), getString(R.string
                    .an_error_just_occurred_while_connecting_to_Dribbble_try_it_again_later));
        } else {
            switch (type) {
                case Constants.REQUEST_LIST_SHOTS_FOR_A_BUCKET:
                    fragment = BucketDetailActivity.getFragment();
                    break;
                case Constants.REQUEST_LIST_SHOTS_FOR_A_PROJECT:
                    fragment = ProjectDetailActivity.getFragment();
                    break;
                case Constants.REQUEST_LIST_COMMENTS_FOR_A_SHOT:
                    fragment = ShotCommentsActivity.getFragment();
                    break;
                default:
                    if (type.equals(Constants.TAB_POPULAR) || type.equals(Constants.TAB_RECENT) || type.equals
                            (Constants.TAB_FOLLOWING) || type.equals(Constants.REQUEST_LIST_SHOTS_FOR_AUTH_USER) ||
                            type.equals(Constants.REQUEST_LIST_SHOTS_FOR_A_USER)) {

                        fragment = TabPagerFragmentAdapter.getCurrentPagerViewFragment();
                    } else if (type.equals(Constants.REQUEST_LIST_LIKES_FOR_AUTH_USER) || type.equals(Constants
                            .REQUEST_LIST_LIKES_FOR_A_USER)) {
                        fragment = UserLikesActivity.getFragment();
                    } else {
                        fragment = MainActivity.getCurrentFragment();
                    }
            }
            switch (requestType) {
                case Constants.REQUEST_REFRESH:
                    showRefreshProgress(false);
                    adapter = new RecyclerAdapter(recyclerView, context, fragment, type, this);
                    recyclerView.setAdapter(adapter);
                    break;
                case Constants.REQUEST_LOAD_MORE:
                    adapter.setLoading(false);
                    break;
                case Constants.REQUEST_NORMAL:
                    if (type.equals(Constants.REQUEST_ADD_A_SHOT_TO_BUCKET)) {
                        adapter = null;
                    } else {
                        adapter = new RecyclerAdapter(recyclerView, context, fragment, type, this);
                    }
                    recyclerView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            adapter.setLoadingMore(new RecyclerAdapter.onLoadingMore() {
                @Override
                public void onLoadMore() {
                    adapter.setLoading(true);
                    page += 1;
                    map.put("page", String.valueOf(page));
                    mRequestType = Constants.REQUEST_LOAD_MORE;
                    initData();
                }
            });
            if (!json.equals(Constants.EMPTY)) {
                switch (type) {
                    case Constants.MENU_MY_LIKES:
                        getActivity().findViewById(R.id.progress_bar).setVisibility(ProgressBar.GONE);
                        jsonList = Json.parseArrayJson(json, Likes.class);
                        break;
                    case Constants.REQUEST_LIST_COMMENTS_FOR_A_SHOT:
                        jsonList = Json.parseArrayJson(json, Comment.class);
                        break;
                    case Constants.REQUEST_LIKE_A_COMMENT:
                        if(json.equals(Constants.CODE_201_CREATED)){
                            
                        }
                        break;
                    case Constants.REQUEST_LIST_LIKES_FOR_AUTH_USER:
                        jsonList = Json.parseArrayJson(json, Likes.class);
                        break;
                    case Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER:
                        jsonList = Json.parseArrayJson(json, Buckets.class);
                        break;
                    case Constants.REQUEST_CHOOSE_BUCKET:
                        jsonList = Json.parseArrayJson(json, Buckets.class);
                        break;
                    case Constants.REQUEST_ADD_A_SHOT_TO_BUCKET:
                        //添加到多个bucket
                        count++;
                        String text;
                        getActivity().finish();
                        if (json.equals(Constants.CODE_204_NO_CONTENT) && count == addShotTobucketMap.size()) {
                            if (count == 1) {
                                text = new StringBuilder().append(context.getText(R.string.added_to)).append
                                        (bucketName).toString();
                            } else {
                                text = new StringBuilder().append(context.getText(R.string.added_to)).append(String
                                        .valueOf(count))
                                        .append(context.getText(R.string.buckets)).toString();
                            }
                            Intent intent = new Intent(getActivity(), ShotDetailActivity.class);
                            intent.putExtra(Constants.TYPE, Constants.REQUEST_ADD_A_SHOT_TO_BUCKET);
                            intent.putExtra(Constants.SNACKBAR_TEXT, text);
                            startActivity(intent);
                        }
                        break;
                    case Constants.REQUEST_LIST_PROJECTS_FOR_AUTH_USER:
                        jsonList = Json.parseArrayJson(json, Project.class);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_FOR_AUTH_USER:
                        jsonList = Json.parseArrayJson(json, Follower.class);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWING_FOR_AUTH_USER:
                        jsonList = Json.parseArrayJson(json, Following.class);
                        break;
                    case Constants.REQUEST_LIST_LIKES_FOR_A_USER:
                        jsonList = Json.parseArrayJson(json, Likes.class);
                        break;
                    case Constants.REQUEST_LIST_BUCKETS_FOR_A_USER:
                        jsonList = Json.parseArrayJson(json, Buckets.class);
                        break;
                    case Constants.REQUEST_LIST_PROJECTS_FOR_A_USER:
                        jsonList = Json.parseArrayJson(json, Project.class);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWERS_FOR_A_USER:
                        jsonList = Json.parseArrayJson(json, Follower.class);
                        break;
                    case Constants.REQUEST_LIST_FOLLOWING_FOR_A_USER:
                        jsonList = Json.parseArrayJson(json, Following.class);
                        break;
                    default:
                        jsonList = Json.parseArrayJson(json, Shot.class);
                        break;
                }
                if (!type.equals(Constants.REQUEST_ADD_A_SHOT_TO_BUCKET)) {
                    for (int i = 0; i < jsonList.size(); i++) {
                        if (type.equals(Constants.MENU_MY_LIKES) || type.equals(Constants
                                .REQUEST_LIST_LIKES_FOR_AUTH_USER) || type.equals(Constants
                                .REQUEST_LIST_LIKES_FOR_A_USER)) {
                            adapter.addData(((Likes) jsonList.get(i)).getShot());
                        } else {
                            adapter.addData(jsonList.get(i));
                        }
                    }
                }
            }
        }
    }


    @Override
    public <T> void onRecyclerFragmentListListener(RecyclerView.ViewHolder viewHolder, T t) {
        Intent intent;
        Resources resources = context.getResources();
        if (viewHolder instanceof RecyclerAdapter.ShotViewHolder) {
            intent = new Intent(getActivity(), ShotDetailActivity.class);
            intent.putExtra(Constants.SHOTS, (Shot) t);
            intent.putExtra(Constants.TYPE, Constants.SHOT_DETAIL);
            startActivity(intent);
        } else if(viewHolder instanceof RecyclerAdapter.CommentViewHolder){
            if(operateCommentDilaog != null) {
                operateCommentDilaog.dismiss();
            }
            showOperateDialog(((RecyclerAdapter.CommentViewHolder) viewHolder).commentsItemRl.getHeight(), (Comment) t);
        } else if (viewHolder instanceof RecyclerAdapter.ProfileShotViewHolder) {
            intent = new Intent(getActivity(), ShotDetailActivity.class);
            intent.putExtra(Constants.SHOTS, (Shot) t);
            intent.putExtra(Constants.TYPE, Constants.USER_SHOT_DETAIL);
            intent.putExtra(Constants.USER, (User) getArguments().getSerializable(Constants.USER));
            startActivity(intent);
        } else if (viewHolder instanceof RecyclerAdapter.BucketsViewHolder) {
            intent = new Intent(getActivity(), BucketDetailActivity.class);
            intent.putExtra(Constants.BUCKETS, (Buckets) t);
            startActivity(intent);
        } else if (viewHolder instanceof RecyclerAdapter.ProjectOfUserViewHolder) {
            intent = new Intent(getActivity(), ProjectDetailActivity.class);
            intent.putExtra(Constants.PROJECTS, (Project) t);
            startActivity(intent);
        } else if (viewHolder instanceof RecyclerAdapter.FollowOfUserViewHolder) {
            intent = new Intent(getActivity(), UserProfileActivity.class);
            intent.putExtra(Constants.TYPE, Constants.REQUEST_SINGLE_USER);
            intent.putExtra(Constants.ID, (String) t);
            startActivity(intent);
        } else if (viewHolder instanceof RecyclerAdapter.ChooseBucketViewHolder) {
            Buckets buckets = (Buckets) t;
            bucketName = buckets.getName();
            String itemId = String.valueOf(viewHolder.getLayoutPosition());
            coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.choose_bucket_cdl);
            floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.choose_bucket_fab);
            if (addShotTobucketMap.get(itemId) != null) {
                ((RecyclerAdapter.ChooseBucketViewHolder) viewHolder).bucketRl.setBackgroundColor(getActivity()
                        .getApplicationContext().getResources().getColor(R.color.bucket_recycler_item_background));
                ((RecyclerAdapter.ChooseBucketViewHolder) viewHolder).chooseBucketFab.setVisibility
                        (FloatingActionButton.GONE);
                addShotTobucketMap.remove(itemId);
            } else {
                ((RecyclerAdapter.ChooseBucketViewHolder) viewHolder).bucketRl.setBackgroundColor(Color.WHITE);
                ((RecyclerAdapter.ChooseBucketViewHolder) viewHolder).chooseBucketFab.setVisibility
                        (FloatingActionButton.VISIBLE);
                addShotTobucketMap.put(itemId, String.valueOf(buckets.getId()));
            }
            if (addShotTobucketMap.size() == 0) {
                floatingActionButton.setVisibility(FloatingActionButton.VISIBLE);
            } else {
                floatingActionButton.setVisibility(FloatingActionButton.GONE);
            }
            text = new StringBuilder().append(resources.getString(R.string.choosed)).append(String.valueOf
                    (addShotTobucketMap.size())).append(resources.getString(R.string.buckets)).toString();
            type = Constants.REQUEST_ADD_A_SHOT_TO_BUCKET;
            if (addShotTobucketMap.size() == 0) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            } else if (addShotTobucketMap.size() == 1) {
                if (snackbar != null && snackbar.isShown()) {
                    snackbar.setText(text);
                } else {
                    snackbar = SnackUI.showAddShotToBucketsActionSnack(context, coordinatorLayout, text, type, id,
                            addShotTobucketMap, this);
                    snackbar.show();
                }
            } else {
                snackbar.setText(text);
            }
        }
    }

    private void showOperateDialog(int height, final Comment comment){
        operateCommentDilaog = new Dialog(getContext(), R.style.ThemeOperateCommentDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_operate_comment_, null);
        Window window = operateCommentDilaog.getWindow();
        window.setWindowAnimations(R.style.operateCommentDialog);
        window.setGravity(Gravity.LEFT);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;
        window.setAttributes(layoutParams);
        operateCommentDilaog.setContentView(view);
        operateCommentDilaog.show();
        LinearLayout operateCommentLl = (LinearLayout)view.findViewById(R.id.operate_comment_ll);
        RelativeLayout likeCommentRl = (RelativeLayout)view.findViewById(R.id.like_comment_rl);
        RelativeLayout replyCommentRl = (RelativeLayout)view.findViewById(R.id.reply_comment_rl);
        RelativeLayout copyCommentRl = (RelativeLayout)view.findViewById(R.id.copy_comment_rl);
        operateCommentLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateCommentDilaog.dismiss();
            }
        });
        final EditText editText = (EditText)fragment.getActivity().findViewById(R.id.add_comment_et);
        likeCommentRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = Constants.REQUEST_LIKE_A_COMMENT;
                map.put(Constants.SHOT_ID, id);
                map.put(Constants.COMMENT_ID, String.valueOf(comment.getId()));
                presenter = new RecyclerPresenter(RecyclerFragment.this, type, Constants.REQUEST_NORMAL,
                        Constants.REQUEST_POST_MEIHOD, map, context);
                presenter.loadJson();
            }
        });
        replyCommentRl.setOnClickListener(new View.OnClickListener() {
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

        copyCommentRl.setOnClickListener(new View.OnClickListener() {
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


    public void showRefreshProgress(final boolean refreshState) {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(refreshState);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
