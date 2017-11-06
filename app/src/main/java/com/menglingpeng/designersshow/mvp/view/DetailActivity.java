package com.menglingpeng.designersshow.mvp.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.utils.ImageLoader;

/**
 * Created by mengdroid on 2017/11/1.
 */

public class DetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private CoordinatorLayout coordinatorLayout;
    private Shots shots;
    private String htmlUrl, imageUrl, imageName;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        //获取序列化对象
        shots = (Shots) getIntent().getSerializableExtra("shots");
        htmlUrl = shots.getHtml_url();
        imageName = shots.getTitle();
        imageUrl = shots.getImages().getNormal();
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
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
                openInBrowser();
                break;
            case R.id.download:
                download();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareShots(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = new StringBuilder().append(imageName).append(htmlUrl).toString();
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.detail_toolbar_overflow_menu_share_create_chooser_title)));
    }

    private void openInBrowser(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(htmlUrl);
        intent.setData(uri);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.detail_toolbar_overflow_menu_open_in_browser_create_chooser_title)));
        }
    }

    private void download(){
        ImageLoader.downloadImage(getApplicationContext(), coordinatorLayout, imageUrl, imageName);
    }
}