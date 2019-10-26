package com.sparetimeforu.android.sparetimeforu.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/15.
 * Email:17wjli6@stu.edu.cn
 */
public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String FIRST_START = "firstStart";

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

    public static Boolean getIsFirstStart(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(FIRST_START, true);
    }

    public static void setIsFirstStart(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(FIRST_START, false)
                .apply();
    }
}
