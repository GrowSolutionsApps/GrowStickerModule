package com.example.stickers.network

import android.content.Context
import android.util.Log
import com.example.stickers.utils.isOnline
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallApiDownloadData {

    interface CallBack {

        fun onLoaded(response: Response<ResponseBody?>?)
        fun onServerError()
        fun onNetworkError()
    }

    companion object {

        fun downloadFileWithDynamicUrlSync(context: Context?, url: String?, callBack: CallBack) {
            val api: RetroService = RetroService.getInstance()
            val call: Call<ResponseBody> = api.downloadFileWithDynamicUrlSync(url)
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    Log.w("msg", "onDownloadComplete")
                    callBack.onLoaded(response)
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    if (context != null) {
                        if (context.isOnline()) {
                            callBack.onServerError()
                        } else {
                            callBack.onNetworkError()
                        }
                    }
                }
            })
        }
    }
}