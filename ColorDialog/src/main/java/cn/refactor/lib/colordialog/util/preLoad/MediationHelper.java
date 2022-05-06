package cn.refactor.lib.colordialog.util.preLoad;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdRequest;

//import com.google.ads.mediation.facebook.FacebookAdapter;
//import com.google.ads.mediation.facebook.FacebookExtras;

public class MediationHelper {
    public static String ADTYPEINT = "int";
    public static String ADTYPEBANNER = "banner";
    public static String ADTYPENATIVE = "native";
    public static String ADTYPENATIVEBANNER = "native_banner";

    public static AdRequest getAdReqvest(String type) {
//        AdColonyBundleBuilder.setShowPrePopup(true);
//        AdColonyBundleBuilder.setShowPostPopup(true);
        AdRequest adRequest;
        if (type.matches(ADTYPEBANNER)) {
            adRequest = new AdRequest.Builder().build();
        }else if (type.matches(ADTYPENATIVEBANNER)) {
            adRequest = new AdRequest.Builder()
//                    .addNetworkExtrasBundle(FacebookAdapter.class, new FacebookExtras().setNativeBanner(true).build())
                    .build();
        }  else {
            adRequest = new AdRequest.Builder()
//                    .addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build())
                    .build();
        }
        return adRequest;
    }

    //must call in activity while call int ad
    public static void onResume(Activity con) {
//        IronSource.onResume(con);
    }

    //must call in activity while call int ad
    public static void onPause(Context con) {
//        IronSource.onPause(con);
    }

}
