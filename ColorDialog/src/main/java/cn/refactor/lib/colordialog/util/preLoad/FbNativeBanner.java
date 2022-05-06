//package cn.refactor.lib.colordialog.util.preLoad;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.NativeAdListener;
//import com.facebook.ads.NativeBannerAd;
//
//import cn.refactor.lib.colordialog.R;
//
//
//public class FbNativeBanner {
//    public static boolean isFbNativeAdLoaded = false;
//    public static NativeBannerAd nativeBannerAd;
//
//
//    public static void pre_load_Ad(Context context) {
//        loadAd(context);
//    }
//
//    public static void loadAd(Context context) {
//        isFbNativeAdLoaded = false;
//        //AdSettings.addTestDevice("9f34c1b2-f8cf-4d74-9045-4284496c1a75");
//        loadFbNative(context);
//    }
//
//    public static NativeBannerAd getFbNativeAd(Context context) {
//        Log.w("msg", "fb loadNativeAd");
//        try {
//            if (nativeBannerAd != null) {
//                if (nativeBannerAd.isAdLoaded()) {
//                    return nativeBannerAd;
//                } else {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static void loadFbNative(Context context) {
//        try {
//
//            String fb_native_banner = "";
//            if (!UData.remoteConfig.getString(UData.fb_native_banner).equals("")) {
//                fb_native_banner = UData.remoteConfig.getString(UData.fb_native_banner);
//            } else {
//                fb_native_banner = context.getString(R.string.fb_native_banner);
//            }
//            nativeBannerAd = new NativeBannerAd(context, fb_native_banner);
//          /*  nativeBannerAd.setAdListener(new NativeAdListener() {
//                @Override
//                public void onMediaDownloaded(Ad ad) {
//                    Log.e("msg", "Native ad finished downloading all assets.");
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                    Log.w("msg", "AdError==" + adError.getErrorMessage());
//                    isFbNativeAdLoaded = false;
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//                    Log.w("msg", "Fb_Native_load");
//                    isFbNativeAdLoaded = true;
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                }
//            });
//            nativeBannerAd.loadAd();*/
//            NativeAdListener nativeAdListener = new NativeAdListener() {
//                @Override
//                public void onMediaDownloaded(Ad ad) {
//                    // Native ad finished downloading all assets
//
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                    // Native ad failed to load
//                    isFbNativeAdLoaded = false;
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//                    // Native ad is loaded and ready to be displayed
//                    isFbNativeAdLoaded = true;
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    // Native ad clicked
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    // Native ad impression
//                }
//            };
//
//            // Request an ad
//            nativeBannerAd.loadAd(
//                    nativeBannerAd.buildLoadAdConfig()
//                            .withAdListener(nativeAdListener)
//                            .build());
//        } catch (Exception e) {
//
//        }
//    }
//
//}
//
