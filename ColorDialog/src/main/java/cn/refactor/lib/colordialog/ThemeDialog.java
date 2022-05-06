package cn.refactor.lib.colordialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import android.util.Log;
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

//import android.support.annotation.NonNull;

//import androidx.annotation.NonNull;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.MediaView;
//import com.facebook.ads.AdOptionsView;
//import com.facebook.ads.MediaView;
//import com.facebook.ads.NativeAdLayout;
//import com.facebook.ads.NativeAdListener;
//import com.facebook.ads.NativeBannerAd;
import com.tenor.android.core.loader.GlideTaskParams;
import com.tenor.android.core.loader.IDrawableLoaderTaskListener;
import com.tenor.android.core.loader.gif.GifLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.util.DisplayUtil;
//import cn.refactor.lib.colordialog.util.preLoad.BigGreedyNativeAdLoader;
import cn.refactor.lib.colordialog.util.preLoad.BigNativeAdLoader;
//import cn.refactor.lib.colordialog.util.preLoad.FbNativeBanner;
import cn.refactor.lib.colordialog.util.preLoad.UData;;

import static android.view.View.GONE;
import static com.tenor.android.core.loader.GlideLoader.applyDimens;
import static com.tenor.android.core.loader.GlideLoader.load;

public class ThemeDialog extends AlertDialog implements View.OnClickListener {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private Bitmap mContentBitmap;

    private View mBtnGroupView, mBkgView, mDialogView;

    private TextView mTitleTv, tvApply;

    private Drawable mDrawable;
    private Context con;
    private AnimationSet mAnimIn, mAnimOut;

    private int mResId, mBackgroundColor, mTitleTextColor, mContentTextColor;
    private int mPlaceHolder;
    private OnEditListener mEditListener;
    private OnDeleteListener mDeleteListener;
    private OnApplyListener mApplyListener;
    private OnUploadListener mUploadListener;
    private OnShareListener mShareListener;
    private MaterialRippleLayout mLayDelete, ripple_upload;

    private CharSequence mTitleText;
    private boolean mApplied;
    private boolean mAllowDelete;
    private boolean mAllowUpload;
    private boolean mIsOnlineTheme;
    private boolean mIsCustomTheme;
    private boolean mIsCustomGIFTheme = false;
    private CharSequence mImagePath, mGifPath;
    private ImageView ivThemeBg, ivThemeGIFBg, ivThemeGIFTag;
    ImageView ivApply;
    private boolean mIsShowAnim;
    private LinearLayout lay_apply, lay_edit, lay_share, lay_delete;
    ThemeDialogTemplateView admob_native_template;
//    private NativeAdLayout nativeAdLayout1;
//    private RelativeLayout adView1;
//    private NativeBannerAd nativeBannerAd1;

    FrameLayout nativeUnit;

    //    private NativeAdLayout nativeAdLayout1;
//    private LinearLayout adView1;
//    private NativeBannerAd nativeBannerAd1;

    public ThemeDialog(Context context) {
        this(context, 0);
    }

