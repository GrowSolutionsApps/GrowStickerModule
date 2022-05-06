package com.example.stickers.utils;

import android.text.TextUtils;
import android.util.JsonReader;

import androidx.annotation.NonNull;


import com.example.stickers.view.TouchA;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ContentFileParser {
    public static final int LIMIT_EMOJI_COUNT = 2;


    @NonNull
    public static List<StickerPack> parseStickerPacks(@NonNull InputStream inputStream) throws IOException, IllegalStateException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        List<StickerPack> readStickerPacks = readStickerPacks(jsonReader);
        jsonReader.close();
        return readStickerPacks;
    }

    @NonNull
    public static StickerPack readStickerPack(@NonNull JsonReader jsonReader) throws IOException, IllegalStateException {
        jsonReader.beginObject();
        List list = null;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            char c = 65535;
            switch (nextName.hashCode()) {
                case -1926372609:
                    if (nextName.equals("publisherWebsite")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1704800878:
                    if (nextName.equals("licenseAgreementWebsite")) {
                        c = 7;
                        break;
                    }
                    break;
                case -1618432855:
                    if (nextName.equals( "identifier")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1527982367:
                    if (nextName.equals("trayImageFile")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1301413791:
                    if (nextName.equals("privacyPolicyWebsite")) {
                        c = 6;
                        break;
                    }
                    break;
                case 3373707:
                    if (nextName.equals("name")) {
                        c = 1;
                        break;
                    }
                    break;
                case 991663872:
                    if (nextName.equals("publisherEmail")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1447404028:
                    if (nextName.equals( "publisher")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1531715286:
                    if (nextName.equals("stickers")) {
                        c = 8;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    str = jsonReader.nextString();
                    break;
                case 1:
                    str2 = jsonReader.nextString();
                    break;
                case 2:
                    str3 = jsonReader.nextString();
                    break;
                case 3:
                    str4 = jsonReader.nextString();
                    break;
                case 4:
                    str5 = jsonReader.nextString();
                    break;
                case 5:
                    str6 = jsonReader.nextString();
                    break;
                case 6:
                    str7 = jsonReader.nextString();
                    break;
                case 7:
                    str8 = jsonReader.nextString();
                    break;
                case 8:
                    list = readStickers(jsonReader);
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        if (TextUtils.isEmpty(str)) {
            throw new IllegalStateException("identifier cannot be empty");
        } else if (TextUtils.isEmpty(str2)) {
            throw new IllegalStateException("name cannot be empty");
        } else if (TextUtils.isEmpty(str3)) {
            throw new IllegalStateException("publisher cannot be empty");
        } else if (TextUtils.isEmpty(str4)) {
            throw new IllegalStateException("tray_image_file cannot be empty");
        } else if (list == null || list.size() == 0) {
            throw new IllegalStateException("sticker list is empty");
        } else if (str.contains("..") || str.contains("/")) {
            throw new IllegalStateException("identifier should not contain .. or / to prevent directory traversal");
        } else {
            jsonReader.endObject();
            StickerPack stickerPack = new StickerPack(str, str2, str3, str4, str5, str6, str7, str8);
            stickerPack.setStickers(list);
            return stickerPack;
        }
    }

    @NonNull
    public static List<StickerPack> readStickerPacks(@NonNull JsonReader jsonReader) throws IOException, IllegalStateException {
        ArrayList<StickerPack> arrayList = new ArrayList<>();
        jsonReader.beginObject();
        String str = null;
        String str2 = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if ("androidPlayStoreLink".equals(nextName)) {
                str = jsonReader.nextString();
            } else if ("iosAppStoreLink".equals(nextName)) {
                str2 = jsonReader.nextString();
            } else if ("stickerPacks".equals(nextName)) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    arrayList.add(readStickerPack(jsonReader));
                }
                jsonReader.endArray();
            } else {
                throw new IllegalStateException(TouchA.a("unknown field in json: ", nextName));
            }
        }
        jsonReader.endObject();
        if (arrayList.size() != 0) {
            for (StickerPack stickerPack : arrayList) {
                stickerPack.setAndroidPlayStoreLink(str);
                stickerPack.setIosAppStoreLink(str2);
            }
            return arrayList;
        }
        throw new IllegalStateException("sticker pack list cannot be empty");
    }

    @NonNull
    public static List<Sticker> readStickers(@NonNull JsonReader jsonReader) throws IOException, IllegalStateException {
        jsonReader.beginArray();
        ArrayList arrayList = new ArrayList();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            String str = null;
            ArrayList arrayList2 = new ArrayList(2);
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                if ("imageFileName".equals(nextName)) {
                    str = jsonReader.nextString();
                } else if ("emojis".equals(nextName)) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        arrayList2.add(jsonReader.nextString());
                    }
                    jsonReader.endArray();
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            if (TextUtils.isEmpty(str)) {
                throw new IllegalStateException("sticker image_file cannot be empty");
            } else if (!str.endsWith(".webp")) {
                throw new IllegalStateException(TouchA.a("image file for stickers should be webp files, image file is: ", str));
            } else if (str.contains("..") || str.contains("/")) {
                throw new IllegalStateException(TouchA.a("the file name should not contain .. or / to prevent directory traversal, image file is:", str));
            } else {
                arrayList.add(new Sticker(str, arrayList2));
            }
        }
        jsonReader.endArray();
        return arrayList;
    }
}
