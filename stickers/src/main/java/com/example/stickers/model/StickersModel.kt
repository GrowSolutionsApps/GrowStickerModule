package com.example.stickers.model

import com.google.gson.annotations.SerializedName

data class StickersModel(

	@field:SerializedName("next_page")
	val nextPage: Int,

	@field:SerializedName("sticker")
	val sticker: List<StickerItem>,

	@field:SerializedName("total_page")
	val totalPage: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class StickerItem(

	@field:SerializedName("thumb")
	val thumb: List<ThumbItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("isnew")
	val isnew: String,

	@field:SerializedName("isvip")
	val isvip: String
)

data class ThumbItem(

	@field:SerializedName("image")
	val image: String
)
