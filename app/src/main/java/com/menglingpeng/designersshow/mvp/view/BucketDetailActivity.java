package com.menglingpeng.designersshow.mvp.view;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;


import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.model.Buckets;
import com.menglingpeng.designersshow.utils.Constants;

public class BucketDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private Buckets buckets;
    private static RecyclerFragment fragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_bucket_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = (Toolbar) findViewById(R.id.bucket_detail_tb);
        buckets = (Buckets) getIntent().getSerializableExtra(Constants.BUCKETS);
        toolbar.setTitle(buckets.getName());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragment = RecyclerFragment.newInstance(String.valueOf(buckets.getId()), Constants
                .REQUEST_LIST_SHOTS_FOR_A_BUCKET);
        replaceFragment(fragment);
    }

    public static RecyclerFragment getFragment() {
        return fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_overflow_menu, menu);
        menu.findItem(R.id.overflow_date).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
}
