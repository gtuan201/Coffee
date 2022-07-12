package com.example.coffee;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceCheckInstall {
    public static final String MY_SHARE_PREFERENCE = "MY_SHARE_PREFERENCE";
    private Context context;

    public SharePreferenceCheckInstall(Context context) {
        this.context = context;
    }
    public void putBooleanInstall(String key, boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE_PREFERENCE,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean getBooleanValue(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE_PREFERENCE,0);
        return sharedPreferences.getBoolean(key,false);
    }
}
