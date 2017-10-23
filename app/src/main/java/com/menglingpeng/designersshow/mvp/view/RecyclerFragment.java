package com.menglingpeng.designersshow.mvp.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.mvp.other.Data;
import com.menglingpeng.designersshow.mvp.other.RecyclerAdapter;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
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
    private String Type;
    private String list= null;
    private String timeframe = null;
    private String date = null;
    private String sort = null;
    private int page = 1;
    private String access_token  = "498b79c0b032215d0e1e1a2fa487a9f8e5637918fa373c63aa29e48528b2822c";
    public static final String TAB_POPULAR = "Popular";
    public static final String TAB_RECENT = "Recent";
    public static final String TAB_FOLLOWING = "Following";
    public static final String MENU_MY_LIKES = "My likes";
    public static final String MENU_MY_BUCKETS = "My buckets";
    public static final String MENU_MY_SHOTS = "My shots";;

    public static RecyclerFragment newInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        RecyclerFragment fragment = new RecyclerFragment() ;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
    }

    @Override
    protected void initView() {
        swipeRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        initFragments();
        presenter = new RecyclerPresenter(this, map, (BaseActivity)getActivity());
        presenter.loadShots();
        linearLayoutManager = new LinearLayoutManager(mActivity);
        adapter = new RecyclerAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
    private void initFragments(){
        map = new HashMap<>();
        Type = getArguments().get(Constants.TYPE).toString();
        switch (Type){
            case TAB_FOLLOWING:
                break;
            case TAB_POPULAR:
                sort = null;
                break;
            case TAB_RECENT:
                sort = Constants.SORT_RECENT;
                break;
        }
        map.put("access_token", access_token);
        if(list != null){
            map.put("list", list);
        }
        if(timeframe != null){
            map.put("timeframe", timeframe);
        }
        if(date != null){
            map.put("date", date);
        }
        if(sort != null){
            map.put("sort", sort);
        }
        map.put("page", String.valueOf(page));
    }

    @Override
    protected void initData() {
        /*initFragments();
        presenter = new RecyclerPresenter(this, map, (BaseActivity)getActivity());
        onRefresh();*/
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onRecyclerFragmentListListener(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof RecyclerAdapter.ViewHolder){
        }
    }

    @Override
    public void onRefresh() {
        presenter.loadShots();
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void hideProgress() {
        showProgress(false);
    }

    @Override
    public void addShots(Shots shots) {
        adapter.addShots(shots);
    }

    @Override
    public void loadFailed(String msg) {

    }

    public void showProgress(final boolean refreshState){
        if(swipeRefresh != null){
            swipeRefresh.setRefreshing(refreshState);
        }
    }
}
