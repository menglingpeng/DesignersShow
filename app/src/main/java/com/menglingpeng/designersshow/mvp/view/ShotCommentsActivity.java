package com.menglingpeng.designersshow.mvp.view;


import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPreUtil;
import com.menglingpeng.designersshow.utils.SnackUI;

import java.util.HashMap;

public class ShotCommentsActivity extends BaseActivity implements RecyclerView{

    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private String id;
    private String title;
    private String type;
    private static String shotUserName;
    private ImageView avatarIv;
    private EditText editText;
    private FloatingActionButton floatingActionButton;
    private static RecyclerFragment fragment;
    private Context context;
    private AlertDialog dialog;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_comments;
    }

    @Override
    protected void initViews() {
        super.initViews();
        context = getApplicationContext();
        shotUserName = getIntent().getStringExtra(Constants.USER_NAME);
        title = new StringBuilder().append(getString(R.string.detail_comments_tv_text)).append("（")
                .append(String.valueOf(getIntent().getStringExtra(Constants.COMMENTS_COUNT))).append("）").toString();
        toolbar = (Toolbar) findViewById(R.id.comments_tb);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.comments_cdl);
        avatarIv = (ImageView)findViewById(R.id.add_comment_avatar_iv);
        editText = (EditText)findViewById(R.id.add_comment_et);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.add_comment_fab);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShotCommentsActivity.this.finish();
            }
        });
        id = getIntent().getStringExtra(Constants.SHOT_ID).toString();
        fragment = RecyclerFragment.newInstance(id, Constants.REQUEST_LIST_COMMENTS_FOR_A_SHOT);
        replaceFragment(fragment);
        ImageLoader.loadCricleImage(context, SharedPreUtil.getLoginData(Constants.AUTH_USER_AVATAR_URL),
                avatarIv);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if(!editText.getText().toString().equals("")){
                    //强制隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    showPostCommentProgressDialog();
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constants.ID, id);
                    map.put(Constants.BODY, editText.getText().toString());
                    map.put(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
                    type = Constants.REQUEST_CREATE_A_COMMENT;
                    RecyclerPresenter presenter = new RecyclerPresenter(ShotCommentsActivity.this, type,
                            Constants.REQUEST_NORMAL, Constants.REQUEST_POST_MEIHOD, map, context);
                    presenter.loadJson();
                }
            }
        });
    }

    private void showPostCommentProgressDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_post_comment, null);
        builder.setView(dialogView);
         dialog = builder.create();
         dialog.show();
        WindowManager wm = getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    public static RecyclerFragment getFragment() {
        return fragment;
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void loadFailed(String msg) {

    }

    @Override
    public void loadSuccess(String json, String requestType) {
        switch (type){
            case Constants.REQUEST_CREATE_A_COMMENT:
                if(json.equals(Constants.CODE_403_FORBIDDEN)){
                    dialog.dismiss();
                    SnackUI.showErrorSnackShort(getApplicationContext(), coordinatorLayout, getString(R.string.add_your_comment_403_forbidden_error_text));
                }
                break;
            default:
                break;
        }
    }

    public static String getShotUserName(){
        return shotUserName;
    }
}
