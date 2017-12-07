package com.menglingpeng.designersshow.mvp.view;


import android.view.View;
import android.support.v7.widget.Toolbar;


import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

public class ShotCommentsActivity extends BaseActivity {

    private Toolbar toolbar;
    private String id;
    private static RecyclerFragment fragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_comments;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = (Toolbar) findViewById(R.id.comments_tb);
        toolbar.setTitle(getIntent().getStringExtra(Constants.COMMENTS_COUNT));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShotCommentsActivity.this.finish();
            }
        });
        id = getIntent().getStringExtra(Constants.SHOT_ID).toString();
        fragment = RecyclerFragment.newInstance(id, Constants.REQUEST_LIST_COMMENTS);
        replaceFragment(fragment);
    }

    public static RecyclerFragment getFragment() {
        return fragment;
    }

}
