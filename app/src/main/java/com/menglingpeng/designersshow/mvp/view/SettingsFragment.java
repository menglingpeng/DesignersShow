package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPrefUtil;
import com.menglingpeng.designersshow.utils.SnackUI;

/**
 * Created by mengdroid on 2017/12/10.
 */

public class SettingsFragment extends PreferenceFragment implements View.OnClickListener {
    private AdvancedSettingsPreference advancedSettingsPreference;
    private DataPreference dataPreference;
    private AboutPreference aboutPreference;
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        context = getActivity().getApplicationContext();
        coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.settings_cdl);
        initPreference(getPreferenceManager());
    }


    private void initPreference(PreferenceManager manager){
        advancedSettingsPreference = (AdvancedSettingsPreference)manager.findPreference(Constants.
                SETTINGS_ADVANCED_SETTINGS_PREF);
        dataPreference = (DataPreference) manager.findPreference(Constants.SETTINGS_DATA_PREF);
        aboutPreference = (AboutPreference)manager.findPreference(Constants.SETTINGS_ABOUT_PREF);
        dataPreference.setOnclickListerner(this);
        aboutPreference.setOnclickListerner(this);
        advancedSettingsPreference.setOnclickListerner(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_switch_preference_data_saving_mode_rl:
                if(SharedPrefUtil.getState(Constants.SAVING_LOWER_IMAGE)){
                    dataPreference.getSavingDataModeSwitch().setChecked(false);
                    SharedPrefUtil.saveState(Constants.SAVING_LOWER_IMAGE, false);
                }else {
                    dataPreference.getSavingDataModeSwitch().setChecked(true);
                    SharedPrefUtil.saveState(Constants.SAVING_LOWER_IMAGE, true);
                }
                break;
            case R.id.settings_switch_preference_gifs_autoplay_rl:
                if(SharedPrefUtil.getState(Constants.GIFS_AUTO_PLAY)){
                    dataPreference.getGifsAutoplaySwitch().setChecked(false);
                    SharedPrefUtil.saveState(Constants.GIFS_AUTO_PLAY, false);
                }else {
                    dataPreference.getGifsAutoplaySwitch().setChecked(true);
                    SharedPrefUtil.saveState(Constants.GIFS_AUTO_PLAY, true);
                }
                break;
            case R.id.settings_switch_preference_clear_cache_rl:
                new ClearDiskCacheTask().execute();
                break;
            case R.id.settings_preference_contact_me_rl:
                sendMailToMe();
                break;
            case R.id.settings_switch_preference_night_mode_rl:
                setNightMode();
                break;
            case R.id.settings_switch_preference_double_press_to_exit_rl:
                setDoubleBackToExit();
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

    private void sendMailToMe(){
        String versionCode = null;
        Uri uri = Uri.parse(Constants.MAIL_TO_URL);
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String emailText = new StringBuilder().append("-------------").append("\n")
                .append("DEBUG INFO:").append("\n")
                .append("Android version:").append(Build.VERSION.RELEASE).append("\n")
                .append("App version:").append(versionCode).append("\n")
                .append("Manufacturer:").append(Build.MANUFACTURER).append("\n")
                .append("Brand:").append(Build.BOARD).append("\n")
                .append("Device model:").append(Build.MODEL).append("\n")
                .append("-------------").toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, Constants.EMAIL_CC);
        intent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, emailText).toString();
        startActivity(Intent.createChooser(intent, getString(R.string.email_create_chooser_title)));
    }

    private void setNightMode() {
        if (SharedPrefUtil.getState(Constants.NIGHT_MODE)) {
            SharedPrefUtil.saveState(Constants.NIGHT_MODE, false);
            advancedSettingsPreference.getNightModlSwitch().setChecked(false);
            coordinatorLayout.setBackgroundColor(Color.WHITE);
        } else{
            SharedPrefUtil.saveState(Constants.NIGHT_MODE, true);
            advancedSettingsPreference.getNightModlSwitch().setChecked(true);
            coordinatorLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
    }
    }

    private void setDoubleBackToExit(){
        if(SharedPrefUtil.getState(Constants.DOUBLE_BACK_TO_EXIT)){
            advancedSettingsPreference.getDoubleBacktoExitSwitch().setChecked(false);
            SharedPrefUtil.saveState(Constants.DOUBLE_BACK_TO_EXIT, false);
        }else {
            advancedSettingsPreference.getDoubleBacktoExitSwitch().setChecked(true);
            SharedPrefUtil.saveState(Constants.DOUBLE_BACK_TO_EXIT, true);
        }
    }
}
