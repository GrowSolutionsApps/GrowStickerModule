package com.example.stickers.utils;

import static com.example.stickers.utils.UtilsKt.getFilePath;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.stickers.identities.StickerPacksContainer;
import com.example.stickers.view.TouchA;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StickerPacksManager {
    public static StickerPacksContainer stickerPacksContainer;

    public static void createStickerImageFile(Uri uri, Uri uri2, Context context, CompressFormat compressFormat) {
        try {
            File file = new File(uri2.getPath());
            byte[] compressImageToBytes = ImageUtils.compressImageToBytes(uri, 70, 512, 512, context, compressFormat);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(compressImageToBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public static void createStickerPackTrayIconFile(Uri uri, Uri uri2, Context context) {

        Log.w("msg", "whatsapp web a2- file createStickerPackTrayIconFile--" + uri);

        try {
            File file = new File(uri2.getPath());
            byte[] compressImageToBytes = ImageUtils.compressImageToBytes(uri, 80, 96, 96, context, CompressFormat.PNG);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(compressImageToBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }


//    public static List<StickerPack> getStickerPacks(Context context) {
//        List<StickerPack> arrayList = new ArrayList<>();
//        if (RequestPermissionsHelper.verifyPermissions(context)) {
//            try {
//                String filePath = "";
//                try {
////                    Changes By Arti V1.16  //By Jhanvi V_1.21
////                    if (MainApp.mApp != null) {
////                        filePath = getFilePath(context);
//                    FileInputStream fileInputStream = new FileInputStream(new File(TouchA.a(new StringBuilder(), context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Emoji Key Keyboard StaticData/" + "stickerPacks/", StickerContentProvider.CONTENT_FILE_NAME)));
//                    Log.w("msg", " whatsapp web fileInputStream--------" + fileInputStream.toString());
//                    arrayList = ContentFileParser.parseStickerPacks(fileInputStream);
//                    fileInputStream.close();
////                    } else {
////                        filePath = Utils.getFilePath(context);
////                    }
//                } catch (Exception e) {
//
//                }
//                if (!filePath.equals("")) {
//                    FileInputStream fileInputStream = new FileInputStream(new File(TouchA.a(new StringBuilder(), filePath + "stickerPacks/", StickerContentProvider.CONTENT_FILE_NAME)));
////                FileInputStream fileInputStream = new FileInputStream(new File(TouchA.a(new StringBuilder(), WAConstants.STICKERS_DIRECTORY_PATH, StickerContentProvider.CONTENT_FILE_NAME)));
//                    arrayList = ContentFileParser.parseStickerPacks(fileInputStream);
//                    fileInputStream.close();
//                }
//            } catch (IOException | IllegalStateException e) {
//                StringBuilder a2 = TouchA.a("contents.json file has some issues: ");
//                a2.append(e.getMessage());
//                Log.i("Content provider: ", a2.toString());
//            } catch (Exception th) {
//            }
//        }
//        return arrayList;
//    }
public static List<StickerPack> getStickerPacks(Context context) {
    List<StickerPack> arrayList = new ArrayList<>();
    try {
        FileInputStream fileInputStream = new FileInputStream(new File(TouchA.a(new StringBuilder(),
                getFilePath(context), StickerContentProvider.CONTENT_FILE_NAME)));
        Log.w("msg", " whatsapp web fileInputStream--------" + fileInputStream.toString());

        arrayList = ContentFileParser.parseStickerPacks(fileInputStream);
        fileInputStream.close();
    } catch (IOException | IllegalStateException e) {
        StringBuilder a2 = TouchA.a("contents.json file has some issues: ");
        a2.append(e.getMessage());
        Log.i("Content provider: ", a2.toString());
    } catch (Throwable th) {
        throw th;
    }
    return arrayList;
}
    public static void saveStickerFilesLocally(Sticker sticker, Uri uri, String str, Context context) {
        StringBuilder b = TouchA.b(str, "/");
        b.append(sticker.imageFileName);
        Log.w("msg", "whatsapp web a2- b--" + b);
        Log.w("msg", "whatsapp web a2- file dataaa--" + Uri.parse(b.toString()));
        Log.w("msg", "uri " + uri);

        createStickerImageFile(uri, Uri.parse(b.toString()), context, CompressFormat.WEBP);

    }

    public static List<Sticker> saveStickerPackFilesLocally(String str, List<Uri> list, Context context) {
        String a2 = TouchA.a(new StringBuilder(), getFilePath(context), str);
        Log.w("msg", " whatsapp web a2--------" + a2);

        ArrayList arrayList = new ArrayList();
        File file = new File(a2);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (Uri uri : list) {
            StringBuilder sb = new StringBuilder();
            sb.append(WAFileUtils.generateRandomIdentifier());
            sb.append(".webp");
            Sticker sticker = new Sticker(sb.toString(), null);
            arrayList.add(sticker);
            saveStickerFilesLocally(sticker, uri, a2, context);
        }
        return arrayList;
    }

    public static void saveStickerPacksToJson(StickerPacksContainer stickerPacksContainer2,Context context) {

        String json = new Gson().toJson((Object) stickerPacksContainer2);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getFilePath(context));
            sb.append("contents.json");
            Log.w("msg", " whatsapp web saveStickerPacksToJson--------" + sb.toString());

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(sb.toString())));
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
//            e.printStackTrace();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("saveStickerPacksToJson: ");
            sb2.append(e.toString());
            Log.i("isStickerPackIsNull", sb2.toString());
        }
    }
}
