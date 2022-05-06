package com.example.stickers.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stickers.R
import com.example.stickers.model.StickerItem
import com.example.stickers.repository.StickersRepository
import com.example.stickers.model.StickersModel
import com.example.stickers.network.Resource
import com.example.stickers.utils.isOnline
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class OnlineStickersViewModel(private val context : Context, private val repository: StickersRepository
) : ViewModel() {

    val onlineStickerList = MutableLiveData<Resource<List<StickerItem>>>()
    val errorMessage = MutableLiveData<String>()
    lateinit var disposable: Disposable

    fun getAllOnlineStickers() {
        if (context.isOnline()) {
            onlineStickerList.postValue(Resource.loading(null))
            //observer subscribing to observable
            val response = repository.getAllOnlineStickers()
            response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getStickerListObserver())
        } else {
            onlineStickerList.postValue(Resource.noInternet(context.getString(R.string.no_internet_try_again)))
        }
    }

    private fun getStickerListObserver(): Observer<StickersModel> {
        return object : Observer<StickersModel> {
            override fun onComplete() {
                //hide progress indicator .
            }

            override fun onError(e: Throwable) {

                onlineStickerList.postValue(Resource.error(e.message!!,null))
            }

            override fun onNext(response: StickersModel) {
                onlineStickerList.postValue(Resource.success(response.sticker))
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
                //start showing progress indicator.
            }
        }
    }
}