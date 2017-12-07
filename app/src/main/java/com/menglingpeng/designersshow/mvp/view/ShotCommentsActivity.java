package com.menglingpeng.designersshow.mvp.view;


import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;


import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

public class ShotCommentsActivity extends BaseActivity {

    private Toolbar toolbar;
    private String id;
    private ImageView avatarIv;
    private EditText editText;
    private FloatingActionButton floatingActionButton;
    private static RecyclerFragment fragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_comments;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = (Toolbar) findViewById(R.id.comments_tb);
        avatarIv = (ImageView)findViewById(R.id.add_comment_avatar_iv);
        editText = (EditText)findViewById(R.id.add_comment_et);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.add_comment_fab);
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
        ImageLoader.loadCricleImage(getApplicationContext(), SharedPreUtil.getLoginData(Constants.AUTH_USER_AVATAR_URL), avatarIv);
    }

    public static RecyclerFragment getFragment() {
        return fragment;
    }

}
