package com.menglingpeng.designersshow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.menglingpeng.designersshow.mvp.view.HomeFragment;


public class MainActivity extends BaseActivity {

    private String currentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replace(HomeFragment.MENU_HOME);
    }

    private void replace(String type){
        if(!type.equals(currentType)){
            currentType = type;
            replaceFragment(HomeFragment.newInstance(type), type);
        }

    }


}
