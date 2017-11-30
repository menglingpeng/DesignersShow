package com.menglingpeng.designersshow.mvp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.util.HashMap;

public class MyProfileActivity extends BaseActivity implements RecyclerView{

private String type;
private Toolbar toolbar;
private RecyclerPresenter presenter;
private ProgressBar progressBar;
private HashMap<String, String> map;
    @Override
    protected void initLayoutId() {
        type = getIntent().getStringExtra(Constants.TYPE);
        if(type.equals(Constants.REQUEST_AUTH_USER)){
            layoutId = R.layout.activity_my_profile;
        }

    }

    @Override
    protected void initViews() {
        super.initViews();
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        toolbar = (Toolbar)findViewById(R.id.my_profile_tb);
        map = new HashMap<>();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        map.put(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
        presenter = new RecyclerPresenter(this, type, Constants.REQUEST_NORMAL, Constants.REQUEST_GET_MEIHOD, map, getApplicationContext());
        presenter.loadJson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile_toolbar_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(type.equals(Constants.REQUEST_AUTH_USER)){
            if(item.getItemId() == R.id.profile_logout){
                showLogoutDialog();
            }else {

            }
        }else {
            if(item.getItemId() == R.id.profile_share){

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        builder.setTitle(getText(R.string.profile_logout));
        builder.setMessage(getText(R.string.are_you_sure_want_to_logout));
        builder.setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getText(R.string.profile_logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginOut();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void loginOut(){
        SharedPreUtil.saveState(Constants.IS_LOGIN, false);
        SharedPreUtil.deleteAuthToken();
        restartApplication();
    }

    private void restartApplication(){
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void hideProgress() {
        progressBar.setVisibility(ProgressBar.GONE);
    }

    @Override
    public void loadFailed(String msg) {

    }

    @Override
    public void loadSuccess(String json, String requestType) {
        
    }
}
