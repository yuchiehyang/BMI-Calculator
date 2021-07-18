package com.example.finalproject0623;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import android.util.Log;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

//CHECKBOX: PreferenceFragment 1
public class SettingPref extends PreferenceFragmentCompat {
    private final String TAG = "chloe";
    private static final String KEYNotifyChanged = "currentchange",KEY = "currentPage";
    private Integer mCurrentPage=0;
    private Boolean open=true;



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_pref_frag,rootKey);

        SwitchPreference switchNotice = (SwitchPreference) findPreference("noticeOn");

        //CHECKBOX:旋轉儲存資料
        if (savedInstanceState != null) {
            open = savedInstanceState.getBoolean(KEYNotifyChanged);
            switchNotice.setChecked(open);
        }
        //點擊即可回到上一頁(如果按上面上一頁的按鈕會回到初始頁)
        CheckBoxPreference backToHomePage = (CheckBoxPreference)findPreference("backToHomePage");
        backToHomePage.setChecked(false);
        backToHomePage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getContext(), WholeAppActivity.class));
                return true;
            }
        });


        //CHECKBOX: 系統通知(有特別設定如果開啟通知則會根據 WholeAppActivity 定的時間傳訊息)
        Preference myPref2 = (Preference)findPreference("noticeOn");
        myPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switchNotice.isChecked()==false) {
                    @SuppressLint("RestrictedApi") NotificationManager manager = (NotificationManager) getApplicationContext()
                            .getSystemService(NOTIFICATION_SERVICE);
                    //clear notification
                    manager.cancelAll();
                    mCurrentPage+=1;
                    open = false;

                    Log.v(TAG,"MESSAGE,CANCEL");

                    return true;

                } else if(switchNotice.isEnabled()==true)  {
                    mCurrentPage+=1;
                    open = true;

                    Log.v(TAG,"MESSAGE RESTORE");

                    return true;

                }
                return false;
            }
        });

    }
    //CHECKBOX:旋轉儲存資料
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, mCurrentPage);
        outState.putBoolean(KEYNotifyChanged, open);
    }
}