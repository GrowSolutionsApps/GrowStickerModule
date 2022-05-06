package cn.refactor.lib.colordialog.util.preLoad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UData {
    public static FirebaseRemoteConfig remoteConfig  = FirebaseRemoteConfig.getInstance();
//    public static String app_ads_sdk = "app_ads_sdk";
//    public static String app_ads_check_var = "admob";
//    public static String greedy_sdk_var = "greedy";


//    //Firebase remote config variable name
//    oldd
//    public static String admob_native = "admob_native";
//    public static String back_admob_native = "back_admob_native";

    //Firebase remote config variable name

    public static String is_setlist_admob_enabled = "is_setlist_admob_enabled";
    public static String is_insideAct_admob_enabled = "is_insideAct_admob_enabled";
    public static String admob_native_new_3 = "admob_native_new_3";
    public static String back_admob_native_new_3 = "back_admob_native_new_3";
    public static String fb_native_banner = "fb_native_banner";
    public static String is_play_store_download_required = "is_play_store_download_required";



}
