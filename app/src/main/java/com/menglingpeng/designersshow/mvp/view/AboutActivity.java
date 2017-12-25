package com.menglingpeng.designersshow.mvp.view;



import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.model.OpenResource;
import com.menglingpeng.designersshow.mvp.other.AboutRecyclerAdapter;

import java.util.ArrayList;

public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    private AboutRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<OpenResource> list;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_about;
    }

    @Override
    protected void initViews() {
        super.initViews();
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.about_cdl);
        toolbar = (Toolbar)findViewById(R.id.about_tb);
        recyclerView = (RecyclerView)findViewById(R.id.about_rv);
        list = new ArrayList<>();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                false);
        adapter = new AboutRecyclerAdapter(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        for(int i = 0; i < list.size(); i++){
            adapter.addData(list.get(i));
        }
    }
}
