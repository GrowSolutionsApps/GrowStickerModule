package cn.refactor.lib.colordialog.util.preLoad;

import android.content.Context;


public class PreferenceManager {

    /**
     * Gets boolean data.
     *
     * @param context the context
     * @param key     the key
     * @return the boolean data
     */
    static public boolean getBooleanData(Context context, String key) {
        try {
            if (context != null)
                return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getBoolean(key, false);
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Gets boolean data.
     *
     * @param context the context
     * @param key     the key
     * @return the boolean data
     */
    static public boolean getBooleanData(Context context, String key, boolean def) {

        try {
            if (context != null)
                return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getBoolean(key, def);
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets int data.
     *
     * @param context the context
     * @param key     the key
     * @return the int data
     */
    static public int getIntData(Context context, String key) {
        try {
            if (context != null)
                return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getInt(key, 0);
            else
                return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    static public int getIntData(Context context, String key, int def) {
        try {
            if (context != null)
                return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getInt(key, def);
            else
                return 0;
        } catch (Exception e) {
            return 0;
        }
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
        if (key.matches("app_version_code")) {
            try {
                return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getString(key, "");
            } catch (Exception e) {
                return "";
            }
        } else {
            return context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).getString(key, "");
        }
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
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    static public void saveData(Context context, String key, int val) {
        if (context == null)
            return;
        context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE).edit().putInt(key, val).apply();
    }

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

    static public void saveData(Context context, String key, float val) {
        if (context == null)
            return;
        context.getSharedPreferences(PreferenceKeys.THEME_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putFloat(key, val)
                .apply();
    }


}
