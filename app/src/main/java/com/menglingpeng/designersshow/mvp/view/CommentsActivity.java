package com.menglingpeng.designersshow.mvp.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toolbar;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

public class CommentsActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private String id;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_comments;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.comments_tb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsActivity.this.finish();
            }
        });
        id = getIntent().getStringExtra(Constants.REQUEST_COMMENTS).toString();
        replaceFragment(RecyclerFragment.newInstance(id, Constants.REQUEST_COMMENTS));
    }

}
