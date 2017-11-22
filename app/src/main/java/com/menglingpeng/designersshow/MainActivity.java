package com.menglingpeng.designersshow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.menglingpeng.designersshow.mvp.other.TabPagerFragmentAdapter;
import com.menglingpeng.designersshow.mvp.view.RecyclerFragment;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.SharedPreUtil;
import com.menglingpeng.designersshow.utils.SnackUI;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String currentType;
    private Toolbar toolbar;
    private String toolbarTitle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RelativeLayout navHeaderRl;
    private ImageView navAvatarIm;
    private TextView navNameTx, navDescTx;
    private ImageView loginDialogCloseIm;
    private Button loginDialogLoginBt;
    private ProgressBar loginDialogPb;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout exploreLl;
    private List<RecyclerFragment> fragments;
    private TabPagerFragmentAdapter adapter;
    private Spinner sortSpinner, listSpinner;
    private static RecyclerFragment currentFragment = null;
    private Boolean backPressed;
    private static final int SMOOTHSCROLL_TOP_POSITION = 50;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_main;
        //先判断是否第一次启动应用
        if(SharedPreUtil.getIsFirstStart()){
            showLoginDialog();
            SharedPreUtil.saveIsFirstStart(false);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        exploreLl = (LinearLayout)findViewById(R.id.explore_ll);
        currentType = Constants.MENU_HOME;
        initToolbar();
        initNavigationView();
        initTabPager();
    }

    private void initToolbar(){
        switch (currentType){
            case Constants.MENU_HOME:
                toolbarTitle = getString(R.string.app_name);
                break;
            case Constants.MENU_EXPLORE:
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
        getMenuInflater().inflate(R.menu.explore_toolbar_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 动态更新Toolbar，在需要更新的地方调用invalidateOptionsMenu()。
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(Constants.MENU_EXPLORE.equals(currentType)){
            menu.findItem(R.id.overflow_date).setVisible(true);
        }else {
            menu.findItem(R.id.overflow_date).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.now:
                replaceFragment(newFragment(Constants.TIMEFRAME_NOW));
                break;
            case R.id.week:
                replaceFragment(newFragment(Constants.TIMEFRAME_WEEK));
                break;
            case R.id.month:
                replaceFragment(newFragment(Constants.TIMEFRAME_WEEK));
                break;
            case R.id.year:
                replaceFragment(newFragment(Constants.TIMEFRAME_YEAR));
                break;
            case R.id.allTime:
                replaceFragment(newFragment(Constants.TIMEFRAME_EVER));
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

    @SuppressLint("RestrictedApi")
    private void initNavigationView(){
        View headerView = navigationView.getHeaderView(0);
        navHeaderRl = (RelativeLayout)headerView.findViewById(R.id.nav_view_header_rl);
        navAvatarIm = (ImageView)headerView.findViewById(R.id.login_avatar_im);
        navNameTx = (TextView)headerView.findViewById(R.id.nav_view_desc_tx);
        navDescTx = (TextView)headerView.findViewById(R.id.nav_view_desc_tx);
        navHeaderRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin){

                }else {
                    showLoginDialog();
                }
            }
        });
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

    private void showLoginDialog(){
        final Dialog dialog = new Dialog(this, R.style.ThemeLoginDialog);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.LoginDialog);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.setContentView(dialogView);
        loginDialogCloseIm = (ImageView)dialogView.findViewById(R.id.dialog_login_close_im);
        loginDialogLoginBt = (Button)dialogView.findViewById(R.id.dialog_login_bt);
        loginDialogPb = (ProgressBar)dialogView.findViewById(R.id.dialog_login_pb);
        dialog.show();
        loginDialogCloseIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        loginDialogLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                currentType = Constants.MENU_HOME;
                break;
            case R.id.nav_explore:
                currentType = Constants.MENU_EXPLORE;
                break;
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
        }
        initSelectedNavigationItemView(currentType);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initSelectedNavigationItemView(String menuType){
        initToolbar();
        invalidateOptionsMenu();
        switch (menuType){
            case Constants.MENU_HOME:
                exploreLl.setVisibility(LinearLayout.GONE);
                if(currentFragment != null) {
                    removeCurrentFragment(currentFragment);
                }
                initTabPager();
                break;
            case Constants.MENU_EXPLORE:
                tabLayout.setVisibility(TabLayout.GONE);
                viewPager.setVisibility(ViewPager.GONE);
                SharedPreUtil.deletedParameters();
                initSpinner();
                break;
            case Constants.MENU_SETTING:
                break;
            default:
                tabLayout.setVisibility(TabLayout.GONE);
                viewPager.setVisibility(ViewPager.GONE);
                exploreLl.setVisibility(LinearLayout.GONE);
                replaceFragment(RecyclerFragment.newInstance(menuType));
                break;
        }
    }

    private void initTabPager(){
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
        if (SharedPreUtil.getIsLogin()) {
            titles.add(getString(R.string.home_following));
            titles.add(getString(R.string.home_popular));
            titles.add(getString(R.string.home_recent));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_FOLLOWING));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_POPULAR));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_RECENT));

        } else {
            titles.add(getString(R.string.home_popular));
            titles.add(getString(R.string.home_recent));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_POPULAR));
            fragments.add(new RecyclerFragment().newInstance(RecyclerFragment.TAB_RECENT));
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

    private void initSpinner(){
        exploreLl.setVisibility(LinearLayout.VISIBLE);
        sortSpinner = (Spinner)findViewById(R.id.sort_spinner);
        listSpinner = (Spinner)findViewById(R.id.list_spinner);
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
                switch (position){
                    case 0:
                        replaceFragment(newFragment(Constants.SORT_POPULAR));
                        break;
                    case 1:
                        replaceFragment(newFragment(Constants.SORT_COMMENTS));
                        break;
                    case 2:
                        replaceFragment(newFragment(Constants.SORT_RECENT));
                        break;
                    case 3:
                        replaceFragment(newFragment(Constants.SORT_VIEWS));
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
                switch (position){
                    case 0:
                        replaceFragment(newFragment(Constants.LIST_SHOTS));
                        break;
                    case 1:
                        replaceFragment(newFragment(Constants.LIST_ANIMTED));
                        break;
                    case 2:
                        replaceFragment(newFragment(Constants.LIST_ATTACHMENTS));
                        break;
                    case 3:
                        replaceFragment(newFragment(Constants.LIST_DEBUTS));
                        break;
                    case 4:
                        replaceFragment(newFragment(Constants.LIST_PLAYOFFS));
                        break;
                    case 5:
                        replaceFragment(newFragment(Constants.LIST_REBOUNDS));
                        break;
                    case 6:
                        replaceFragment(newFragment(Constants.LIST_TEAM));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private RecyclerFragment newFragment(String type){
        currentFragment = RecyclerFragment.newInstance(type);
        return currentFragment;
    }

    public static RecyclerFragment getCurrentFragment(){
        return currentFragment;
    }

    public void removeCurrentFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
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
        SnackUI.showSnackShort(drawerLayout, getResources().getString(R.string.double_back_quit));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        SharedPreUtil.deletedParameters();
        super.onDestroy();

    }
}
