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
 * Created by mengdroid on 2017/12/11.
 */

public class DataPreference extends Preference {

    private View contentView;
    private RelativeLayout savingDataModeRl;
    private Switch savingDataModeSwitch;
    private RelativeLayout gifsAutoplayRl;
    private Switch gifsAutoplaySwitch;
    private RelativeLayout clearCacheRl;
    public DataPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.pref_catagory_data, parent, false);
        return contentView;
    }

    @Override
    protected void onBindView(View view) {
        savingDataModeRl = (RelativeLayout)view.findViewById(R.id.settings_switch_preference_data_saving_mode_rl);
        savingDataModeSwitch = (Switch)view.findViewById(R.id.settings_switch_preference_data_saving_mode_switch);
        gifsAutoplayRl = (RelativeLayout)view.findViewById(R.id.settings_switch_preference_gifs_autoplay_rl);
        gifsAutoplaySwitch = (Switch)view.findViewById(R.id.settings_switch_preference_gifs_autoplay_switch);
        clearCacheRl = (RelativeLayout)view.findViewById(R.id.settings_switch_preference_clear_cache_rl);
    }

    public RelativeLayout getSavingDataModeRl(){
        return savingDataModeRl;
    }

    public Switch getSavingDataModeSwitch(){
        return  savingDataModeSwitch;
    }

    public RelativeLayout getGifsAutoplayRl(){
        return gifsAutoplayRl;
    }

    public Switch getGifsAutoplaySwitch(){
        return gifsAutoplaySwitch;
    }

    public RelativeLayout getClearCacheRl(){
        return clearCacheRl;
    }
}
