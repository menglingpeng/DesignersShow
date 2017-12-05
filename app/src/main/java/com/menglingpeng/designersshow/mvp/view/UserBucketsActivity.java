package com.menglingpeng.designersshow.mvp.view;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.SharedPreUtil;
import com.menglingpeng.designersshow.utils.SnackUI;

import java.util.HashMap;

public class UserBucketsActivity extends BaseActivity implements RecyclerView {

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout coordinatorLayout;
    private String title;
    private String type;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_user_buckets;
    }

    @Override
    protected void initViews() {
        super.initViews();
        type = getIntent().getStringExtra(Constants.TYPE);
        title = new StringBuilder().append(getIntent().getStringExtra(Constants.NAME)).append(getString(R.string.s)).append(getString(R.string.buckets)).toString();
        floatingActionButton = (FloatingActionButton)findViewById(R.id.auth_user_bucket_fab);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.user_bukets_cdl);
        toolbar = (Toolbar)findViewById(R.id.user_bukets_tb);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(type.equals(Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER)){
            floatingActionButton.setVisibility(FloatingActionButton.VISIBLE);
            replaceFragment(RecyclerFragment.newInstance(type));
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCreateBucketDialog();
                }
            });
        }else {
            floatingActionButton.setVisibility(FloatingActionButton.GONE);
            replaceFragment(RecyclerFragment.newInstance(getIntent().getStringExtra(Constants.ID), type));
        }
    }

    private void showCreateBucketDialog(){
        final TextInputEditText bucketNameEt, bucketDescEt;
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.create_a_bucket_dialog_message, null);
        builder.setTitle(R.string.create_a_bucket);
        builder.setView(dialogView);
        bucketNameEt = (TextInputEditText)dialogView.findViewById(R.id.bucket_name_tiet);
        bucketDescEt = (TextInputEditText)dialogView.findViewById(R.id.bucket_desc_tiet);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = bucketNameEt.getText().toString();
                if(name.equals("")){
                    SnackUI.showSnackShort(getApplicationContext(), coordinatorLayout, getString(R.string.the_name_of_bucket_is_not_null));
                }else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
                    map.put(Constants.NAME, bucketNameEt.getText().toString());
                    map.put(Constants.DESCRIPTION, bucketDescEt.getText().toString());
                    type = Constants.REQUEST_CREATE_A_BUCKET;
                    RecyclerPresenter presenter = new RecyclerPresenter(UserBucketsActivity.this, type, Constants.REQUEST_NORMAL, Constants.REQUEST_POST_MEIHOD, map, getApplicationContext());
                    presenter.loadJson();
                    SnackUI.showSnackShort(getApplicationContext(), coordinatorLayout, getString(R.string.snack_create_a_bucket_text));
                }

            }
        });
        bucketNameEt.setFocusable(true);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void loadFailed(String msg) {

    }

    @Override
    public void loadSuccess(String json, String requestType) {
        replaceFragment(RecyclerFragment.newInstance(Constants.REQUEST_LIST_BUCKETS_FOR_AUTH_USER));
    }
}
