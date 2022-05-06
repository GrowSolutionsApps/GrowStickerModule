package com.example.stickers.helper;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.stickers.view.TouchA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

public class FileHelper {
    public static String TAG;
    private static Throwable th;

    static {
        StringBuilder a2 = TouchA.a("JNP__");
        a2.append(FileHelper.class.getSimpleName());
        TAG = a2.toString();
    }

    public static void copyFile(@NonNull String str, @NonNull String str2) throws Throwable {
        FileChannel fileChannel;
        if (!str.equalsIgnoreCase(str2)) {
            FileChannel fileChannel2 = null;
            try {
                FileChannel channel = new FileInputStream(new File(str)).getChannel();
                try {
                    fileChannel = new FileOutputStream(new File(str2)).getChannel();
                    try {
                        channel.transferTo(0, channel.size(), fileChannel);
                        channel.close();
                        channel.close();
                        if (fileChannel != null) {
                            fileChannel.close();
                        }
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        fileChannel2 = channel;
                        th = th2;
                        if (fileChannel2 != null) {
                        }
                        if (fileChannel != null) {
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    fileChannel2 = channel;
                    Throwable th = th3;
                    fileChannel = null;
                    if (fileChannel2 != null) {
                        fileChannel2.close();
                    }
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                Throwable th = th4;
                fileChannel = null;
                if (fileChannel2 != null) {
                }
                if (fileChannel != null) {
                }
                throw th;
            }
        }
    }

    public static boolean delete(String str) {
        try {
            return new File(str).delete();
        } catch (Exception unused) {
            return false;
        }
    }

    @SuppressLint({"DefaultLocale"})
    public static String formatFileSize(long j) {
        if (j < 1024) {
            return String.format("%d B", new Object[]{Long.valueOf(j)});
        } else if (j < 1048576) {
            return String.format("%.1f KB", new Object[]{Float.valueOf(((float) j) / 1024.0f)});
        } else if (j < 1073741824) {
            return String.format("%.1f MB", new Object[]{Float.valueOf((((float) j) / 1024.0f) / 1024.0f)});
        } else if (j < 0) {
            return String.format("%.1f GB", new Object[]{Float.valueOf(((((float) j) / 1024.0f) / 1024.0f) / 1024.0f)});
        } else {
            return String.format("%.1f TB", new Object[]{Float.valueOf((((((float) j) / 1024.0f) / 1024.0f) / 1024.0f) / 1024.0f)});
        }
    }


    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor cursor;
        String str2 = "_data";
        try {
            cursor = context.getContentResolver().query(uri, new String[]{str2}, str, strArr, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String string = cursor.getString(cursor.getColumnIndexOrThrow(str2));
                        cursor.close();
                        return string;
                    }
                } catch (IllegalArgumentException e) {
                    e = e;
                    try {
                        Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", new Object[]{e.getMessage()}));
                    } catch (Throwable th) {
                        th = th;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            }
        } catch (IllegalArgumentException e2) {
            IllegalArgumentException e = e2;
            cursor = null;
            Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", new Object[]{e.getMessage()}));
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
            if (cursor != null) {
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return str2;
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if (!(VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if (!"content".equalsIgnoreCase(uri.getScheme())) {
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            } else if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            } else {
                return getDataColumn(context, uri, null, null);
            }
        } else {
            String str = ":";
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(str);
                if ("primary".equalsIgnoreCase(split[0])) {
                    StringBuilder sb = new StringBuilder();
//                    sb.append(getFilePath(context));
                    sb.append(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Emoji Key Keyboard StaticData/" /*+ "stickerPacks"*/);
                    sb.append("/");
                    sb.append(split[1]);
                    return sb.toString();
//                    sb.append(Environment.getExternalStorageDirectory());
//                    sb.append("/");
//                    sb.append(split[1]);
//                    return sb.toString();
                }
            } else if (isDownloadsDocument(uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(documentId)) {
                    try {
                        return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                                Long.valueOf(documentId).longValue()), null, null);
                    } catch (NumberFormatException e) {
                        Log.i(TAG, e.getMessage());
                        return null;
                    }
                }
            } else if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(str);
                String str2 = split2[0];
                if ("image".equals(str2)) {  //  extra priyanka crash v1.15  //By Jhanvi V_1.21
                    if (VERSION.SDK_INT >= 29) {
                        uri2 = Media.getContentUri("external");
                    } else {
                        uri2 = Media.EXTERNAL_CONTENT_URI;
                    }
                } else if ("video".equals(str2)) {
                    if (VERSION.SDK_INT >= 29) {
                        uri2 = Video.Media.getContentUri("external");
                    } else {
                        uri2 = Video.Media.EXTERNAL_CONTENT_URI;
                    }
                } else if ("audio".equals(str2)) {
                    if (VERSION.SDK_INT >= 29) {
                        uri2 = Audio.Media.getContentUri("external");
                    } else {
                        uri2 = Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                }
                return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
            }
        }
        return null;
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isExtension(String str, String str2) {
        if (str2.equalsIgnoreCase(str.substring(str.lastIndexOf(".") + 1, str.length()))) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static ArrayList<String> loadFiles(String str) {
        Log.w("msg","str : " + str);
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(str);
        Log.w("msg","str file : " + file);

        String[] list = file.exists() ? file.list() : null;
        if (list != null) {
            for (String append : list) {
                StringBuilder sb = new StringBuilder();
                sb.append(file.getPath());
                sb.append("/");
                sb.append(append);
                arrayList.add(sb.toString());
            }
        }
        Log.w("msg","str arrayList : " + arrayList);
        return arrayList;
    }

    public static ArrayList<String> loadPackFiles(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(str);
        String[] list = file.exists() ? file.list() : null;
        if (list != null) {
            for (String append : list) {
                StringBuilder sb = new StringBuilder();
                sb.append(file.getPath());
                sb.append("/");
                sb.append(append);
                String sb2 = sb.toString();
                if (loadFiles(sb2).size() > 0) {
                    arrayList.add(sb2);
                } else {
                    delete(sb2);
                }
            }
        }
        return arrayList;
    }

    public static boolean delete(Context context, Uri uri) {
        return new File(getPath(context, uri)).delete();
    }
}
