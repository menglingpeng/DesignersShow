package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;

/**
 * Created by mengdroid on 2017/11/1.
 */

public class DetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView imageView;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        imageView = (ImageView)findViewById(R.id.detail_im);
        toolbar = (Toolbar)findViewById(R.id.detail_tb);
        setSupportActionBar(toolbar);
        //设置透明度
        toolbar.getBackground().setAlpha(1);
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }
}
