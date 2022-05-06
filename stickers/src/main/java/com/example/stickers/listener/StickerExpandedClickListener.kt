package com.example.stickers.listener

import android.view.View
import com.example.stickers.model.PreviewItem
import com.example.stickers.model.StickersListDownload

interface StickerExpandedClickListener {

    fun onClickExpand(
        position: Int,
        stickerItemPreview: View,
        allStickers: MutableList<PreviewItem>
    )

    fun onDownloadedClickExpand(
        position: Int,
        stickerItemPreview: View,
        allStickers: MutableList<StickersListDownload>
    )


}