package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.MainActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.interf.RecyclerPresenterIf;
import com.menglingpeng.designersshow.mvp.model.Comments;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.mvp.other.Data;
import com.menglingpeng.designersshow.mvp.other.RecyclerAdapter;
import com.menglingpeng.designersshow.mvp.other.TabPagerFragmentAdapter;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;


/**
 * Created by mengdroid on 2017/10/13.
 */

public class RecyclerFragment extends BaseFragment implements com.menglingpeng.designersshow.mvp.interf.RecyclerView<Shots>, OnRecyclerListItemListener, SwipeRefreshLayout.OnRefreshListener {

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
    private String list= null;
    private String timeframe = null;
    private String date = null;
    private String sort = null;
    private int page = 1;
    private ArrayList<Shots> shotsList;
    private ArrayList<Comments> commmentsList;

    public static RecyclerFragment newInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        RecyclerFragment fragment = new RecyclerFragment() ;
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RecyclerFragment newInstance(String id, String type){
        Bundle bundle = new Bundle();
        bundle.putString(type, id);
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
        type = getArguments().get(Constants.TYPE).toString();
    }

    @Override
    protected void initData() {
        initParameters();
        presenter = new RecyclerPresenter(this, type, mRequestType, map, (BaseActivity)getActivity());
        presenter.loadShots();
    }

    @Override
    protected void initView() {
        swipeRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //确保item大小固定，可以提升性能
        recyclerView.setHasFixedSize(true);
        if(mRequestType != Constants.REQUEST_COMMENTS) {
            swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
            swipeRefresh.setOnRefreshListener(this);
        }else {
            swipeRefresh.setVisibility(SwipeRefreshLayout.GONE);
        }
        recyclerView.setHasFixedSize(true);
    }

    private void initParameters(){
        map = new HashMap<>();
        if(getArguments().get(Constants.REQUEST_COMMENTS) != null){
            map.put(Constants.SHOTS, getArguments().get(Constants.REQUEST_COMMENTS).toString());
            mRequestType = Constants.REQUEST_COMMENTS;
        }else {
            switch (type){
                case Constants.TAB_FOLLOWING:
                    break;
                case Constants.TAB_POPULAR:
                    break;
                case Constants.TAB_RECENT:
                    sort = Constants.SORT_RECENT;
                    break;
                case Constants.SORT_POPULAR:
                    break;
                case Constants.SORT_COMMENTS:
                    sort = Constants.SORT_COMMENTS;
                    break;
                case Constants.SORT_RECENT:
                    sort = Constants.SORT_RECENT;
                    break;
                case Constants.SORT_VIEWS:
                    sort = Constants.SORT_VIEWS;
                    break;
                case Constants.LIST_SHOTS:
                    break;
                case Constants.LIST_ANIMTED:
                    list = Constants.LIST_ANIMTED;
                    break;
                case Constants.LIST_ATTACHMENTS:
                    list = Constants.LIST_ATTACHMENTS;
                    break;
                case Constants.LIST_DEBUTS:
                    list = Constants.LIST_DEBUTS;
                    break;
                case Constants.LIST_PLAYOFFS:
                    list = Constants.LIST_PLAYOFFS;
                    break;
                case Constants.LIST_REBOUNDS:
                    list = Constants.LIST_REBOUNDS;
                    break;
                case Constants.LIST_TEAM:
                    list = Constants.LIST_TEAM;
                    break;
                case Constants.TIMEFRAME_NOW:
                    timeframe = null;
                    break;
                case Constants.TIMEFRAME_WEEK:
                    timeframe = Constants.TIMEFRAME_WEEK;
                    break;
                case Constants.TIMEFRAME_MONTH:
                    timeframe = Constants.TIMEFRAME_MONTH;
                    break;
                case Constants.TIMEFRAME_YEAR:
                    timeframe = Constants.TIMEFRAME_YEAR;
                    break;
                case Constants.TIMEFRAME_EVER:
                    timeframe = Constants.TIMEFRAME_EVER;
                    break;
            }
            //list, timeframe, date, sort缺省状态下，DribbbleAPI有默认值
            if(!type.equals(Constants.TAB_POPULAR) && !type.equals(Constants.TAB_RECENT) && !type.equals(Constants.TAB_FOLLOWING)){
                map = SharedPreUtil.getParameters();
            }
            if(SharedPreUtil.getState(Constants.IS_LOGIN)){
                map.put(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
            }else {
                map.put(Constants.ACCESS_TOKEN, Constants.APP_ACCESS_TOKEN);
            }
            if(list != null){
                map.put(Constants.LIST, list);
            }
            if(timeframe != null){
                map.put(Constants.TIMEFRAME, timeframe);
            }
            if(date != null){
                map.put(Constants.DATE, date);
            }
            if(sort != null){
                map.put(Constants.SORT, sort);
            }
            map.put(Constants.PAGE, String.valueOf(page));
            if(!type.equals(Constants.TAB_POPULAR) && !type.equals(Constants.TAB_RECENT) && !type.equals(Constants.TAB_FOLLOWING)) {
                SharedPreUtil.saveParameters(map);
            }
        }
    }

    @Override
    public void onRefresh() {
        mRequestType = Constants.REQUEST_REFRESH;
        initData();
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
    public void loadSuccess(String shotsjson, String requestType) {
        Fragment fragment = null;
        Context context = null;
        if(!mRequestType.equals(Constants.REQUEST_COMMENTS)) {
            if (type.equals(Constants.TAB_POPULAR) || type.equals(Constants.TAB_RECENT) || type.equals(Constants.TAB_FOLLOWING)) {
                fragment = TabPagerFragmentAdapter.getCurrentPagerViewFragment();
            } else {
                fragment = MainActivity.getCurrentFragment();
            }
            shotsList = Json.parseShots(shotsjson);
        }else {
            context = getActivity().getApplicationContext();
            commmentsList = Json.parseComments(shotsjson);
        }
        switch (requestType){
            case Constants.REQUEST_REFRESH:
                showRefreshProgress(false);
                adapter = new RecyclerAdapter(recyclerView, fragment, type, this);
                recyclerView.setAdapter(adapter);
            case Constants.REQUEST_LOAD_MORE:
                adapter.setLoading(false);
                break;
            case Constants.REQUEST_COMMENTS:
                adapter = new RecyclerAdapter(recyclerView, context, mRequestType, this);
                recyclerView.setAdapter(adapter);
                break;
            default:
                adapter = new RecyclerAdapter(recyclerView, fragment, type, this);
                recyclerView.setAdapter(adapter);
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
        if(mRequestType != Constants.REQUEST_COMMENTS) {
            for (int i = 0; i < shotsList.size(); i++) {
                adapter.addShotsData(shotsList.get(i));
            }
        }else {
            for(int i = 0; i < commmentsList.size(); i++){
                adapter.addCommentsData(commmentsList.get(i));
            }
        }
    }

    @Override
    public void onRecyclerFragmentListListener(RecyclerView.ViewHolder viewHolder, Shots shots) {
        if(viewHolder instanceof RecyclerAdapter.ViewHolder){
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("shots", shots);
            startActivity(intent);
        }
    }

    public void showRefreshProgress(final boolean refreshState){
        if(swipeRefresh != null){
            swipeRefresh.setRefreshing(refreshState);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

}
