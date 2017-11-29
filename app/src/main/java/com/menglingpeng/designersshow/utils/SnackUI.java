package com.menglingpeng.designersshow.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.MainActivity;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;

import java.util.HashMap;

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

    public static Snackbar showAddShotToBucketsActionSnack(final Context context, View rootView, CharSequence text, final String type,final String shotId,  final HashMap<String, String> map, final RecyclerView recyclerView){
        Snackbar snackbar = Snackbar.make(rootView, text, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        snackbar.setAction(context.getResources().getString(R.string.OK), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String key : map.keySet()) {
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put(Constants.SHOT_ID, shotId);
                    map1.put(Constants.ACCESS_TOKEN, SharedPreUtil.getAuthToken());
                    map1.put(Constants.BUCKET_ID, map.get(key));
                    RecyclerPresenter presenter = new RecyclerPresenter(recyclerView, type, Constants.REQUEST_ADD_A_SHOT_TO_BUCKET, Constants.REQUEST_PUT_MEIHOD, map1, context);
                    presenter.loadJson();
                }

            }
        });
            return snackbar;
    }

}
