package cn.refactor.lib.colordialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdOptionsView;
//import com.facebook.ads.MediaView;
//import com.facebook.ads.NativeAdLayout;
//import com.facebook.ads.NativeAdListener;
//import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;


import cn.refactor.lib.colordialog.util.DisplayUtil;
//import cn.refactor.lib.colordialog.util.preLoad.BigGreedyNativeAdLoader;
import cn.refactor.lib.colordialog.util.preLoad.BigNativeAdLoader;
//import cn.refactor.lib.colordialog.util.preLoad.FbNativeBanner;
import cn.refactor.lib.colordialog.util.preLoad.UData;

import static android.view.View.GONE;

//import android.support.annotation.NonNull;
//import android.support.annotation.NonNull;
//import androidx.annotation.NonNull;

public class GameDialog extends AlertDialog implements View.OnClickListener {
    private View mBtnGroupView, mBkgView, mDialogView;
    private MaterialRippleLayout ripple_apply;
    private OnApplyListener applyListener;
    private TextView mTitleTv;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private Drawable mDrawable;
    private Context con;
    private AnimationSet mAnimIn, mAnimOut;

    private int mpos, mBackgroundColor, mTitleTextColor, mContentTextColor;
    private int mPlaceHolder;
    private CharSequence mTitleText;
    private boolean mApplied;
    private boolean mIsCustomGIFTheme = false;
    private CharSequence mImagePath, mGifPath;
    private ImageView ivThemeBg;
    private boolean mIsShowAnim;
    private LinearLayout lay_apply;
    ThemeDialogTemplateView admob_native_template;
    FrameLayout nativeUnit;
    //    private NativeAdLayout nativeAdLayout1;
//    private LinearLayout adView1;
//    private NativeBannerAd nativeBannerAd1;
    private RelativeLayout adView1;
//    private NativeBannerAd nativeBannerAd1;
//    private NativeAdLayout nativeAdLayout1;


    public GameDialog(Context context) {
        this(context, 0);
    }

    public GameDialog(Context context, int theme) {
        super(context, R.style.color_dialog_3);
        con = context;
        init();
    }


    private void callDismiss() {
        super.dismiss();
    }

    private void init() {
        mAnimIn = AnimationLoader.getInAnimation(getContext());
        mAnimOut = AnimationLoader.getOutAnimation(getContext());
        initAnimListener();
    }

    public void setTitle(CharSequence title) {
        mTitleText = title;

    }

    public GameDialog setTitleText(CharSequence title) {
        mTitleText = title;
        return this;
    }

    public GameDialog setApplied(boolean applied) {
        mApplied = applied;
        return this;
    }

    public GameDialog setAllowDelete(boolean allow) {
        return this;
    }

    public GameDialog setOnlineTheme(boolean onlineTheme) {
        return this;
    }

    public GameDialog setCustomGifTheme(boolean customTheme, String gifBgPath) {
        mIsCustomGIFTheme = customTheme;
        mGifPath = gifBgPath;
        return this;
    }

