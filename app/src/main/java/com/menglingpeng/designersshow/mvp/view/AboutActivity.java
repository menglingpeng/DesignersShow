package com.menglingpeng.designersshow.mvp.view;



import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;

public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_about;
    }

    @Override
    protected void initViews() {
        super.initViews();
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.about_cdl);
        toolbar = (Toolbar)findViewById(R.id.about_tb);
        recyclerView = (RecyclerView)findViewById(R.id.about_rv);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
