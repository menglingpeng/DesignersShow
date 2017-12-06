package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

public class UserProjectsActivity extends BaseActivity {

    private String type;
    private Toolbar toolbar;
    private String title;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_user_projects;
    }

    @Override
    protected void initViews() {
        super.initViews();
        type = getIntent().getStringExtra(Constants.TYPE);
        title = new StringBuilder().append(getIntent().getStringExtra(Constants.NAME)).append(getString(R.string.s))
                .append(getString(R.string.project)).toString();
        toolbar = (Toolbar)findViewById(R.id.user_projects_tb);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(type.equals(Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER)){
            replaceFragment(RecyclerFragment.newInstance(Constants.REQUEST_LIST_PROJECTS_FOR_AUTH_USER));
        }else {
            replaceFragment(RecyclerFragment.newInstance(getIntent().getStringExtra(Constants.ID),
                    Constants.REQUEST_LIST_PROJECTS_FOR_A_USER));
        }
    }
}
