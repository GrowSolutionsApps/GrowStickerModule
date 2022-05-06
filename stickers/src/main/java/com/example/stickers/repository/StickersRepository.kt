package com.example.stickers.repository

import com.example.stickers.network.RetroService

class StickersRepository constructor(private val retrofitService: RetroService) {

    fun getAllOnlineStickers() = retrofitService.getOnlineStickers("baifpgjoi53495-34954jnfgldjflgdjfl","Grow@#123","hi","1")
}