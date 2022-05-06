package com.example.stickers.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stickers.repository.StickersRepository

class StickersModelFactory(val context: Context,private val repository: StickersRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnlineStickersViewModel::class.java)) {
            return OnlineStickersViewModel(context,repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}