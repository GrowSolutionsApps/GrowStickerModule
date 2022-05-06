package com.example.stickers.utils;


import com.example.stickers.model.DownloadedStickersDataModel;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class StickerAddOnComparator implements Serializable, Comparator<DownloadedStickersDataModel> {
    static final long serialVersionUID = 1276823;

    public StickerAddOnComparator() {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public int compare(DownloadedStickersDataModel stickerAddOn, DownloadedStickersDataModel stickerAddOn2) {
        if (stickerAddOn == null && stickerAddOn2 == null) {
            return 0;
        }
        if (stickerAddOn == null) {
            return 1;
        }
        if (stickerAddOn2 == null) {
            return -1;
        }
        if (stickerAddOn.get_index() == stickerAddOn2.get_index()) {
            return (int) (new File(stickerAddOn2.get_filePath()).lastModified() - new File(stickerAddOn.get_filePath()).lastModified());
        } else if (stickerAddOn.get_index() == DownloadedStickersDataModel.DEFAULT_INDEX) {
            return stickerAddOn2.get_index() - stickerAddOn.get_index();
        } else {
            return stickerAddOn.get_index() - stickerAddOn2.get_index();
        }
    }
}