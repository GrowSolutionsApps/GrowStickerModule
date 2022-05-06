package com.example.stickers.utils;

import static com.example.stickers.utils.UtilsKt.getFilePath;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stickers.view.TouchA;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StickerContentProvider extends ContentProvider {
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
    public static final String METADATA = "metadata";
    public static Uri AUTHORITY_URI = new Builder().scheme("content")
            .authority("com.example.mvvmstickermodule.stickercontentprovider")
            .appendPath(METADATA).build();
    public static final String CONTENT_FILE_NAME = "contents.json";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
    public static final UriMatcher MATCHER = new UriMatcher(-1);
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";
    public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    public static final int STICKER_PACK_TRAY_ICON_CODE = 5;
    public List<StickerPack> stickerPackList;

    private File fetchFile(@NonNull String str, @NonNull String str2) {
        StringBuilder sb = new StringBuilder();
//        sb.append(Data.file_path_without + "/.emojipack/" + "Pentol" + "/Pentol/1.webp");
//        sb.append(WAConstants.STICKERS_DIRECTORY_PATH);
        sb.append(getFilePath(getContext()));
//        if (getContext() != null) {
//            sb.append(getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Emoji Key Keyboard StaticData/" /*+ "stickerPacks/"*/);
//        }
        sb.append(str2);
        sb.append("/");
        sb.append(str);
        Log.w("msg", "whatsapp web1--------- " + sb.toString());
        return new File(sb.toString());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        for (StickerPack stickerPack : getStickerPackList()) {
            if (lastPathSegment.equals(stickerPack.identifier)) {
                return getStickerPackInfo(uri, Collections.singletonList(stickerPack));
            }
        }
        return getStickerPackInfo(uri, new ArrayList());
    }

    private File getImageAsset(Uri uri) throws IllegalArgumentException {
        List pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 3) {
            String str = (String) pathSegments.get(pathSegments.size() - 1);
            String str2 = (String) pathSegments.get(pathSegments.size() - 2);
            if (TextUtils.isEmpty(str2)) {
                throw new IllegalArgumentException(TouchA.a("identifier is empty, uri: ", uri));
            } else if (!TextUtils.isEmpty(str)) {
                for (StickerPack stickerPack : getStickerPackList()) {
                    if (str2.equals(stickerPack.identifier)) {
                        if (str.equals(stickerPack.trayImageFile)) {
                            return fetchFile(str, str2);
                        }
                        for (Sticker sticker : stickerPack.getStickers()) {
                            if (str.equals(sticker.imageFileName)) {
                                return fetchFile(str, str2);
                            }
                        }
                        continue;
                    }
                }
                return null;
            } else {
                throw new IllegalArgumentException(TouchA.a("file name is empty, uri: ", uri));
            }
        } else {
            throw new IllegalArgumentException(TouchA.a("path segments should be 3, uri is: ", uri));
        }
    }

    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, getStickerPackList());
    }

    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, @NonNull List<StickerPack> list) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{STICKER_PACK_IDENTIFIER_IN_QUERY, "sticker_pack_name", STICKER_PACK_PUBLISHER_IN_QUERY,
                STICKER_PACK_ICON_IN_QUERY, ANDROID_APP_DOWNLOAD_LINK_IN_QUERY, IOS_APP_DOWNLOAD_LINK_IN_QUERY, PUBLISHER_EMAIL, PUBLISHER_WEBSITE,
                PRIVACY_POLICY_WEBSITE, LICENSE_AGREENMENT_WEBSITE});
        for (StickerPack stickerPack : list) {
            RowBuilder newRow = matrixCursor.newRow();
            newRow.add(stickerPack.identifier);
            newRow.add(stickerPack.name);
            newRow.add(stickerPack.publisher);
            newRow.add(stickerPack.trayImageFile);
            newRow.add(stickerPack.b);
            newRow.add(stickerPack.f1369a);
            newRow.add(stickerPack.publisherEmail);
            newRow.add(stickerPack.publisherWebsite);
            newRow.add(stickerPack.privacyPolicyWebsite);
            newRow.add(stickerPack.licenseAgreementWebsite);
        }
        Context context = getContext();
        context.getClass();
        matrixCursor.setNotificationUri(context.getContentResolver(), uri);
        return matrixCursor;
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        Log.w("msg", "whatsapp web a2- file getStickersForAStickerPack--");

        MatrixCursor matrixCursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY});
        for (StickerPack stickerPack : getStickerPackList()) {
            if (lastPathSegment.equals(stickerPack.identifier)) {
                for (Sticker sticker : stickerPack.getStickers()) {
                    matrixCursor.addRow(new Object[]{sticker.imageFileName, TextUtils.join(/*DataBaseEventsStorage.COMMA_SEP*/",", sticker.f1368a)});
                }
            }
        }
        Context context = getContext();
        context.getClass();
        matrixCursor.setNotificationUri(context.getContentResolver(), uri);
        return matrixCursor;
    }

    private synchronized void readContentFile(@NonNull Context context) {
        this.stickerPackList = StickerPacksManager.getStickerPacks(context);
    }

    public int delete(@NonNull Uri uri, @Nullable String str, String[] strArr) {
        throw new UnsupportedOperationException("Not supported");
    }

    public List<StickerPack> getStickerPackList() {
        Context context = getContext();
        context.getClass();
        readContentFile(context);
        return this.stickerPackList;
    }

    public String getType(@NonNull Uri uri) {
        int match = MATCHER.match(uri);
        if (match == 1) {
            return "vnd.android.cursor.dir/vnd.com.example.mvvmstickermodule.stickercontentprovider.metadata";
        }
        if (match == 2) {
            return "vnd.android.cursor.item/vnd.com.example.mvvmstickermodule.stickercontentprovider.metadata";
        }
        if (match == 3) {
            return "vnd.android.cursor.dir/vnd.com.example.mvvmstickermodule.stickercontentprovider.stickers";
        }
        if (match == 4) {
            return "image/webp";
        }
        if (match == 5) {
            return "image/png";
        }
        throw new IllegalArgumentException(TouchA.a("Unknown URI: ", uri));
    }

    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        Context context = getContext();
        context.getClass();
        String packageName = context.getPackageName();
        String str = "com.example.mvvmstickermodule.stickercontentprovider";
        if (str.startsWith(packageName)) {
            StickerPack stickerPack = (StickerPack) new Gson().fromJson((String) contentValues.get("stickerPack"), StickerPack.class);
            UriMatcher uriMatcher = MATCHER;
            String str2 = "stickers_asset/";
            StringBuilder a2 = TouchA.a(str2);
            a2.append(stickerPack.identifier);
            String str3 = "/";
            a2.append(str3);
            a2.append(stickerPack.trayImageFile);
            uriMatcher.addURI(str, a2.toString(), 5);
            for (Sticker sticker : stickerPack.getStickers()) {
                UriMatcher uriMatcher2 = MATCHER;
                StringBuilder a3 = TouchA.a(str2);
                a3.append(stickerPack.identifier);
                a3.append(str3);
                a3.append(sticker.imageFileName);
                uriMatcher2.addURI(str, a3.toString(), 4);
            }
            return uri;
        }
        StringBuilder a4 = TouchA.a("your authority (com.example.mvvmstickermodule.stickercontentprovider) for the content provider should start with your package name: ");
        a4.append(getContext().getPackageName());
        throw new IllegalStateException(a4.toString());
    }

    public boolean onCreate() {
        Context context = getContext();
        context.getClass();
        String packageName = context.getPackageName();
        String str = "com.example.mvvmstickermodule.stickercontentprovider";
        if (str.startsWith(packageName)) {
            MATCHER.addURI(str, METADATA, 1);
            MATCHER.addURI(str, "metadata/*", 2);
            MATCHER.addURI(str, "stickers/*", 3);
            for (StickerPack stickerPack : getStickerPackList()) {
                UriMatcher uriMatcher = MATCHER;
                String str2 = "stickers_asset/";
                StringBuilder a2 = TouchA.a(str2);
                a2.append(stickerPack.identifier);
                String str3 = "/";
                a2.append(str3);
                a2.append(stickerPack.trayImageFile);
                uriMatcher.addURI(str, a2.toString(), 5);
                for (Sticker sticker : stickerPack.getStickers()) {
                    UriMatcher uriMatcher2 = MATCHER;
                    StringBuilder a3 = TouchA.a(str2);
                    a3.append(stickerPack.identifier);
                    a3.append(str3);
                    a3.append(sticker.imageFileName);
                    uriMatcher2.addURI(str, a3.toString(), 4);
                }
            }
            return true;
        }
        StringBuilder a4 = TouchA.a("your authority (com.example.mvvmstickermodule.stickercontentprovider) for the content provider should start with your package name: ");
        a4.append(getContext().getPackageName());
        throw new IllegalStateException(a4.toString());
    }

    @Nullable
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String str) {
        int match = MATCHER.match(uri);
        ParcelFileDescriptor parcelFileDescriptor = null;
        if (match != 4 && match != 5) {
            return null;
        }
        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(getImageAsset(uri), 268435456);
        } catch (FileNotFoundException e) {
            Log.w("msg","FileNotFoundException : " +e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("msg","Exception : " +e.getMessage());
        }
        return new ParcelFileDescriptor(parcelFileDescriptor);
    }

    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, String str, String[] strArr2, String str2) {
        this.stickerPackList = StickerPacksManager.getStickerPacks(getContext());
        int match = MATCHER.match(uri);
        if (match == 1) {
            return getPackForAllStickerPacks(uri);
        }
        if (match == 2) {
            return getCursorForSingleStickerPack(uri);
        }
        if (match == 3) {
            return getStickersForAStickerPack(uri);
        }
        throw new IllegalArgumentException(TouchA.a("Unknown URI: ", uri));
    }

    public int update(@NonNull Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("Not supported");
    }
}
