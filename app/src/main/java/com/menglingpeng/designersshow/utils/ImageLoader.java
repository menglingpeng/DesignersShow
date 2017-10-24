package com.menglingpeng.designersshow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.menglingpeng.designersshow.R;

import java.security.MessageDigest;

/**
 * Created by mengdroid on 2017/10/20.
 */

public class ImageLoader {

    public static void load(Fragment fragment, String  url, ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.empty).diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(fragment)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadCricleImage(Fragment fragment, String url, ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().transform(new GlideCircleTransform());
        Glide.with(fragment)
                .load(url)
                .apply(requestOptions)
                .into(imageView);

    }

    public static class GlideCircleTransform extends BitmapTransformation{

        public GlideCircleTransform() {
            super();
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return null;
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }
}