    public ThemeDialog(Context context, int theme) {
        super(context, R.style.color_dialog_2);
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

    public ThemeDialog setTitleText(CharSequence title) {
        mTitleText = title;
        return this;
    }

    public ThemeDialog setApplied(boolean applied) {
        mApplied = applied;
        return this;
    }

    public ThemeDialog setAllowDelete(boolean allow) {
        mAllowDelete = allow;
        return this;
    }

    public ThemeDialog setAllowUpload(boolean allow) {
        mAllowUpload = allow;
        return this;
    }

    public ThemeDialog setOnlineTheme(boolean onlineTheme) {
        mIsOnlineTheme = onlineTheme;
        return this;
    }

    public ThemeDialog setCustomGifTheme(boolean customTheme, String gifBgPath) {
        mIsCustomGIFTheme = customTheme;
        mGifPath = gifBgPath;
        return this;
    }

    public ThemeDialog setCustomTheme(boolean customTheme) {
        mIsCustomTheme = customTheme;
        return this;
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getText(titleId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.layout_themedialog, null);
        setContentView(contentView);
        prefs = con.getSharedPreferences("THEME_PREFS", Context.MODE_PRIVATE);
        if (prefs != null)
            edit = prefs.edit();
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mBkgView = contentView.findViewById(R.id.llBkg);
        admob_native_template = findViewById(R.id.admob_native_template);
        nativeUnit = findViewById(R.id.nativeUnit);
        if (!prefs.getBoolean("is_remove_ads", false)) {
//            if (UData.remoteConfig.getString(UData.app_ads_sdk).equals(UData.app_ads_check_var)) {
                BigNativeAdLoader.loadAd(con,UData.remoteConfig.getBoolean(UData.is_setlist_admob_enabled), new BigNativeAdLoader.adCallback() {
                    @Override
                    public void adLoaded() {
                        if (BigNativeAdLoader.isreadytoshow(UData.remoteConfig.getBoolean(UData.is_setlist_admob_enabled))) {
                            admob_native_template.setNativeAd(BigNativeAdLoader.AdmobNativeAd);
                            admob_native_template.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void adFaildToLoad() {
                        admob_native_template.setVisibility(GONE);
                    }
                });
//            }
//            else if (UData.remoteConfig.getString(UData.app_ads_sdk).equals(UData.greedy_sdk_var)) {
//                BigGreedyNativeAdLoader.loadAd(con, nativeUnit, 60, new BigGreedyNativeAdLoader.adCallback() {
//                    @Override
//                    public void adLoaded() {
//                    }
//
//                    @Override
//                    public void adFaildToLoad() {
//                        admob_native_template.setVisibility(GONE);
//                    }
//                });
//            } else {
//                loadFB_NativeBannerADs();
//            }

        } else {
            admob_native_template.setVisibility(GONE);
        }

        mTitleTv = (TextView) contentView.findViewById(R.id.tvTitle);
        tvApply = (TextView) contentView.findViewById(R.id.tvApply);
        ivThemeBg = (ImageView) contentView.findViewById(R.id.ivThemeBg);
        ivThemeGIFBg = (ImageView) contentView.findViewById(R.id.ivThemeGIFBg);
        ivApply = (ImageView) contentView.findViewById(R.id.ivApply);
        ivThemeGIFTag = (ImageView) contentView.findViewById(R.id.ivThemeGIFTag);
        mLayDelete = (MaterialRippleLayout) contentView.findViewById(R.id.ripple_delete);
        ripple_upload = (MaterialRippleLayout) contentView.findViewById(R.id.ripple_upload);
        mBtnGroupView = contentView.findViewById(R.id.llBtnGroup);
        lay_apply = (LinearLayout) contentView.findViewById(R.id.lay_apply);
        lay_edit = (LinearLayout) contentView.findViewById(R.id.lay_edit);
        lay_share = (LinearLayout) contentView.findViewById(R.id.lay_share);
        lay_delete = (LinearLayout) contentView.findViewById(R.id.lay_delete);

        lay_apply.setOnClickListener(this);
        lay_edit.setOnClickListener(this);
        lay_share.setOnClickListener(this);
        lay_delete.setOnClickListener(this);
        ripple_upload.setOnClickListener(this);
        if (mTitleText != null)
            mTitleTv.setText(mTitleText);

        if (!mAllowDelete) {
            lay_delete.setVisibility(View.GONE);
            mLayDelete.setVisibility(View.GONE);
        }
        if (mAllowUpload) {
            ripple_upload.setVisibility(View.VISIBLE);
        } else {
            ripple_upload.setVisibility(GONE);
        }
        if (mApplied) {
//            ivApply.setImageResource(R.drawable.ic_check_applied);
            tvApply.setText("Applied");
            tvApply.setTextColor(con.getResources().getColor(R.color.black));
        }
        if (null == mEditListener && null == mShareListener) {
            mBtnGroupView.setVisibility(View.GONE);
        }

        Log.w("msg", "mIsOnlineTheme " + mIsOnlineTheme);
        Log.w("msg", "mImagePath " + mImagePath);
        if (mImagePath != null && mPlaceHolder != 0) {
            if (!mIsOnlineTheme && !mIsCustomTheme) {
                String[] filenameArray = mImagePath.toString().split("\\.");
                String extension = filenameArray[filenameArray.length - 1];
                Log.w("msg", "extension " + extension);
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("webp") || extension.equalsIgnoreCase("png")) {
                    try {
                        Glide.with(con)
                                .load(mImagePath.toString())
                                .placeholder(mPlaceHolder)
                                .into(ivThemeBg);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else if (extension.equalsIgnoreCase("gif")) {
                    try {
                        GlideTaskParams<ImageView> params = new GlideTaskParams<>(ivThemeBg, "" + Uri.fromFile(new File(mImagePath.toString())));
                        params.setListener(new IDrawableLoaderTaskListener<ImageView, Drawable>() {
                            @Override
                            public void success(@NonNull ImageView imageView, @NonNull Drawable drawable) {
                            }

                            @Override
                            public void failure(@NonNull ImageView imageView, @NonNull Drawable drawable) {
                                imageView.setImageDrawable(drawable);

                            }
                        });
                        GifRequestBuilder<String> requestBuilder = Glide.with((con)).load(params.getPath()).asGif().diskCacheStrategy(DiskCacheStrategy.ALL);
                        load(applyDimens(requestBuilder, params), params);
//                        ImageRequest.create(themePreviewBg)
//                                .setTargetFile(staticThemeData.get(position).keyboard_gif_bigPreview)
//                                .execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mIsCustomGIFTheme) {
                    ivThemeGIFTag.setVisibility(View.VISIBLE);
                    Glide.with(con).load(Uri.fromFile(new File(mImagePath.toString()))).listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).placeholder(mPlaceHolder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivThemeBg);
                    Log.w("msg", "ivThemeGIFBg " + mGifPath.toString());
                    GlideTaskParams<ImageView> params1 = new GlideTaskParams<>(ivThemeGIFBg, "" + Uri.fromFile(new File(mGifPath.toString())));
                    params1.setListener(new IDrawableLoaderTaskListener<ImageView, Drawable>() {
                        @Override
                        public void success(@NonNull ImageView imageView, @NonNull Drawable drawable) {
                            imageView.setImageDrawable(drawable);
                        }

                        @Override
                        public void failure(@NonNull ImageView imageView, @NonNull Drawable drawable) {
                        }
                    });
                    GifLoader.loadGif(con, params1);
                }


            } else if (mIsCustomTheme) {
                try {
                    Log.w("msg", "ivThemeGIFBg " + mGifPath.toString());
                    Log.w("msg", "ivThemeGIFBg " + !mIsCustomGIFTheme);
                    if (!mIsCustomGIFTheme) {
                        Glide.with(con).load(Uri.fromFile(new File(mImagePath.toString()))).listener(new RequestListener<Uri, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                e.printStackTrace();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).placeholder(mPlaceHolder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivThemeBg);
                    } else {
                        ivThemeGIFTag.setVisibility(View.VISIBLE);
                        Glide.with(con).load(Uri.fromFile(new File(mImagePath.toString()))).listener(new RequestListener<Uri, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                e.printStackTrace();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).placeholder(mPlaceHolder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivThemeBg);
                        Log.w("msg", "ivThemeGIFBg " + mGifPath.toString());
                        GlideTaskParams<ImageView> params = new GlideTaskParams<>(ivThemeGIFBg, "" + Uri.fromFile(new File(mGifPath.toString())));
                        params.setListener(new IDrawableLoaderTaskListener<ImageView, Drawable>() {
                            @Override
                            public void success(@NonNull ImageView imageView, @NonNull Drawable drawable) {
                                imageView.setImageDrawable(drawable);
                            }

                            @Override
                            public void failure(@NonNull ImageView imageView, @NonNull Drawable drawable) {
                            }
                        });
                        GifLoader.loadGif(con, params);
                    }
                } catch (Exception e) {
                }
            } else {
                PackageManager pmanager = con.getPackageManager();
                Resources res = null;
                try {
                    res = pmanager.getResourcesForApplication(mImagePath.toString());
                    int mDrawableResID = res.getIdentifier("small_preview", "drawable", mImagePath.toString());
                    if (mDrawableResID == 0) {
                        mDrawableResID = res.getIdentifier("album_icon", "raw", mImagePath.toString());
                        if (mDrawableResID == 0) {
                            mDrawableResID = res.getIdentifier("album_icon", "drawable", mImagePath.toString());
                            ivThemeBg.setImageDrawable(res.getDrawable(mDrawableResID));
                        } else {
                            File Gif_Preview = new File(con.getFilesDir().getAbsolutePath() + "/Gif_Preview/");
                            if (!Gif_Preview.exists()) {
                                Gif_Preview.mkdir();
                            }
                            final File file = new File(Gif_Preview + "/" + mImagePath.toString() + ".gif");
                            if (!file.exists()) {
                                InputStream in = res.openRawResource(mDrawableResID);
                                FileOutputStream out = null;
                                try {
                                    out = new FileOutputStream(file.getAbsolutePath());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                byte[] buff = new byte[1024];
                                int read = 0;
                                try {
                                    while ((read = in.read(buff)) > 0) {
                                        out.write(buff, 0, read);
                                    }
                                } catch (IOException e) {
                                } finally {
                                    try {
                                        in.close();
                                        out.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                            final GlideTaskParams<ImageView> params = new GlideTaskParams<>(ivThemeBg, "" + Uri.fromFile(file));
                            params.setListener(new IDrawableLoaderTaskListener<ImageView, Drawable>() {
                                @Override
                                public void success(@NonNull ImageView imageView, @NonNull Drawable drawable) {
                                    imageView.setImageDrawable(drawable);
                                }

                                @Override
                                public void failure(@NonNull ImageView imageView, @NonNull Drawable drawable) {

                                }
                            });

                            params.setPlaceholder(mPlaceHolder);
                            GifRequestBuilder<String> requestBuilder = Glide.with((con)).load(params.getPath()).asGif().diskCacheStrategy(DiskCacheStrategy.ALL);
                            load(applyDimens(requestBuilder, params), params);

                        }
                    } else {
                        ivThemeBg.setImageDrawable(res.getDrawable(mDrawableResID));
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        setTextColor();

        setBackgroundColor();

        setBottomCorners(mBtnGroupView);
    }

//    private void loadFB_NativeBannerADs() {
//        nativeBannerAd1 = FbNativeBanner.getFbNativeAd(con);
//        if (nativeBannerAd1 != null && nativeBannerAd1.isAdLoaded()) {
//            inflateAdSmall(nativeBannerAd1);
//        } else {
//            nativeBannerAd1 = new NativeBannerAd(con, con.getString(R.string.fb_native_banner));
//            nativeBannerAd1.setAdListener(new NativeAdListener() {
//
//                @Override
//                public void onMediaDownloaded(Ad ad) {
//
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//
//                    // Race condition, load() called again before last ad was displayed
//                    if (nativeBannerAd1 == null || nativeBannerAd1 != ad) {
//                        return;
//                    }
//                    // Inflate Native Banner Ad into Container
//                    inflateAdSmall(nativeBannerAd1);
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//
//                }
//
//            });
//            // load the ad
//            nativeBannerAd1.loadAd();
//        }
//    }
//
//    private void inflateAdSmall(NativeBannerAd nativeBannerAd) {
//        // Unregister last ad
//        nativeBannerAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//        nativeAdLayout1 = (NativeAdLayout) findViewById(R.id.native_banner_ad_container);
//        nativeAdLayout1.setVisibility(View.VISIBLE);
//        admob_native_template.setVisibility(GONE);
//        LayoutInflater inflater = LayoutInflater.from(con);
//        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
//        adView1 = (LinearLayout) inflater.inflate(R.layout.fb_native_dialog, nativeAdLayout1, false);
//        nativeAdLayout1.addView(adView1);
//
//        // Add the AdChoices icon
//        RelativeLayout adChoicesContainer = (RelativeLayout) adView1.findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(con, nativeBannerAd, nativeAdLayout1);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        TextView nativeAdTitle = (TextView) adView1.findViewById(R.id.native_ad_title);
//        TextView nativeAdSocialContext = (TextView) adView1.findViewById(R.id.native_ad_social_context);
//        TextView sponsoredLabel = (TextView) adView1.findViewById(R.id.native_ad_sponsored_label);
//        AdIconView nativeAdIconView = (AdIconView) adView1.findViewById(R.id.native_icon_view);
//        Button nativeAdCallToAction = (Button) adView1.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
//        nativeAdCallToAction.setVisibility(
//                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
//        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
//        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());
//
//        // Register the Title and CTA button to listen for clicks.
//        List<View> clickableViews = new ArrayList<>();
//        Log.w("inflateAdSmall", "");
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//        nativeBannerAd.registerViewForInteraction(adView1, nativeAdIconView, clickableViews);
//    }

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
        if (R.id.lay_apply == id) {
            mApplyListener.onClick(this);
        } else if (R.id.lay_edit == id) {
            mEditListener.onClick(this);
        } else if (R.id.lay_share == id) {
            mShareListener.onClick(this);
        } else if (R.id.lay_delete == id) {
            mDeleteListener.onClick(this);
        } else if (R.id.lay_upload == id) {
            mUploadListener.onClick(this);
        }
    }

    public ThemeDialog setAnimationEnable(boolean enable) {
        mIsShowAnim = enable;
        return this;
    }

    public ThemeDialog setAnimationIn(AnimationSet animIn) {
        mAnimIn = animIn;
        return this;
    }

    public ThemeDialog setAnimationOut(AnimationSet animOut) {
        mAnimOut = animOut;
        initAnimListener();
        return this;
    }

    public ThemeDialog setColor(int color) {
        mBackgroundColor = color;
        return this;
    }

    public ThemeDialog setImagePath(String path, int place_re) {
        mImagePath = path;
        mPlaceHolder = place_re;
        return this;
    }

    public ThemeDialog setColor(String color) {
        try {
            setColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ThemeDialog setTitleTextColor(int color) {
        mTitleTextColor = color;
        return this;
    }

    public ThemeDialog setTitleTextColor(String color) {
        try {
            setTitleTextColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ThemeDialog setContentTextColor(int color) {
        mContentTextColor = color;
        return this;
    }

    public ThemeDialog setContentTextColor(String color) {
        try {
            setContentTextColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }


    public ThemeDialog setApplyListener(OnApplyListener l) {
        mApplyListener = l;
        return this;
    }

    public ThemeDialog setEditListener(OnEditListener l) {
        mEditListener = l;
        return this;
    }

    public ThemeDialog setDeleteListener(OnDeleteListener l) {
        mDeleteListener = l;
        return this;
    }

    public ThemeDialog setUploadListener(OnUploadListener l) {
        mUploadListener = l;
        return this;
    }
//        i

    public ThemeDialog setShareListener(OnShareListener l) {
        mShareListener = l;
        return this;
    }

    public ThemeDialog setContentImage(Drawable drawable) {
        mDrawable = drawable;
        return this;
    }

    public ThemeDialog setContentImage(Bitmap bitmap) {
        mContentBitmap = bitmap;
        return this;
    }

    public ThemeDialog setContentImage(int resId) {
        mResId = resId;
        return this;
    }

    public CharSequence getTitleText() {
        return mTitleText;
    }


    public interface OnEditListener {
        void onClick(ThemeDialog dialog);
    }

    public interface OnShareListener {
        void onClick(ThemeDialog dialog);
    }

    public interface OnApplyListener {
        void onClick(ThemeDialog dialog);
    }

    public interface OnDismissListener {
        void onClick(ThemeDialog dialog);
    }

    public interface OnDeleteListener {
        void onClick(ThemeDialog dialog);
    }

    public interface OnUploadListener {
        void onClick(ThemeDialog dialog);
    }

   /* private void loadFB_NativeBannerADs() {
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
        nativeAdLayout1 = (NativeAdLayout) findViewById(R.id.native_banner_ad_container);
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
