package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.menglingpeng.designersshow.R;

/**
 * Created by mengdroid on 2017/12/19.
 */

public class AdvancedSettingsPreference extends Preference {

    private View contentView;
    private RelativeLayout nightModeRl;
    private Switch nightModlSwitch;
    private View.OnClickListener listener;

    public AdvancedSettingsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.pref_advanced_settings,
                parent, false);
        return contentView;
    }

    @Override
    protected void onBindView(View view) {
        nightModeRl = (RelativeLayout)view.findViewById(R.id.settings_switch_preference_night_mode_rl);
        nightModlSwitch = (Switch)view.findViewById(R.id.settings_switch_preference_night_mode_switch);
        nightModeRl.setOnClickListener(listener);
    }

    public void setOnclickListerner(View.OnClickListener listerner){
        this.listener = listerner;
    }

    public RelativeLayout getNightModeRl(){
        return nightModeRl;
    }

    public Switch getNightModlSwitch(){
        return nightModlSwitch;
    }
}
