package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.model.User;
import com.menglingpeng.designersshow.mvp.other.TabPagerFragmentAdapter;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.SharedPreUtil;
import com.menglingpeng.designersshow.utils.SnackUI;
import com.menglingpeng.designersshow.utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileActivity extends BaseActivity implements RecyclerView {

    private String type;
    private User user = null;
    private String userId;
    private CoordinatorLayout profileCdl;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView profileBackgroundIv, profileAvatarIv;
    private TextView profileNameTv, profileDescTv;
    private RecyclerPresenter presenter;
    private ProgressBar progressBar;
    private TabLayout profileTl;
    private ViewPager profileVp;
    private Button followBt;
    private Button unfollowBt;
    private TabPagerFragmentAdapter adapter;
    private HashMap<String, String> map;
    private ArrayList<RecyclerFragment> fragmentsList;
    private Context context;
    private Boolean isFollowing;

    @Override
    protected void initLayoutId() {
        context = getApplicationContext();
        type = getIntent().getStringExtra(Constants.TYPE);
        userId = getIntent().getStringExtra(Constants.ID);
        map = new HashMap<>();
        map.put(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
        if (type.equals(Constants.REQUEST_SINGLE_USER)) {
            map.put(Constants.ID, userId);
            type = Constants.CHECK_IF_YOU_ARE_FOLLOWING_A_USER;
        }
        initData(Constants.REQUEST_GET_MEIHOD);
        layoutId = R.layout.activity_user_profile;
    }

    private void initData(String requestMethod){
        presenter = new RecyclerPresenter(UserProfileActivity.this, type, Constants.REQUEST_NORMAL,
                requestMethod, map, context);
        presenter.loadJson();
    }

    @Override
    protected void initViews() {
        super.initViews();
        progressBar = (ProgressBar) findViewById(R.id.profile_pb);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_toolbar_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(type.equals(Constants.REQUEST_SINGLE_USER)){
            menu.findItem(R.id.profile_logout).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (type.equals(Constants.REQUEST_AUTH_USER)) {
            if (item.getItemId() == R.id.profile_logout) {
                showLogoutDialog();
            } else {
                shareUserProfile();
            }
        } else {
            if (item.getItemId() == R.id.profile_share) {
                shareUserProfile();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
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
                logout();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        SharedPreUtil.saveState(Constants.IS_LOGIN, false);
        SharedPreUtil.deleteAuthToken();
        restartApplication();
    }

    private void restartApplication() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void shareUserProfile(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = new StringBuilder().append(getString(R.string.check_out)).append(user.getName()).append(
                getString(R.string.s)).append(getString(R.string.user_profile)).append(user.getHtml_url()).append("\n").
                append(getString(R.string.detail_toolbar_overflow_menu_share_footer_text)).toString();
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }


    @Override
    public void hideProgress() {
    }

    @Override
    public void loadFailed(String msg) {

    }

    @Override
    public void loadSuccess(String json, String requestType) {
        switch (type) {
            case Constants.CHECK_IF_YOU_ARE_FOLLOWING_A_USER:
                type = Constants.REQUEST_SINGLE_USER;
                invalidateOptionsMenu();
                initData(Constants.REQUEST_GET_MEIHOD);
                if (json.equals(Constants.CODE_404_NOT_FOUND)) {
                    isFollowing = false;
                } else {
                    isFollowing = true;
                }
                break;
            case Constants.REQUEST_UNFOLLOW_A_USER:
                if(json.equals(Constants.CODE_204_NO_CONTENT)){
                    unfollowBt.setVisibility(Button.GONE);
                    followBt.setVisibility(Button.VISIBLE);
                    SnackUI.showSnackShort(context, profileCdl, TextUtil.setAfterBold(context, getString(
                            R.string.unfollowed_successful), user.getName()));
                    followBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            type = Constants.REQUEST_FOLLOW_A_USER;
                            initData(Constants.REQUEST_PUT_MEIHOD);
                        }
                    });
                }else {
                    SnackUI.showErrorSnackShort(context, profileCdl, getString(R.string.unfollowed_error));
                }
                break;
            case Constants.REQUEST_FOLLOW_A_USER:
                if(json.equals(Constants.CODE_204_NO_CONTENT)){
                    unfollowBt.setVisibility(Button.VISIBLE);
                    followBt.setVisibility(Button.GONE);
                    SnackUI.showSnackShort(context, profileCdl, TextUtil.setAfterBold(context, getString(
                            R.string.followed_successful), user.getName()));
                    unfollowBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            type = Constants.REQUEST_UNFOLLOW_A_USER;
                            initData(Constants.REQUEST_DELETE_MEIHOD);
                        }
                    });
                }else {
                    SnackUI.showErrorSnackShort(context, profileCdl, getString(R.string.followed_error));
                }
                break;
            default:
                progressBar.setVisibility(ProgressBar.GONE);
                user = Json.parseJson(json, User.class);
                profileCdl = (CoordinatorLayout)findViewById(R.id.profile_cdl);
                collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_ctbl);
                collapsingToolbarLayout.setVisibility(CollapsingToolbarLayout.VISIBLE);
                /*collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);*/
                toolbar = (Toolbar) findViewById(R.id.profile_tb);
                profileNameTv = (TextView) findViewById(R.id.profile_name_tv);
                profileDescTv = (TextView) findViewById(R.id.profile_desc_tv);
                profileBackgroundIv = (ImageView) findViewById(R.id.profile_backgroud_iv);
                profileAvatarIv = (ImageView) findViewById(R.id.profile_avatar_iv);
                followBt = (Button) findViewById(R.id.profile_follow_bt);
                unfollowBt = (Button) findViewById(R.id.profile_unfollow_bt);
                if (type.equals(Constants.REQUEST_SINGLE_USER)) {
                    if (isFollowing) {
                        unfollowBt.setVisibility(Button.VISIBLE);
                        unfollowBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                type = Constants.REQUEST_UNFOLLOW_A_USER;
                                initData(Constants.REQUEST_DELETE_MEIHOD);
                            }
                        });
                    } else {
                        followBt.setVisibility(Button.VISIBLE);
                        followBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                type = Constants.REQUEST_FOLLOW_A_USER;
                                initData(Constants.REQUEST_PUT_MEIHOD);
                            }
                        });
                    }
                }
                profileTl = (TabLayout) findViewById(R.id.profile_tl);
                profileTl.setVisibility(TabLayout.VISIBLE);
                profileVp = (ViewPager) findViewById(R.id.profile_vp);
                fragmentsList = new ArrayList<>();
                setSupportActionBar(toolbar);
                //隐藏Toolbar的标题
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                toolbar.setNavigationIcon(R.drawable.ic_back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                profileNameTv.setText(user.getName());
                TextUtil.setHtmlText(profileDescTv, user.getBio());
                ImageLoader.loadBlurImage(getApplicationContext(), user.getAvatar_url(), profileBackgroundIv);
                ImageLoader.loadCricleImage(getApplicationContext(), user.getAvatar_url(), profileAvatarIv);
                profileBackgroundIv.setAlpha(100);
                initTabPager();
                break;
        }
    }

    private void initTabPager() {
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

    private void initTabFragments() {
        ArrayList<String> titlesList = new ArrayList<>();
        titlesList.add(getText(R.string.detail).toString());
        titlesList.add(getText(R.string.explore_spinner_list_shots).toString());
        titlesList.add(getText(R.string.followers).toString());
        if (type.equals(Constants.REQUEST_AUTH_USER)) {
            fragmentsList.add(RecyclerFragment.newInstance(user, Constants.REQUEST_LIST_DETAIL_FOR_AUTH_USER));
            fragmentsList.add(RecyclerFragment.newInstance(user, Constants.REQUEST_LIST_SHOTS_FOR_AUTH_USER));
            fragmentsList.add(RecyclerFragment.newInstance(Constants.REQUEST_LIST_FOLLOWERS_FOR_AUTH_USER));
        } else {
            fragmentsList.add(RecyclerFragment.newInstance(user, Constants.REQUEST_LIST_DETAIL_FOR_A_USER));
            fragmentsList.add(RecyclerFragment.newInstance(user, Constants.REQUEST_LIST_SHOTS_FOR_A_USER));
            fragmentsList.add(RecyclerFragment.newInstance(userId, Constants.REQUEST_LIST_FOLLOWERS_FOR_A_USER));
        }
        adapter.setFragments(fragmentsList, titlesList);
    }


}
