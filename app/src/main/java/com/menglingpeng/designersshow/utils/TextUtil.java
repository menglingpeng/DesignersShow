package com.menglingpeng.designersshow.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.menglingpeng.designersshow.R;

import java.time.format.TextStyle;

/**
 * Created by mengdroid on 2017/11/14.
 */

public class TextUtil {

    public static SpannableStringBuilder setBeforeBold(String beforeText, String afterText) {
        SpannableStringBuilder builder = new SpannableStringBuilder(beforeText);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, beforeText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.append(" ");
        builder.append(afterText);
        return builder;
    }

    public static SpannableStringBuilder setAfterBold(Context context, String beforeText, String afterText) {
        SpannableStringBuilder builder = new SpannableStringBuilder(afterText);
        builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.shots_username)), 0,
                afterText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.append(" ");
        builder.append(beforeText);
        return builder;
    }
}
