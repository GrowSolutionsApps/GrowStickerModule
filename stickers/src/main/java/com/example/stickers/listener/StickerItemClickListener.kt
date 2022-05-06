package com.example.stickers.listener

interface StickerItemClickListener {

    fun click(position : Int,stickerId: String,stickerName: String,isVip: String)
}