package com.menglingpeng.designersshow.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.menglingpeng.designersshow.R;

import java.io.File;
import java.security.MessageDigest;

import static com.bumptech.glide.request.target.Target.*;
import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by mengdroid on 2017/10/20.
 */

public class ImageLoader {

    public static void load(Fragment fragment, String  url, ImageView imageView, boolean isFirst){
        RequestOptions requestOptions = new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);
        RequestBuilder<Bitmap> requestBuilder = Glide.with(fragment).asBitmap();
        //第一次加载GIF图片时不播放
        if(isFirst){
            requestBuilder.apply(requestOptions);
            requestBuilder.load(url).into(imageView);
        }else {
            Glide.with(fragment)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    public static void loadCricleImage(Fragment fragment, String url, ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment)
                .load(url)
                .apply(requestOptions)
                .into(imageView);

    }

    public static void downloadImage(Context context, CoordinatorLayout coordinatorLayout, String url, String imageName){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        /*// 设置允许在何种网络下进行下载任务,默认所有网络都允许
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);*/
        //设置通知栏如何显示下载通知（进度）,此参数表示下载通知在下载过程和下载完成后一直存在，直到点击或者消除通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置通知基本信息
        request.setTitle(imageName);
        //设置下载文件的保存位置为系统相册
        File appDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DesignersShow");
        if(!appDir.exists()){
            appDir.mkdirs();
        }
        request.setDestinationInExternalFilesDir(context, appDir.getAbsolutePath(), imageName);
        //获取DownloadManager实例
        DownloadManager dm = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求添加到下载队列，返回一个下载ID
        long downloadId = dm.enqueue(request);
        //根据ID过滤下载结果
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = dm.query(query);
        if(cursor != null && cursor.moveToFirst()){
            //下载请求的状态
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));
            //下载文件在本地的路径
            String localFilePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            switch (status){
                //准备下载
                case DownloadManager.STATUS_PENDING:
                    String text = new StringBuilder().append(context.getResources().getString(R.string.detail_toolbar_overflow_menu_download_snackbar_text)).append(imageName).toString();
                    SnackUI.showSnackShort(coordinatorLayout, text);
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_PAUSED:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //通知系统图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(localFilePath)));
                    break;
                case DownloadManager.STATUS_FAILED:
                    break;
            }
        }
        if(cursor != null){
            cursor.close();
        }

    }
}

