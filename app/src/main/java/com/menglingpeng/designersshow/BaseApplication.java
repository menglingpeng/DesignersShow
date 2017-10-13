package com.menglingpeng.designersshow;

import android.app.Application;
import android.content.Context;

/**
 * Created by mengdroid on 2017/10/13.
 */

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
