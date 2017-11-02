package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_toolbar_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                break;
            case R.id.open_in_browser:
                break;
            case R.id.download:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
