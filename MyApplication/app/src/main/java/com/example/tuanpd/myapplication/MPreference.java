package com.example.tuanpd.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class MPreference {
    private static final String PREF_FILE = "pref_app";
    private static final String PREF_KEY_INTERVAL_PERIOD = "PREF_KEY_INTERVAL_PERIOD";
    private static final String PREF_KEY_CURRENT_GROUP_ID = "PREF_KEY_CURRENT_GROUP_ID";
    private static final String PREF_KEY_CURRENT_GROUP_PW = "PREF_KEY_CURRENT_GROUP_PW";
    private static final String PREF_KEY_IS_GROUP_CONNECTED = "PREF_KEY_IS_GROUP_CONNECTED";
    private static final String PREF_KEY_IS_GPS_SHARING = "PREF_KEY_IS_GPS_SHARING";

    private static SharedPreferences.Editor getPreferenceEditor(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static int getIntervalPeriodIndex(Context context) {
        return getSharedPreferences(context).getInt(PREF_KEY_INTERVAL_PERIOD, 0); // default: 0
    }

    public static void setIntervalPeriodIndex(Context context, int index) {
        getPreferenceEditor(context).putInt(PREF_KEY_INTERVAL_PERIOD, index).commit();
    }

    public static String getCurrentGroupId(Context context) {
        return getSharedPreferences(context).getString(PREF_KEY_CURRENT_GROUP_ID, ""); // default: 0
    }

    public static void setCurrentGroupId(Context context, String id) {
        getPreferenceEditor(context).putString(PREF_KEY_INTERVAL_PERIOD, id).commit();
    }

    public static String getCurrentGroupPw(Context context) {
        return getSharedPreferences(context).getString(PREF_KEY_CURRENT_GROUP_PW, ""); // default: empty
    }

    public static void setCurrentGroupPw(Context context, String pw) {
        getPreferenceEditor(context).putString(PREF_KEY_CURRENT_GROUP_PW, pw).commit();
    }

    public static boolean isGroupConnected(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_KEY_IS_GROUP_CONNECTED, false); // default: false
    }

    public static void setGroupConnected(Context context, boolean isConnected) {
        getPreferenceEditor(context).putBoolean(PREF_KEY_IS_GROUP_CONNECTED, isConnected).commit();
    }

    public static boolean isGPSSharing(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_KEY_IS_GPS_SHARING, false); // default: false
    }

    public static void setGPSSharing(Context context, boolean isSharing) {
        getPreferenceEditor(context).putBoolean(PREF_KEY_IS_GPS_SHARING, isSharing).commit();
    }
}
