package com.example.stickers.model

import com.google.gson.annotations.SerializedName

data class StickerFullScreenModel(

	@field:SerializedName("preview")
	val preview: List<PreviewItem>,

	@field:SerializedName("zip")
	val zip: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("isnew")
	val isnew: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("isvip")
	val isvip: String,

	@field:SerializedName("status")
	val status: String
)

data class PreviewItem(

	@field:SerializedName("image")
	val image: String
)
