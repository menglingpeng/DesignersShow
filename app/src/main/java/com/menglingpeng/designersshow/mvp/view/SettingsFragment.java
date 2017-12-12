package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

/**
 * Created by mengdroid on 2017/12/10.
 */

public class SettingsFragment extends PreferenceFragment {
    private Preference dataPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.pref_settings);
        initPreference();
    }


    private void initPreference(){
        dataPreference = (DataPreference)findPreference(Constants.SETTINS_DATA_PREF);
        dataPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
    }
}
