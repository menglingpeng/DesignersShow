package com.menglingpeng.designersshow.mvp.view;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

public class ChooseBucketActivity extends BaseActivity {

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_choose_bucket;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = (Toolbar)findViewById(R.id.choose_bucket_tb);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.choose_bucket_fab);
        toolbar.setTitle(R.string.choose_a_bucket);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        replaceFragment(RecyclerFragment.newInstance(Constants.MENU_MY_BUCKETS));
    }
}
