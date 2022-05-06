package com.example.stickers.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.stickers.model.DownloadedStickersDataModel;
import com.example.stickers.preference.PreferenceManager;

import java.util.Collections;
import java.util.List;

public class StickerUtil {

    public static void saveStickerSort(Context context, List<DownloadedStickersDataModel> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (DownloadedStickersDataModel stickerAddOn : list) {
            stringBuilder.append(stickerAddOn.getId());
            if (!stickerAddOn.equals(list.get(list.size() - 1))) {
                stringBuilder.append(",");
            }
        }
        PreferenceManager.saveData(context, "sticker_addon_sort", stringBuilder.toString());
//        SharedPrefsUtil.setStickerAddOnSort(context, stringBuilder.toString());
    }
    public static void sortStickers(Context context, List<DownloadedStickersDataModel> list) {
        String stickerAddOnSort = PreferenceManager.getStringData(context,"sticker_addon_sort");
//        String stickerAddOnSort = SharedPrefsUtil.getStickerAddOnSort(context);
        if (!TextUtils.isEmpty(stickerAddOnSort)) {
            String[] split = stickerAddOnSort.split(",");
            for (DownloadedStickersDataModel stickerAddOn : list) {
                for (int i = 0; i < split.length; i++) {
                    if (Integer.valueOf(split[i]).intValue() == stickerAddOn.getId()) {
                        stickerAddOn.set_index(i);
                    }
                }
            }
        }
        Collections.sort(list, new StickerAddOnComparator());
        saveStickerSort(context, list);
    }
}