    public GameDialog setCustomTheme(boolean customTheme) {
        return this;
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getText(titleId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = View.inflate(getContext(), R.layout.layout_gamedialog, null);
        setContentView(contentView);
        prefs = con.getSharedPreferences("THEME_PREFS", Context.MODE_PRIVATE);
        if (prefs != null)
            edit = prefs.edit();

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mBkgView = contentView.findViewById(R.id.llBkg);
        ripple_apply = contentView.findViewById(R.id.ripple_apply);
        admob_native_template = findViewById(R.id.admob_native_template);
        nativeUnit = findViewById(R.id.nativeUnit);
        if (!prefs.getBoolean("is_remove_ads", false))
        {
//            if (UData.remoteConfig.getString(UData.app_ads_sdk).equals(UData.app_ads_check_var)) {
                BigNativeAdLoader.loadAd(con,UData.remoteConfig.getBoolean(UData.is_insideAct_admob_enabled), new BigNativeAdLoader.adCallback() {
                    @Override
                    public void adLoaded() {
                        if (BigNativeAdLoader.isreadytoshow(UData.remoteConfig.getBoolean(UData.is_insideAct_admob_enabled))) {
                            admob_native_template.setNativeAd(BigNativeAdLoader.AdmobNativeAd);
                            admob_native_template.setVisibility(View.VISIBLE);
                        } else {
                            try {
                                admob_native_template.setVisibility(GONE);
                            } catch (Exception e) {

                            }
                        }
                    }

                    @Override
                    public void adFaildToLoad() {
                        try {
                            admob_native_template.setVisibility(GONE);
                        } catch (Exception e) {
                        }
                    }
                });

//            }
//            else if (UData.remoteConfig.getString(UData.app_ads_sdk).equals(UData.greedy_sdk_var)) {
//                BigGreedyNativeAdLoader.loadAd(con, nativeUnit, 60, new BigGreedyNativeAdLoader.adCallback() {
//                    @Override
//                    public void adLoaded() {
//                        nativeUnit.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void adFaildToLoad() {
//                        try {
//                            nativeUnit.setVisibility(GONE);
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//            } else {
//                loadFB_NativeBannerADs();
//            }
        } else {
            admob_native_template.setVisibility(GONE);
        }

        mTitleTv = (TextView) contentView.findViewById(R.id.tvTitle);
        ivThemeBg = (ImageView) contentView.findViewById(R.id.ivThemeBg);
        mBtnGroupView = contentView.findViewById(R.id.llBtnGroup);
//        nativeAdLayout1 = (NativeAdLayout) findViewById(R.id.native_banner_ad_container);

        lay_apply = (LinearLayout) contentView.findViewById(R.id.lay_apply);
        lay_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyListener.onClick(GameDialog.this, 0);
            }
        });
        ripple_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyListener.onClick(GameDialog.this, mpos);
            }
        });
        if (mTitleText != null)
            mTitleTv.setText(mTitleText);
        if (mImagePath != null && mPlaceHolder != 0) {
            try {
                Glide.with(con)
                        .load(mImagePath.toString())
                        .placeholder(mPlaceHolder)
                        .into(ivThemeBg);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        setTextColor();
        setBackgroundColor();
    }

    private void setBottomCorners(View llBtnGroup) {
        int radius = DisplayUtil.dp2px(getContext(), 10);
        float[] outerRadii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(R.color.diy_actionbar));
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startWithAnimation(mIsShowAnim);
    }

    @Override
    public void dismiss() {
        dismissWithAnimation(mIsShowAnim);
    }

    private void startWithAnimation(boolean showInAnimation) {
        if (showInAnimation) {
            mDialogView.startAnimation(mAnimIn);
        }
    }

    private void dismissWithAnimation(boolean showOutAnimation) {
        if (showOutAnimation) {
            mDialogView.startAnimation(mAnimOut);
        } else {
            super.dismiss();
        }
    }

    private void initAnimListener() {
        mAnimOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        callDismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setBackgroundColor() {
        if (0 == mBackgroundColor) {
            return;
        }

        int radius = DisplayUtil.dp2px(getContext(), 6);
        float[] outerRadii = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(mBackgroundColor);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        mBkgView.setBackgroundDrawable(shapeDrawable);
    }

    private void setTextColor() {
        if (0 != mTitleTextColor) {
            mTitleTv.setTextColor(mTitleTextColor);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    public GameDialog setAnimationEnable(boolean enable) {
        mIsShowAnim = enable;
        return this;
    }

    public GameDialog setAnimationIn(AnimationSet animIn) {
        mAnimIn = animIn;
        return this;
    }

    public GameDialog setAnimationOut(AnimationSet animOut) {
        mAnimOut = animOut;
        initAnimListener();
        return this;
    }

    public GameDialog setColor(int color) {
        mBackgroundColor = color;
        return this;
    }

    public GameDialog setImagePath(String path, int place_re) {
        mImagePath = path;
        mPlaceHolder = place_re;
        return this;
    }

    public GameDialog setpos(int pos) {
        mpos = pos;
        return this;
    }

    public GameDialog setColor(String color) {
        try {
            setColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    public GameDialog setTitleTextColor(int color) {
        mTitleTextColor = color;
        return this;
    }

    public GameDialog setTitleTextColor(String color) {
        try {
            setTitleTextColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    public GameDialog setContentTextColor(int color) {
        mContentTextColor = color;
        return this;
    }

    public GameDialog setContentTextColor(String color) {
        try {
            setContentTextColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }


    public GameDialog setApplyListener(OnApplyListener l) {
        applyListener = l;
        return this;
    }

    public GameDialog setEditListener(OnEditListener l) {
        return this;
    }

    public GameDialog setDeleteListener(OnDeleteListener l) {
        return this;
    }

//        i

    public GameDialog setShareListener(OnShareListener l) {
        return this;
    }

    public GameDialog setContentImage(Drawable drawable) {
        mDrawable = drawable;
        return this;
    }

    public GameDialog setContentImage(Bitmap bitmap) {
        return this;
    }

    public GameDialog setContentImage(int resId) {
        return this;
    }

    public CharSequence getTitleText() {
        return mTitleText;
    }


    public interface OnEditListener {
        void onClick(GameDialog dialog);
    }

    public interface OnShareListener {
        void onClick(GameDialog dialog);
    }

    public interface OnApplyListener {
        void onClick(GameDialog dialog, int i);
    }

    public interface OnDismissListener {
        void onClick(GameDialog dialog);
    }

    public interface OnDeleteListener {
        void onClick(GameDialog dialog);
    }

    /*private void loadFB_NativeBannerADs() {
        nativeBannerAd1 = FbNativeBanner.getFbNativeAd(con);
        if (nativeBannerAd1 != null && nativeBannerAd1.isAdLoaded()) {
            inflateAdSmall(nativeBannerAd1);
        } else {
            String fb_native_banner = "";
            if (!UData.remoteConfig.getString(UData.fb_native_banner).equals("")) {
                fb_native_banner = UData.remoteConfig.getString(UData.fb_native_banner);
            } else {
                fb_native_banner = con.getString(R.string.fb_native_banner);
            }
            nativeBannerAd1 = new NativeBannerAd(con, fb_native_banner);
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    // Native ad finished downloading all assets

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Native ad failed to load
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Native ad is loaded and ready to be displayed
                    if (nativeBannerAd1 == null || nativeBannerAd1 != ad) {
                        return;
                    }
                    admob_native_template.setVisibility(GONE);
                    nativeUnit.setVisibility(GONE);
                    inflateAdSmall(nativeBannerAd1);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                }
            };
            // Request an ad
            nativeBannerAd1.loadAd(
                    nativeBannerAd1.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
        }
    }

    private void inflateAdSmall(NativeBannerAd nativeBannerAd) {
        nativeBannerAd.unregisterView();
        nativeAdLayout1.setVisibility(View.VISIBLE);
        admob_native_template.setVisibility(GONE);
        nativeUnit.setVisibility(GONE);
        LayoutInflater inflater = LayoutInflater.from(con);
        adView1 = (RelativeLayout) inflater.inflate(R.layout.fb_native_banner1, nativeAdLayout1, false);
        nativeAdLayout1.addView(adView1);
        RelativeLayout adChoicesContainer = (RelativeLayout) adView1.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(con, nativeBannerAd, nativeAdLayout1);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);
        TextView nativeAdTitle = (TextView) adView1.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = (TextView) adView1.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = (TextView) adView1.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = (MediaView) adView1.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = (Button) adView1.findViewById(R.id.native_ad_call_to_action);
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView1, nativeAdIconView, clickableViews);
    }*/
}
