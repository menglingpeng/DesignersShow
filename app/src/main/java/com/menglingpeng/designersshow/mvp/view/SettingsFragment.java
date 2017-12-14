package com.menglingpeng.designersshow.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
            case R.id.settings_preference_contact_me_rl:
                sendMailToMe();
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
}
