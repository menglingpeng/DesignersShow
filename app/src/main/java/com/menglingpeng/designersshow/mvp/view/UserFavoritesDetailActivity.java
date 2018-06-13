package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;

public class UserFavoritesDetailActivity extends BaseActivity implements OnRecyclerListItemListener{

    private Toolbar toolbar;
    private String type;
    private String title;
    private static RecyclerFragment fragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_user_favorites_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = (Toolbar) findViewById(R.id.user_favorites_detail_tb);
        title = getString(R.string.nav_my_favorites);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public <T> void onRecyclerFragmentListListener(RecyclerView.ViewHolder viewHolder, T t) {

    }
}
