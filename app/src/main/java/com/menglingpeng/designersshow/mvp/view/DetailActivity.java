package com.menglingpeng.designersshow.mvp.view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.model.Shots;

/**
 * Created by mengdroid on 2017/11/1.
 */

public class DetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private Shots shots;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        shots = (Shots) getIntent().getSerializableExtra("shots");
        imageView = (ImageView)findViewById(R.id.detail_im);
        toolbar = (Toolbar)findViewById(R.id.detail_tb);
        setSupportActionBar(toolbar);
        //设置透明度
        toolbar.getBackground().setAlpha(1);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                shareShots();
                break;
            case R.id.open_in_browser:
                break;
            case R.id.download:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareShots(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shots.getTitle()+ shots.getHtml_url());
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.detail_toolbar_overflow_menu_share_create_chooser_title)));
    }
}
