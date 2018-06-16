package com.example.laboratorytwoau.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static final String STORAGE_TITLE = "com.example.itacademy.au.kg.";
    private static final String PROF_NAME = "com.example.itacademy.au.kg.profession.name";
    private SharedPreferences mPreferences;

    public PreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences(STORAGE_TITLE, Context.MODE_PRIVATE);
    }

    public void saveProfessionName(String name) {
        mPreferences.edit().putString(PROF_NAME, name).apply();
    }

    public String getProfessionName() {
        return mPreferences.getString(PROF_NAME, "");
    }
}
