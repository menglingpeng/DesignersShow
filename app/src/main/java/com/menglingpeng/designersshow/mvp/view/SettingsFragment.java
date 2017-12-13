package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPreUtil;
import com.menglingpeng.designersshow.utils.SnackUI;

/**
 * Created by mengdroid on 2017/12/10.
 */

public class SettingsFragment extends PreferenceFragment implements View.OnClickListener {
    private DataPreference dataPreference;
    private AboutPreference aboutPreference;
    private Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        context = getActivity().getApplicationContext();
        initPreference();
    }


    private void initPreference(){
        dataPreference = (DataPreference) findPreference(Constants.SETTINGS_DATA_PREF);
        aboutPreference = (AboutPreference)findPreference(Constants.SETTINGS_ABOUT_PREF);
        dataPreference.setOnclickListerner(this);
        aboutPreference.setOnclickListerner(this);
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
                if(SharedPreUtil.getState(Constants.GIFS_AUTO_PLAY)){
                    dataPreference.getGifsAutoplaySwitch().setChecked(false);
                    SharedPreUtil.saveState(Constants.GIFS_AUTO_PLAY, false);
                }else {
                    dataPreference.getGifsAutoplaySwitch().setChecked(true);
                    SharedPreUtil.saveState(Constants.GIFS_AUTO_PLAY, true);
                }
                break;
            case R.id.settings_switch_preference_clear_cache_rl:
                new ClearDiskCacheTask().execute();
                break;
            default:
                break;
        }
    }

    private class ClearDiskCacheTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            ImageLoader.clearDiskCache(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SnackUI.showSnackShort(context, getActivity().findViewById(R.id.settings_cdl), getString(R.string.cache_successfully_cleared));
        }
    }
}
