package com.example.stickers.preference;

import static com.example.stickers.preference.PreferenceKeys.APP_VERSION_CODE;

import android.content.Context;

public class PreferenceManager {

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    static public void saveData(Context context, String key, boolean val) {
        if (context == null)
            return;
        context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, val)
                .apply();

    }

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    // Save Data
    static public void saveData(Context context, String key, String val) {
        if (context == null)
            return;
        context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).edit().putString(key, val).apply();
    }

    /**
     * Gets string data.
     *
     * @param context the context
     * @param key     the key
     * @return the string data
     */
    // Get Data
    static public String getStringData(Context context, String key) {
        if (context == null)
            return "";
        if (key.matches(APP_VERSION_CODE)) {
            try {
                return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getString(key, "");
            } catch (Exception e) {
                return "";
            }
        } else {
            return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getString(key, "");
        }
    }
}
