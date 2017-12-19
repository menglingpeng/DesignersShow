package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menglingpeng.designersshow.R;

/**
 * Created by mengdroid on 2017/12/19.
 */

public class AdvancedSettingsPreference extends Preference {

    private View contentView;

    public AdvancedSettingsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.pref_advanced_settings,
                parent, false);
        return contentView;
    }

}
