package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.menglingpeng.designersshow.R;

/**
 * Created by mengdroid on 2017/12/11.
 */

public class AboutPreference  extends Preference{

    private View contentView;
    private RelativeLayout aboutAppRl;
    private RelativeLayout contactMeRl;
    private View.OnClickListener listener;

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.pref_category_about,
                parent, false);
        return contentView;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        aboutAppRl = (RelativeLayout)view.findViewById(R.id.settings_preference_about_app_rl);
        contactMeRl = (RelativeLayout)view.findViewById(R.id.settings_preference_contact_me_rl);
        aboutAppRl.setOnClickListener(listener);
        contactMeRl.setOnClickListener(listener);
    }

    public void setOnclickListerner(View.OnClickListener listerner){
        this.listener = listerner;
    }

    public RelativeLayout getAboutAppRl(){
        return  aboutAppRl;
    }

    public RelativeLayout getContactMeRl(){
        return contactMeRl;
    }
}
