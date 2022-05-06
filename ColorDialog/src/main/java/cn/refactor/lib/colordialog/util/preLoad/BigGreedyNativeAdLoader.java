//package cn.refactor.lib.colordialog.util.preLoad;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.greedygame.core.adview.GGAdview;
//import com.greedygame.core.adview.interfaces.AdLoadCallback;
//import com.greedygame.core.adview.modals.AdRequestErrors;
//
//import org.jetbrains.annotations.NotNull;
//
//import cn.refactor.lib.colordialog.R;
//
//
//// this Class Is use For Where Load BIG Native Ads In FontAdpter ,Leave Dialog etc,...
//public class BigGreedyNativeAdLoader {
//    public static void loadAd(Context context, FrameLayout nativeUnit, int iLayHeight, adCallback mcallback1) {
//        loadGreedyNativeAd(context, nativeUnit, iLayHeight);
//    }
//    public static void reloadAds(Context context) {
//    }
//
//    public interface adCallback {
//        public void adLoaded();
//        public void adFaildToLoad();
//    }
//
//    public static boolean isreadytoshow() {
//        return false;
//
//    }
//
//    public static void loadGreedyNativeAd(Context context, final FrameLayout nativeUnit, int height) {
//        Log.w("msg", "Greedy Color  loadGreedyNativeAd " +height);
//
//        GGAdview ggAdView = new GGAdview(context);
//        ggAdView.setUnitId(context.getString(R.string.greedy_native));
//        ggAdView.setAdsMaxHeight(height); //Value is in pixels, not in dp
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
//        nativeUnit.addView(ggAdView, layoutParams);
//        ggAdView.loadAd(new AdLoadCallback() {
//            @Override
//            public void onAdLoaded() {
//                nativeUnit.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdLoadFailed(@NotNull AdRequestErrors adRequestErrors) {
//                Log.w("msg", "Native Failed== " + adRequestErrors.toString());
//                nativeUnit.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onUiiOpened() {
//            }
//
//            @Override
//            public void onUiiClosed() {
//            }
//
//            @Override
//            public void onReadyForRefresh() {
//            }
//        });
//    }
//
//}
