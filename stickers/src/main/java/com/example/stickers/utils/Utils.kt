package com.example.stickers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.view.View

fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}

/**
 * A single click listener that only invokes [block] at most once per every [wait] milliseconds.
 */
fun View.setOnSingleClickListener(wait: Long = 1000L, block: () -> Unit) {
    setOnClickListener(OnSingleClickListener(block, wait))
}

class OnSingleClickListener(private val block: () -> Unit, private val wait: Long) : View.OnClickListener {

    private var lastClickTime = 0L

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < wait) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()

        block()
    }
}

fun Context.isOnline(): Boolean {
    val connectivityManager = this.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.activeNetworkInfo?.run {
            return when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> false
            }
        }
    }
    return false
}

fun getStickerDownloadPath(context: Context) : String{
    return context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath + "/Emoji Key Keyboard StaticData/sticker_pack/"
}
fun getSDCardStickerPath(context: Context): String {
    return context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath + "/Emoji Key Keyboard StaticData/sticker_pack/"
}
fun getFilePath(context: Context): String {
    return context.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath + "/Emoji Key Keyboard StaticData/"
}

