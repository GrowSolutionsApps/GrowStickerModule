package cn.refactor.lib.colordialog.util.preLoad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

//import com.greedygame.core.adview.interfaces.AdLoadCallback;
//import com.greedygame.core.adview.GGAdview;
//import com.greedygame.core.adview.modals.AdRequestErrors;
//import com.greedygame.core.adview.interfaces.AdLoadCallback;

import cn.refactor.lib.colordialog.R;

// this Class Is use For Where Load Native Ads In Dialoag(Color Dialod)


public class BigNativeAdLoader {
    public static boolean isAdmobNativeAdLoaded = false;
    public static NativeAd AdmobNativeAd = null;
    public static adCallback mcallback;

    public static void loadAd(Context context, boolean is_setlist_admob_enabled, adCallback mcallback1) {
        Log.w("msg", "Greedy  Color BIG Native loadAd ");
        if (is_setlist_admob_enabled) {
            loadNativeAds(context);
            mcallback = mcallback1;
        }
    }

    public static void reloadColorNative(Context context, boolean is_setlist_admob_enabled, adCallback mcallback1) {
        Log.w("msg", "Greedy  Color BIG Native loadAd ");
        if (is_setlist_admob_enabled) {
            loadNativeAds(context);
            mcallback = mcallback1;
        }
    }


    public interface adCallback {
        public void adLoaded();

        public void adFaildToLoad();
    }

    public static void loadNativeAds(final Context context) {
        String admob_native;

        Log.w("msg", "color dialog is_play_store_download_required " + UData.remoteConfig.getString(UData.is_play_store_download_required));
        Log.w("msg", "color dialog app_from_playstore " + PreferenceManager.getStringData(context, "app_from_playstore"));
        /*if (UData.remoteConfig.getString(UData.is_play_store_download_required).equals("true")) {
            if (PreferenceManager.getStringData(context, "app_from_playstore").equals("true")) {
                if (!UData.remoteConfig.getString(UData.admob_native_new_3).equals("")) {
                    admob_native = UData.remoteConfig.getString(UData.admob_native_new_3);
                } else {
                    admob_native = context.getString(R.string.admob_native_play);
                }
            } else {
                if (!UData.remoteConfig.getString(UData.back_admob_native_new_3).equals("")) {
                    admob_native = UData.remoteConfig.getString(UData.back_admob_native_new_3);
                } else {
                    admob_native = context.getString(R.string.back_admob_native);
                }
            }
        } else {*/
            if (!UData.remoteConfig.getString(UData.admob_native_new_3).equals("")) {
                admob_native = UData.remoteConfig.getString(UData.admob_native_new_3);
            } else {
                admob_native = context.getString(R.string.admob_native_play);
            }
//        }
        Log.w("msg", "loadNativeAds : " + admob_native);
        AdLoader adLoader = new AdLoader.Builder(context, admob_native)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        Log.w("msg", "admob : NativeADs Adloaded Color ");
                        isAdmobNativeAdLoaded = true;
                        AdmobNativeAd = NativeAd;
                        mcallback.adLoaded();
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError errorCode) {
                        Log.w("msg", "admob : NativeADs  Color " + errorCode);
                        isAdmobNativeAdLoaded = false;
                        loadNativeAdsReLoad(context);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(MediationHelper.getAdReqvest(MediationHelper.ADTYPENATIVE));
    }

    @SuppressLint("MissingPermission")
    public static void loadNativeAdsReLoad(Context context) {
        String admob_native;

        /*if (UData.remoteConfig.getString(UData.is_play_store_download_required).equals("true")) {
            if (PreferenceManager.getStringData(context, "app_from_playstore").equals("true")) {
                if (!UData.remoteConfig.getString(UData.back_admob_native_new_3).equals("")) {
                    admob_native = UData.remoteConfig.getString(UData.back_admob_native_new_3);
                } else {
                    admob_native = context.getString(R.string.back_admob_native);
                }
            } else {
                if (!UData.remoteConfig.getString(UData.back_admob_native_new_3).equals("")) {
                    admob_native = UData.remoteConfig.getString(UData.back_admob_native_new_3);
                } else {
                    admob_native = context.getString(R.string.back_admob_native);
                }
            }
        } else {*/
            if (!UData.remoteConfig.getString(UData.back_admob_native_new_3).equals("")) {
                admob_native = UData.remoteConfig.getString(UData.back_admob_native_new_3);
            } else {
                admob_native = context.getString(R.string.back_admob_native);
            }
//        }
        AdLoader adLoader = new AdLoader.Builder(context, admob_native)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        Log.w("msg", "admob : NativeADsReLoad Adloaded Color ");

                        isAdmobNativeAdLoaded = true;
                        AdmobNativeAd = NativeAd;
                        mcallback.adLoaded();
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError errorCode) {
                        Log.w("msg", "admob : NativeADsReLoad Color " + errorCode);
                        isAdmobNativeAdLoaded = false;
                        mcallback.adFaildToLoad();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(MediationHelper.getAdReqvest(MediationHelper.ADTYPENATIVE));
    }

    public static boolean isreadytoshow(boolean is_setlist_admob_enabled) {
        Log.w("msg", "Greedy Color Admob BIG Native isreadytoshow ");
        if (is_setlist_admob_enabled) {
            if (BigNativeAdLoader.isAdmobNativeAdLoaded && BigNativeAdLoader.AdmobNativeAd != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


}
