package com.menglingpeng.designersshow;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

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

    public void replaceFragment(Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }
}
