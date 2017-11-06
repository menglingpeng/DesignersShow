package com.menglingpeng.designersshow.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.MainActivity;

/**
 * Created by mengdroid on 2017/10/13.
 */

public class SnackUI {

    public static void showSnackShort(View rootView, CharSequence text){
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackLong(View rootView, CharSequence text){
        Snackbar.make(rootView, text, Snackbar.LENGTH_LONG).show();
    }
}
