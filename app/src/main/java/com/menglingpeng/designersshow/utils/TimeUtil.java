package com.menglingpeng.designersshow.utils;

import android.util.Log;

import com.menglingpeng.designersshow.BaseActivity;
import com.menglingpeng.designersshow.BaseApplication;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.other.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mengdroid on 2017/10/29.
 */

public class TimeUtil {

    public static String getTimeDifference(String utcTime){
        String current, create;
        Date currentDate, createDate;
        long diff;
        String diffString = null;
        //当地时间格式
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);
        localFormat.setTimeZone(TimeZone.getDefault());
        try {
            current = localFormat.format(new Date());
            create = utcToLocal(utcTime);
            currentDate = localFormat.parse(current);
            createDate = localFormat.parse(create);
            diff = currentDate.getTime()-createDate.getTime();
            Log.i("long diff", String.valueOf(diff));
            diffString = diffToString(createDate, currentDate, diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diffString;
    }

    private static String utcToLocal(String utcTime){
        //当地时间格式
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);
        //UTC时间格式
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = utcFormat.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        localFormat.setTimeZone(TimeZone.getDefault());
        return localFormat.format(utcDate);
    }

    public static String diffToString(Date cretateDate ,Date currentDate, long diff){
        String differ;
        int day, hours, minutes, seconds;
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat yesterdayFormat = new SimpleDateFormat(" HH:mm");
        SimpleDateFormat format = new SimpleDateFormat(BaseApplication.getContext().getResources().getString(R.string.shots_create_at_time_days));
        day =currentDate.getDay() - cretateDate.getDay();
        hours =(int)diff/(1000*60*60);
        minutes = (int)diff/(1000*60);
        seconds = (int)diff/1000;
        switch (day){
            case 0:
                if(seconds < 60){
                    differ = BaseApplication.getContext().getResources().getString(R.string.shots_create_at_time_now);
                }else if(seconds > 60 && seconds <3600){
                    differ =  builder.append(String.valueOf(minutes)).append(BaseApplication.getContext().getResources().getString(R.string.shots_create_at_time_minutes)).toString();
                }else {
                    differ =  builder.append(String.valueOf(hours)).append(BaseApplication.getContext().getResources().getString(R.string.shots_create_at_time_hours)).toString();
                }
                break;
            case 1:
                differ = builder.append(BaseApplication.getContext().getResources().getString(R.string.shots_create_at_time_one_day)).append(yesterdayFormat.format(cretateDate)).toString();
                break;
            /*case 2:
                differ = BaseApplication.getContext().getResources().getString(R.string.shots_create_at_time_two_days);
                break;*/
            default:
                differ = format.format(cretateDate);
                break;
        }
        return differ;
    }

}
