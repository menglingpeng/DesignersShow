package com.menglingpeng.designersshow.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by mengdroid on 2017/10/20.
 */

public class ImageLoader {

    public static void load(Context context, String  url, ImageView imageView){
        Glide.with(context)
             .load(url)
             .into(imageView);
    }
}
