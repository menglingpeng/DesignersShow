package com.menglingpeng.designersshow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public abstract class BaseActivity extends AppCompatActivity {

    protected int layoutId = R.layout.activity_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initLayoutId();
        super.onCreate(savedInstanceState);
        initViews();
    }

    protected abstract void initLayoutId();

    protected void initViews() {
        setContentView(layoutId);
    }

    /**
     * 切换Fragment，同时传递Type达到复用的目的。
     */
    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
