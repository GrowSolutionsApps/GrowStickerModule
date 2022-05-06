package com.example.stickers.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stickers.R
import com.example.stickers.model.PreviewItem
import com.example.stickers.model.StickerFullScreenModel
import com.example.stickers.network.Resource
import com.example.stickers.repository.StickerFullScreenRepository
import com.example.stickers.utils.isOnline
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class StickerFullScreenViewModel (private val context : Context, private val repository: StickerFullScreenRepository): ViewModel() {

    val allStickersList = MutableLiveData<Resource<StickerFullScreenModel>>()
    val errorMessage = MutableLiveData<String>()
    lateinit var disposable: Disposable

    fun getAllItemSelectedStickers(stickerId: String) {
        if (context.isOnline()) {
            allStickersList.postValue(Resource.loading(null))
            //observer subscribing to observable
            val response = repository.getAllStickers(stickerId)
            response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAllStickersObserver())
        } else {
            allStickersList.postValue(Resource.noInternet(context.getString(R.string.no_internet_try_again)))
        }
    }

    private fun getAllStickersObserver(): Observer<StickerFullScreenModel> {
        return object : Observer<StickerFullScreenModel> {
            override fun onComplete() {
                //hide progress indicator .
            }

            override fun onError(e: Throwable) {
                Log.w("msg","response.onError : "+e.message!!)
                allStickersList.postValue(Resource.error(e.message!!,null))
            }

            override fun onNext(response: StickerFullScreenModel) {
                Log.w("msg","response.preview "+response.preview)
                if (response.status == "success") {
                    allStickersList.postValue(Resource.success(response))
                }
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
                //start showing progress indicator.
            }
        }
    }
}