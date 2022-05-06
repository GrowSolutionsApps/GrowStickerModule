package com.example.stickers.utils;


import android.content.Context;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

public final class UncachedInputMethodManagerUtils {
    public static boolean isThisImeEnabled(Context context, InputMethodManager inputMethodManager) {
        String packageName = context.getPackageName();
        for (InputMethodInfo packageName2 : inputMethodManager.getEnabledInputMethodList()) {
            if (packageName.equals(packageName2.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isThisImeCurrent(Context context, InputMethodManager inputMethodManager) {
        InputMethodInfo inputMethodInfoOf = getInputMethodInfoOf(context.getPackageName(), inputMethodManager);
        return inputMethodInfoOf != null && inputMethodInfoOf.getId().equals(Settings.Secure.getString(context.getContentResolver(), "default_input_method"));
    }

    public static InputMethodInfo getInputMethodInfoOf(String str, InputMethodManager inputMethodManager) {
        for (InputMethodInfo next : inputMethodManager.getInputMethodList()) {
            if (str.equals(next.getPackageName())) {
                return next;
            }
        }
        return null;
    }
}