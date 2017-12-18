package com.menglingpeng.designersshow.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.menglingpeng.designersshow.R;

/**
 * Created by mengdroid on 2017/12/18.
 */

public class ShareAndOpenInBrowserUtil {

    public static void share(Context context, String shareText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string
                .detail_toolbar_overflow_menu_share_create_chooser_title)));
    }

    public static void openInBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string
                    .detail_toolbar_overflow_menu_open_in_browser_create_chooser_title)));
        }
    }
}
