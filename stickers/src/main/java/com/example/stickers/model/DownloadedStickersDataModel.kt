package com.example.stickers.model

import java.io.File

data class DownloadedStickersDataModel(
    val id: Int,
    val packName: String,
    val count: Array<File>,
    val filePath: String) {

    companion object{
        const val DEFAULT_INDEX: Int = 1000000
    }
    private val index = DEFAULT_INDEX

    var _index: Int = index
        // Custom Getter
        get() = field
        set(value){
            field = value
        }

    var _filePath: String = filePath
        // Custom Getter
        get() = field
        set(value){
            field = value
        }

    var _packName: String = packName
        // Custom Getter
        get() = field
        set(value){
            field = value
        }

    var _count: Array<File> = count
        // Custom Getter
        get() = field
        set(value){
            field = value
        }

}