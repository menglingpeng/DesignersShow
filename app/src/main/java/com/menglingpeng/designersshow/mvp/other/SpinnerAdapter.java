package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by mengdroid on 2017/11/9.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] stringArray;

    public SpinnerAdapter(@NonNull Context context, int resourceId, String[] stringArray) {
        super(context, resourceId);
        this.context = context;
        this.stringArray = stringArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
        tv.setText(stringArray[position]);
        tv.setTextSize(18f);
        tv.setTextColor(Color.WHITE);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
        tv.setText(stringArray[position]);
        tv.setTextSize(18f);
        tv.setTextColor(Color.WHITE);
        return convertView;
    }
}
