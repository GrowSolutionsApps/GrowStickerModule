package com.example.stickers.network

import com.example.stickers.model.StickerFullScreenModel
import com.example.stickers.model.StickersModel
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetroService {

    @POST("stickerlist")
    @FormUrlEncoded
    fun getOnlineStickers(
        @Field("apikey") apikey: String,
        @Field("password") password: String,
        @Field("languagecode") languagecode: String,
        @Field("page") page: String) : Observable<StickersModel>

    @POST("stickerdetail")
    @FormUrlEncoded
    fun getAllItemSelectedStickers(
        @Field("apikey") apikey: String,
        @Field("password") password: String,
        @Field("stickerid") stickerid: String ): Observable<StickerFullScreenModel>

    @GET
    fun downloadFileWithDynamicUrlSync(@Url fileUrl: String?): Call<ResponseBody>

    companion object {

        var retrofitService: RetroService? = null

        //Create the RetrofitService instance using the retrofit.
        fun getInstance(): RetroService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://ledkeyboard.growsolutions.in/api/Sticker/")
                    .addConverterFactory(GsonConverterFactory.create())
                    //You need to tell Retrofit that you want to use RxJava 3
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetroService::class.java)
            }
            return retrofitService!!
        }
    }
}