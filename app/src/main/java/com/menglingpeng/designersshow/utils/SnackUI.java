package com.menglingpeng.designersshow.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.MainActivity;
import com.menglingpeng.designersshow.R;

/**
 * Created by mengdroid on 2017/10/13.
 */

public class SnackUI {

    public static void showSnackShort(Context context, View rootView, CharSequence text){
       Snackbar snackbar =  Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public static void showSnackLong(Context context, View rootView, CharSequence text){
        Snackbar snackbar =  Snackbar.make(rootView, text, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }
}
