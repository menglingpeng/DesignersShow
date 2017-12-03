package com.menglingpeng.designersshow.mvp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.model.User;
import com.menglingpeng.designersshow.mvp.other.TabPagerFragmentAdapter;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class MyProfileActivity extends BaseActivity implements RecyclerView{

private String type;
private Toolbar toolbar;
private ImageView profileBackgroundIm, profileAvatarIm;
private RecyclerPresenter presenter;
private ProgressBar progressBar;
private TabLayout profileTl;
private ViewPager profileVp;
private TabPagerFragmentAdapter adapter;
private HashMap<String, String> map;
private ArrayList<RecyclerFragment> fragmentsList;
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
        map = new HashMap<>();
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
        User user = Json.parseJson(json, User.class);
        toolbar = (Toolbar)findViewById(R.id.profile_tb);
        profileBackgroundIm = (ImageView)findViewById(R.id.profile_backgroud_im);
        profileAvatarIm = (ImageView)findViewById(R.id.profile_avatar_im);
        profileTl = (TabLayout)findViewById(R.id.profile_tl);
        profileVp = (ViewPager)findViewById(R.id.profile_vp);
        fragmentsList = new ArrayList<>();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageLoader.load(getApplicationContext(), user.getAvatar_url(), profileBackgroundIm, true);
        ImageLoader.loadCricleImage(getApplicationContext(), user.getAvatar_url(), profileAvatarIm);
        initTabPager();
    }

    private void initTabPager(){
        adapter = new TabPagerFragmentAdapter(getSupportFragmentManager());
        initTabFragments();
        profileVp.setAdapter(adapter);
        profileTl.setupWithViewPager(profileVp);
        profileTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                profileVp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initTabFragments(){
        ArrayList<String> titlesList = new ArrayList<>();
        titlesList.add(getText(R.string.detail).toString());
        titlesList.add(getText(R.string.explore_spinner_list_shots).toString());
        titlesList.add(getText(R.string.followers).toString());
        if(type.equals(Constants.REQUEST_AUTH_USER)){
            fragmentsList.add(RecyclerFragment.newInstance(Constants.REQUEST_LIST_DETAIL_OF_AUTH_USER));
            fragmentsList.add(RecyclerFragment.newInstance(Constants.REQUEST_LIST_SHOTS_FOR_AUTH_USER));
            fragmentsList.add(RecyclerFragment.newInstance(Constants.REQUEST_LIST_FOLLOWERS_OF_AUTH_USER));
        }
        adapter.setFragments(fragmentsList, titlesList);
    }

}
