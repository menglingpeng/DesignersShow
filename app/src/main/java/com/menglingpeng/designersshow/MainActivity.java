package com.menglingpeng.designersshow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.menglingpeng.designersshow.mvp.model.AuthToken;
import com.menglingpeng.designersshow.mvp.model.User;
import com.menglingpeng.designersshow.mvp.other.TabPagerFragmentAdapter;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.mvp.view.LoginDialogFragment;
import com.menglingpeng.designersshow.mvp.view.SettingsActivity;
import com.menglingpeng.designersshow.mvp.view.UserProfileActivity;
import com.menglingpeng.designersshow.mvp.view.RecyclerFragment;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.SharedPrefUtil;
import com.menglingpeng.designersshow.utils.SnackUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, com
        .menglingpeng.designersshow.mvp.interf.RecyclerView ,LoginDialogFragment.LoginDialogListener{

    private String currentType;
    private String type;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private String toolbarTitle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RelativeLayout navHeaderRl;
    private ImageView navAvatarIm;
    private TextView navNameTx, navDescTx;
    private Button loginDialogLoginBt;
    private ProgressBar loginDialogPb;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout exploreLl;
    private List<RecyclerFragment> fragments;
    private TabPagerFragmentAdapter adapter;
    private Spinner sortSpinner, listSpinner;
    private Dialog loginDialog;
    private static RecyclerFragment currentFragment = null;
    private Boolean isLogin;
    private Boolean backPressed = false;
    private static final int SMOOTHSCROLL_TOP_POSITION = 50;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_main;
        //先判断是否第一次启动应用
        if (SharedPrefUtil.getState(Constants.IS_FIRST_START)) {
            showLoginDialog();
            SharedPrefUtil.saveState(Constants.IS_FIRST_START, false);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        isLogin = SharedPrefUtil.getState(Constants.IS_LOGIN);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        exploreLl = (LinearLayout) findViewById(R.id.explore_ll);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.my_bucket_fab);
        currentType = Constants.MENU_HOME;
        initToolbar();
        initNavigationView();
        initTabPager();
    }

    private void initToolbar() {
        switch (currentType) {
            case Constants.MENU_HOME:
                toolbarTitle = getString(R.string.app_name);
                break;
            case Constants.MENU_EXPLORE:
                toolbarTitle = getString(R.string.nav_explore_menu);
                break;
            case Constants.MENU_MY_LIKES:
                toolbarTitle = getString(R.string.toolbar_my_likes);
                break;
            case Constants.MENU_MY_BUCKETS:
                toolbarTitle = getString(R.string.nav_my_buckets_menu);
                break;
            case Constants.MENU_MY_SHOTS:
                toolbarTitle = getString(R.string.nav_my_shots_menu);
                break;
            default:
                break;
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(toolbarTitle);
        if (null != toolbar) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 动态更新Toolbar，在需要更新的地方调用invalidateOptionsMenu()。
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Constants.MENU_EXPLORE.equals(currentType)) {
            menu.findItem(R.id.overflow_date).setVisible(true);
        } else if (Constants.MENU_MY_BUCKETS.equals(currentType)) {
            menu.findItem(R.id.overflow_date).setVisible(false);
            menu.findItem(R.id.overflow_item_type).setVisible(false);
        } else {
            menu.findItem(R.id.overflow_date).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void showCreateBucketDialog() {
        final TextInputEditText bucketNameEt, bucketDescEt;
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.create_a_bucket_dialog_message, null);
        builder.setTitle(R.string.create_a_bucket);
        builder.setView(dialogView);
        bucketNameEt = (TextInputEditText) dialogView.findViewById(R.id.bucket_name_tiet);
        bucketDescEt = (TextInputEditText) dialogView.findViewById(R.id.bucket_desc_tiet);
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
                if (name.equals("")) {
                    SnackUI.showSnackShort(getApplicationContext(), drawerLayout, getString(R.string
                            .the_name_of_bucket_is_not_null));
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constants.ACCESS_TOKEN, SharedPrefUtil.getAuthToken());
                    map.put(Constants.NAME, bucketNameEt.getText().toString());
                    map.put(Constants.DESCRIPTION, bucketDescEt.getText().toString());
                    type = Constants.REQUEST_CREATE_A_BUCKET;
                    RecyclerPresenter presenter = new RecyclerPresenter(MainActivity.this, type, Constants
                            .REQUEST_NORMAL, Constants.REQUEST_POST_MEIHOD, map, MainActivity.this);
                    presenter.loadJson();
                    SnackUI.showSnackShort(getApplicationContext(), drawerLayout, getResources().getString(R.string
                            .snack_create_a_bucket_text));
                }

            }
        });
        bucketNameEt.setFocusable(true);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.now:
                replaceFragment(newFragment(Constants.REQUEST_TIMEFRAME_NOW));
                break;
            case R.id.week:
                replaceFragment(newFragment(Constants.REQUEST_TIMEFRAME_WEEK));
                break;
            case R.id.month:
                replaceFragment(newFragment(Constants.REQUEST_TIMEFRAME_WEEK));
                break;
            case R.id.year:
                replaceFragment(newFragment(Constants.REQUEST_TIMEFRAME_YEAR));
                break;
            case R.id.allTime:
                replaceFragment(newFragment(Constants.REQUEST_TIMEFRAME_EVER));
                break;
            case R.id.overflow_small:
                break;
            case R.id.overflow_small_without_infos:
                break;
            case R.id.overflow_large:
                break;
            case R.id.overflow_large_without_infos:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    private void initNavigationView() {
        View headerView = navigationView.getHeaderView(0);
        navHeaderRl = (RelativeLayout) headerView.findViewById(R.id.nav_view_header_rl);
        navAvatarIm = (ImageView) headerView.findViewById(R.id.login_avatar_im);
        navNameTx = (TextView) headerView.findViewById(R.id.nav_view_name_tx);
        navDescTx = (TextView) headerView.findViewById(R.id.nav_view_desc_tx);
        if (isLogin) {
            ImageLoader.loadCricleImage(getApplicationContext(), SharedPrefUtil.getLoginData(Constants
                    .AUTH_USER_AVATAR_URL), navAvatarIm);
            navNameTx.setText(SharedPrefUtil.getLoginData(Constants.AUTH_USER_NAME));
            navDescTx.setText(getResources().getString(R.string.nav_view_login_desc_tx));
        } else {
            navAvatarIm.setImageResource(R.drawable.ic_avatar);
            navNameTx.setText(getResources().getString(R.string.nav_view_name_tx));
            navDescTx.setText(getResources().getString(R.string.nav_view_desc_tx));
        }
        navHeaderRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                    intent.putExtra(Constants.TYPE, Constants.REQUEST_AUTH_USER);
                    startActivity(intent);
                } else {
                    showLoginDialog();
                }
            }
        });
        //设置打开和关闭Drawer的特效
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string
                .nav_drawer_open, R.string.nav_drawer_close);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        navigationView.inflateMenu(R.menu.nav_content_menu);
        navigationView.getMenu().getItem(0).setChecked(true);
        //API 23.0.0 method
        //navigationView.setCheckedItem(R.id.nav_home);
        //修改NavigationView选中的Icon和Text颜色，默认是跟随主题颜色。
        ColorStateList csl = getResources().getColorStateList(R.color.navigationview_menu_item_color);
        navigationView.setItemIconTintList(csl);
        navigationView.setItemTextColor(csl);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 登陆的中转页面
     */
    private void showLoginDialog() {
        LoginDialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), Constants.LOGIN_DIALOG_FRAGMENT_TAG);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                currentType = Constants.MENU_HOME;
                initSelectedNavigationItemView(currentType);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_explore:
                currentType = Constants.MENU_EXPLORE;
                initSelectedNavigationItemView(currentType);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                //后面的MenuItem需要登陆
                if (!isLogin) {
                    for (int i = 2; i < 6; i++) {
                        navigationView.getMenu().getItem(i).setCheckable(false);
                    }
                    showLoginDialog();
                } else {
                    switch (item.getItemId()) {
                        case R.id.nav_likes:
                            currentType = Constants.MENU_MY_LIKES;
                            break;
                        case R.id.nav_buckets:
                            currentType = Constants.MENU_MY_BUCKETS;
                            break;
                        case R.id.nav_shots:
                            currentType = Constants.MENU_MY_SHOTS;
                            break;
                        case R.id.nav_settings:
                            currentType = Constants.MENU_SETTING;
                            break;
                        default:
                            break;
                    }
                    initSelectedNavigationItemView(currentType);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
        }
        return true;
    }

    private void initSelectedNavigationItemView(String menuType) {
        initToolbar();
        invalidateOptionsMenu();
        switch (menuType) {
            case Constants.MENU_HOME:
                floatingActionButton.setVisibility(FloatingActionButton.GONE);
                exploreLl.setVisibility(LinearLayout.GONE);
                if (currentFragment != null) {
                    removeCurrentFragment(currentFragment);
                }
                initTabPager();
                break;
            case Constants.MENU_EXPLORE:
                floatingActionButton.setVisibility(FloatingActionButton.GONE);
                tabLayout.setVisibility(TabLayout.GONE);
                viewPager.setVisibility(ViewPager.GONE);
                SharedPrefUtil.deletedParameters();
                initSpinner();
                break;
            case Constants.MENU_MY_BUCKETS:
                floatingActionButton.setVisibility(FloatingActionButton.VISIBLE);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCreateBucketDialog();
                    }
                });
                tabLayout.setVisibility(TabLayout.GONE);
                viewPager.setVisibility(ViewPager.GONE);
                exploreLl.setVisibility(LinearLayout.GONE);
                replaceFragment(newFragment(menuType));
                break;
            case Constants.MENU_SETTING:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                floatingActionButton.setVisibility(FloatingActionButton.GONE);
                tabLayout.setVisibility(TabLayout.GONE);
                viewPager.setVisibility(ViewPager.GONE);
                exploreLl.setVisibility(LinearLayout.GONE);
                replaceFragment(newFragment(menuType));
                break;
        }
    }

    private void initTabPager() {
        tabLayout.setVisibility(TabLayout.VISIBLE);
        viewPager.setVisibility(ViewPager.VISIBLE);
        fragments = new ArrayList<>();
        adapter = new TabPagerFragmentAdapter(getSupportFragmentManager());
        initFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                scrollToTop(fragments.get(tab.getPosition()).getRecyclerView());
            }
        });
    }

    private void initFragments() {
        List<String> titles = new ArrayList<>();
        if (isLogin) {
            titles.add(getString(R.string.home_following));
            titles.add(getString(R.string.home_popular));
            titles.add(getString(R.string.home_recent));
            fragments.add(new RecyclerFragment().newInstance(Constants.TAB_FOLLOWING));
            fragments.add(new RecyclerFragment().newInstance(Constants.TAB_POPULAR));
            fragments.add(new RecyclerFragment().newInstance(Constants.TAB_RECENT));

        } else {
            titles.add(getString(R.string.home_popular));
            titles.add(getString(R.string.home_recent));
            fragments.add(new RecyclerFragment().newInstance(Constants.TAB_POPULAR));
            fragments.add(new RecyclerFragment().newInstance(Constants.TAB_RECENT));
        }
        adapter.setFragments(fragments, titles);
    }

    private void scrollToTop(RecyclerView list) {
        int lastPosition;
        if (null != list) {
            LinearLayoutManager manager = (LinearLayoutManager) list.getLayoutManager();
            lastPosition = manager.findLastVisibleItemPosition();
            if (lastPosition < SMOOTHSCROLL_TOP_POSITION) {
                list.smoothScrollToPosition(0);
            } else {
                list.scrollToPosition(0);
            }
        }
    }

    private void initSpinner() {
        exploreLl.setVisibility(LinearLayout.VISIBLE);
        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        listSpinner = (Spinner) findViewById(R.id.list_spinner);
        ArrayAdapter<String> sortAdapter, listAdapter;
        String[] sortArray = getResources().getStringArray(R.array.sort);
        String[] listArray = getResources().getStringArray(R.array.list);
        sortAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_text, sortArray);
        listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_text, listArray);
        listAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        sortAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        listSpinner.setAdapter(listAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        replaceFragment(newFragment(Constants.REQUEST_SORT_POPULAR));
                        break;
                    case 1:
                        replaceFragment(newFragment(Constants.REQUEST_SORT_COMMENTS));
                        break;
                    case 2:
                        replaceFragment(newFragment(Constants.REQUEST_SORT_RECENT));
                        break;
                    case 3:
                        replaceFragment(newFragment(Constants.REQUEST_SORT_VIEWS));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        replaceFragment(newFragment(Constants.REQUEST_LIST_SHOTS));
                        break;
                    case 1:
                        replaceFragment(newFragment(Constants.REQUEST_LIST_ANIMTED));
                        break;
                    case 2:
                        replaceFragment(newFragment(Constants.REQUEST_LIST_ATTACHMENTS));
                        break;
                    case 3:
                        replaceFragment(newFragment(Constants.REQUEST_LIST_DEBUTS));
                        break;
                    case 4:
                        replaceFragment(newFragment(Constants.REQUEST_LIST_PLAYOFFS));
                        break;
                    case 5:
                        replaceFragment(newFragment(Constants.REQUEST_LIST_REBOUNDS));
                        break;
                    case 6:
                        replaceFragment(newFragment(Constants.REQUEST_LIST_TEAM));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private RecyclerFragment newFragment(String type) {
        currentFragment = RecyclerFragment.newInstance(type);
        return currentFragment;
    }

    public static RecyclerFragment getCurrentFragment() {
        return currentFragment;
    }

    public void removeCurrentFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(SharedPrefUtil.getState(Constants.DOUBLE_BACK_TO_EXIT)) {
                doubleBackToQuit();
            }
        }
    }

    private void doubleBackToQuit() {
        if (backPressed) {
            super.onBackPressed();
        }
        backPressed = true;
        SnackUI.showSnackShort(getApplicationContext(), drawerLayout, getResources().getString(R.string
                .double_back_quit));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }

    /**
     * URL scheme方式启动MainActivity（SingleTask）时，调用此方法
     * 可以接收网页传递的数据
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        HashMap<String, String> map = new HashMap<>();
        loginDialogLoginBt.setVisibility(Button.GONE);
        loginDialogPb.setVisibility(ProgressBar.VISIBLE);
        Uri uri = intent.getData();
        //接收传递过来URL中的参数code
        String code = uri.getQuery();
        map.put(Constants.CLIENT_ID, Constants.CLIENT_ID_VALUE);
        map.put(Constants.CLIENT_SECRET, Constants.CLIENT_SECRET_VALUE);
        //code字符串前面带有code=,需要去掉。
        map.put(Constants.CODE, code.replace("code=", ""));
        type = Constants.REQUEST_AUTH_TOKEN;
        RecyclerPresenter presenter = new RecyclerPresenter(this, type, Constants.REQUEST_NORMAL, Constants
                .REQUEST_POST_MEIHOD, map, this);
        presenter.loadJson();
    }

    @Override
    protected void onDestroy() {
        SharedPrefUtil.deletedParameters();
        super.onDestroy();

    }

    @Override
    public void hideProgress() {
        loginDialogPb.setVisibility(ProgressBar.GONE);
    }

    @Override
    public void loadFailed(String msg) {

    }

    @Override
    public void loadSuccess(String json, String requestType) {
        HashMap<String, String> map = new HashMap<>();
        switch (type) {
            case Constants.REQUEST_AUTH_TOKEN:
                loginDialogPb.setVisibility(ProgressBar.VISIBLE);
                if (json.indexOf("error") != -1) {

                } else {
                    AuthToken authToken = Json.parseJson(json, AuthToken.class);
                    Log.i("authToken", authToken.getAccess_token());
                    map.put(Constants.ACCESS_TOKEN, authToken.getAccess_token());
                    SharedPrefUtil.saveParameters(map);
                    SharedPrefUtil.saveState(Constants.IS_LOGIN, true);
                    isLogin = true;
                    type = Constants.REQUEST_AUTH_USER;
                    RecyclerPresenter presenter = new RecyclerPresenter(this, type, Constants.REQUEST_NORMAL,
                            Constants.REQUEST_GET_MEIHOD, map, this);
                    presenter.loadJson();
                }
                break;
            case Constants.REQUEST_AUTH_USER:
                User authUser = Json.parseJson(json, User.class);
                ImageLoader.loadCricleImage(getApplicationContext(), authUser.getAvatar_url(), navAvatarIm);
                navNameTx.setText(authUser.getUsername());
                navDescTx.setText(getResources().getString(R.string.nav_view_login_desc_tx));
                map.put(Constants.AUTH_USER_AVATAR_URL, authUser.getAvatar_url());
                map.put(Constants.AUTH_USER_NAME, authUser.getUsername());
                map.put(Constants.AUTH_USER_ID, String.valueOf(authUser.getId()));
                SharedPrefUtil.saveParameters(map);
                loginDialog.cancel();
                initTabPager();
                break;
            case Constants.REQUEST_CREATE_A_BUCKET:
                replaceFragment(newFragment(Constants.MENU_MY_BUCKETS));
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoginDialogLoginListener(Button button, ProgressBar progressBar, Dialog dialog) {
        loginDialogPb = progressBar;
        loginDialogLoginBt = button;
        loginDialog = dialog;
    }

}
