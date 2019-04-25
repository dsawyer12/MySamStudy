package com.example.mysamstudy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SettingsManager {
    private static SharedPreferences sharedPreferences;
    public static final String dark_theme_preferences = "dark_theme_preferences";
    public static final String share_selection_preferences = "share_selection_preferences";
    public static final String share_all_sets_preferences = "share_all_sets_preferences";
    public static final String user_session = "user_session";
    public static final String study_preferences_loop = "study_preferences_loop";
    public static final String study_preferences_show_answer = "study_preferences_show_answer";

    public SettingsManager(){}

    public static void getSharedPreferences(Context context, String preferences){
        sharedPreferences = context.getSharedPreferences(preferences, Context.MODE_PRIVATE);
    }

    public static Boolean getDarkTheme(String preferences){
        return sharedPreferences.getBoolean(preferences, false);
    }

    public static Integer getShareSelectionPreferences(String preferences){
        return sharedPreferences.getInt(preferences, 0);
    }

    public static Boolean get_study_preferences(String preferences){
        return sharedPreferences.getBoolean(preferences, false);
    }

    public static Boolean get_share_all_sets_preferences(String preferences){
        return sharedPreferences.getBoolean(preferences, true);
    }

    public static void write(String preferences, boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferences, value);
        editor.apply();
    }

    public static void write(String preferences, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(preferences, value);
        editor.apply();
    }

    public static void logOut(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(user_session, null);
        editor.apply();
    }

    public static String getUserSession(String preferences){
        return sharedPreferences.getString(preferences, null);
    }

    public static void loadUserSession(String preferences, String values){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferences, values);
        editor.apply();
    }
}
