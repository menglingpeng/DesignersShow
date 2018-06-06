package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

public class UserFavoritesActivity extends BaseActivity {

    private Toolbar toolbar;
    private String type;
    private String title;
    private static RecyclerFragment fragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_user_favorites;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = (Toolbar) findViewById(R.id.user_favorites_tb);
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
        type = Constants.REQUEST_LIST_LIKES_FOR_AUTH_USER;
        replaceFragment(newFragment(type));
    }

    private RecyclerFragment newFragment(String type) {
        if (type.equals(Constants.REQUEST_LIST_LIKES_FOR_AUTH_USER)) {
            fragment = RecyclerFragment.newInstance(type);
        } else {
            fragment = RecyclerFragment.newInstance(getIntent().getStringExtra(Constants.ID), type);
        }
        return fragment;
    }

    public static RecyclerFragment getFragment() {
        return fragment;
    }

}
