package com.example.stickers.repository

import com.example.stickers.network.RetroService

class StickerFullScreenRepository constructor(private val retrofitService: RetroService) {

    fun getAllStickers(stickerId: String) = retrofitService.getAllItemSelectedStickers("baifpgjoi53495-34954jnfgldjflgdjfl","Grow@#123",stickerId)
}