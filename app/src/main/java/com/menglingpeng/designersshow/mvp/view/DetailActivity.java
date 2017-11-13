package com.menglingpeng.designersshow.mvp.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnloadDetailImageListener;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.utils.ImageLoader;

/**
 * Created by mengdroid on 2017/11/1.
 */

public class DetailActivity extends BaseActivity implements OnloadDetailImageListener {

    private Toolbar toolbar;
    private ImageView imageView;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressBar progressBar;
    private TextView detailTitleTx, detailUpdateTimeTx, detailUserNameTx, detailUserLocation;
    private ImageView detailAvatarIm, detailLikesIm, detailCommentsIm, detailBucketsIm, detailViewsIm;
    private TextView detailAvatarCountTx, detailLikesCountTx, detailCommentsCountTx, detailBucketsCountTx, detailViewsCountTx;
    private Button detailAttachmentsBt;
    private TextView detailDescTx;
    private Shots shots;
    private String htmlUrl, hidpiUrl, imageUrl, imageName;

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
        hidpiUrl = shots.getImages().getHidpi();
        if(hidpiUrl != null) {
            imageUrl = hidpiUrl;
        }else {
            imageUrl = shots.getImages().getNormal();
        }
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        imageView = (ImageView)findViewById(R.id.detail_im);
        toolbar = (Toolbar)findViewById(R.id.detail_tb);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        toolbar.setTitle(shots.getTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });
        ImageLoader.loadDetailImage(getApplicationContext(), imageUrl, imageView, this);
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
        String text = new StringBuilder().append(imageName).append("\n")
                .append(htmlUrl).append("\n")
                .append(getResources().getString(R.string.detail_toolbar_overflow_menu_share_footer_text))
                .toString();
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

    @Override
    public void onSuccess() {
        progressBar.setVisibility(ProgressBar.GONE);
    }

    @Override
    public void onFailure(String msg) {

    }
}
