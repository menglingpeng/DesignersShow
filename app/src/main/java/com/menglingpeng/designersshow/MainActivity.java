package com.menglingpeng.designersshow;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.menglingpeng.designersshow.mvp.view.HomeFragment;
import com.menglingpeng.designersshow.mvp.view.RecyclerFragment;
import com.menglingpeng.designersshow.utils.SnackUI;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String currentType;
    private Toolbar toolbar;
    private String toolbarTitle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean backPressed;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        replace(HomeFragment.MENU_HOME);
        initToolbar();
        initNavigationView();

    }

    private void initToolbar(){
        switch (currentType){
            case HomeFragment.MENU_HOME:
                toolbarTitle = getString(R.string.app_name);
                break;
            case HomeFragment.MENU_EXPLORE:
                toolbarTitle = getString(R.string.nav_explore_menu);
                break;
            case RecyclerFragment.MENU_MY_LIKES:
                toolbarTitle = getString(R.string.toolbar_my_likes);
                break;
            case RecyclerFragment.MENU_MY_BUCKETS:
                toolbarTitle = getString(R.string.nav_my_buckets_menu);
                break;
            case RecyclerFragment.MENU_MY_SHOTS:
                toolbarTitle = getString(R.string.nav_my_shots_menu);
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
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.explore_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 动态更新Toolbar，在需要更新的地方调用invalidateOptionsMenu()。
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(HomeFragment.MENU_EXPLORE.equals(currentType)){
            menu.findItem(R.id.overflow_date).setVisible(true);
        }else {
            menu.findItem(R.id.overflow_date).setVisible(false);

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.overflow_date:
                break;
            case R.id.overflow_small:
                break;
            case R.id.overflow_small_without_infos:
                break;
            case R.id.overflow_large:
                break;
            case R.id.overflow_large_without_infos:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 切换Fragment，同时传递Type达到复用的目的。
     */
    private void replace(String type){
        if(!type.equals(currentType)){
            currentType = type;
            if(type != HomeFragment.MENU_HOME && type != HomeFragment.MENU_EXPLORE){
                replaceFragment(RecyclerFragment.newInstance(type), type);
            }else {
                replaceFragment(HomeFragment.newInstance(type), type);
            }
        }
    }

    private void initNavigationView(){
        //设置打开和关闭Drawer的特效
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        navigationView.inflateMenu(R.menu.nav_content_menu);
        navigationView.getMenu().getItem(0).setChecked(true);
        //修改NavigationView选中的Icon和Text颜色，默认是跟随主题颜色。
        ColorStateList csl = getResources().getColorStateList(R.color.navigationview_menu_item_color);
        navigationView.setItemIconTintList(csl);
        navigationView.setItemTextColor(csl);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                replace(HomeFragment.MENU_HOME);
                initToolbar();
                invalidateOptionsMenu();
                break;
            case R.id.nav_explore:
                replace(HomeFragment.MENU_EXPLORE);
                initToolbar();
                invalidateOptionsMenu();
                break;
            case R.id.nav_likes:
                replace(RecyclerFragment.MENU_MY_LIKES);
                initToolbar();
                invalidateOptionsMenu();
                break;
            case R.id.nav_buckets:
                replace(RecyclerFragment.MENU_MY_BUCKETS);
                initToolbar();
                invalidateOptionsMenu();
                break;
            case R.id.nav_shots:
                replace(RecyclerFragment.MENU_MY_SHOTS);
                initToolbar();
                invalidateOptionsMenu();
                break;
            case R.id.nav_settings:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            doubleBackToQuit();
        }
    }

    private void doubleBackToQuit(){
        if(backPressed){
          super.onBackPressed();
        }
        backPressed = true;
        SnackUI.showSnackShort(drawerLayout, R.string.double_back_quit);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }

}
