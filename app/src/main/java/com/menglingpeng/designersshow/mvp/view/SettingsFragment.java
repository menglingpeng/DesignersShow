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
import com.menglingpeng.designersshow.utils.SharedPreUtil;

/**
 * Created by mengdroid on 2017/12/10.
 */

public class SettingsFragment extends PreferenceFragment implements View.OnClickListener {
    private DataPreference dataPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.pref_settings);
        initPreference();
    }


    private void initPreference(){
        dataPreference = (DataPreference)findPreference(Constants.SETTINS_DATA_PREF);
        dataPreference.setOnclickListerner(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_switch_preference_data_saving_mode_rl:
                if(SharedPreUtil.getState(Constants.SAVING_LOWER_IMAGE)){
                    dataPreference.getSavingDataModeSwitch().setChecked(false);
                    SharedPreUtil.saveState(Constants.SAVING_LOWER_IMAGE, false);
                }else {
                    dataPreference.getSavingDataModeSwitch().setChecked(true);
                    SharedPreUtil.saveState(Constants.SAVING_LOWER_IMAGE, true);
                }
                break;
            case R.id.settings_switch_preference_gifs_autoplay_rl:
                break;
            case R.id.settings_switch_preference_clear_cache_rl:
                break;
            default:
                break;
        }
    }
}
