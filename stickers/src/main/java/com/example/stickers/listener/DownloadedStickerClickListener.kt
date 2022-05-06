package com.example.stickers.listener

import com.example.stickers.model.StickersListDownload
import java.util.ArrayList

interface DownloadedStickerClickListener {

    fun click(position: Int,name: String, arrayList: ArrayList<StickersListDownload>,deletePath: String)
}