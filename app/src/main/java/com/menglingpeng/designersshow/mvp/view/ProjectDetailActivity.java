package com.menglingpeng.designersshow.mvp.view;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.model.Project;
import com.menglingpeng.designersshow.utils.Constants;

public class ProjectDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private Project project;
    private static RecyclerFragment fragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_project_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        project = (Project) getIntent().getSerializableExtra(Constants.PROJECTS);
        toolbar = (Toolbar) findViewById(R.id.project_detail_tb);
        toolbar.setTitle(project.getName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragment = RecyclerFragment.newInstance(String.valueOf(project.getId()), Constants
                .REQUEST_LIST_SHOTS_FOR_A_PROJECT);
        replaceFragment(fragment);
    }

    public static RecyclerFragment getFragment() {
        return fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_overflow_menu, menu);
        menu.findItem(R.id.overflow_date).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
}
