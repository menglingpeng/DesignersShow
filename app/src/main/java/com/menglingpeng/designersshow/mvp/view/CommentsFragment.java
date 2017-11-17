package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.CommentsView;
import com.menglingpeng.designersshow.mvp.presenter.CommentsPresenter;
import com.menglingpeng.designersshow.utils.Constants;

/**
 * Created by mengdroid on 2017/11/17.
 */

public class CommentsFragment extends BaseFragment implements CommentsView{

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CommentsPresenter presenter;
    private String requestType;
    private int id;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_comments;
    }

    @Override
    protected void initView() {
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.comments_recycler_view);

    }

    @Override
    protected void initData() {
        id = Integer.valueOf(getArguments().get(Constants.REQUEST_COMMENTS).toString());
        presenter = new CommentsPresenter(this, id, Constants.REQUEST_COMMENTS);
        presenter.loadComments();
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(ProgressBar.GONE);
    }

    @Override
    public void loadFailed(String msg) {
        progressBar.setVisibility(ProgressBar.GONE);
        
    }

    @Override
    public void loadSuccess(String shotsJson, String requestType) {

    }
}
