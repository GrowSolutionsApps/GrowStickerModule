package com.example.stickers.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stickers.repository.StickerFullScreenRepository

class StickersFullScreenModelFactory(val context: Context, private val repository: StickerFullScreenRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StickerFullScreenViewModel::class.java)) {
            return StickerFullScreenViewModel(context,repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}