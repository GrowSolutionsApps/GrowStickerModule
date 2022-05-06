package com.example.stickers.utils;

import static com.example.stickers.utils.UtilsKt.getFilePath;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;


import com.example.stickers.view.TouchA;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Random;

public class WAFileUtils {
    public static void deleteFile(String str, Context context) {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        context.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, TouchA.a("_data='", str, "'"), null);
    }

    public static void deleteFolder(String str) {
        File file = new File(str);
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    deleteFolder(listFiles[i].getPath());
                } else {
                    listFiles[i].delete();
                }
            }
        }
        file.delete();
    }

    public static String generateRandomIdentifier() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(random.nextInt(35)));
        }
        return sb.toString();
    }

    public static String getImageRealPathFromURI(Context context, Uri uri) {
        String str = "_data";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{str}, null, null, null);
            if (cursor != null) {
                cursor.getClass();
                int columnIndexOrThrow = cursor.getColumnIndexOrThrow(str);
                cursor.moveToFirst();
                String string = cursor.getString(columnIndexOrThrow);
                cursor.close();
                return string;
            }
            String path = uri.getPath();
            if (cursor != null) {
                cursor.close();
            }
            return path;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static void initializeDirectories(Context context) {
        File file = new File(getFilePath(context));
        Log.w("msg"," whatsapp web file1--------" + file.getAbsolutePath());

        if (!file.exists()) {
            file.mkdirs();
            String str = "{\"androidPlayStoreLink\": \"\",\"iosAppStoreLink\": \"\",\"stickerPacks\": [ ]}";
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getFilePath(context));
                sb.append("contents.json");
                PrintWriter printWriter = new PrintWriter(sb.toString());
                printWriter.write(str);
                printWriter.close();
            } catch (FileNotFoundException e) {
                Log.w("msg","initializeDirectories error : " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
