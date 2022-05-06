package com.example.stickers.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stickers.activity.StickerFullScreenActivity
import com.example.stickers.activity.StickerFullScreenActivity.Companion.STICKER_ID
import com.example.stickers.activity.StickerFullScreenActivity.Companion.STICKER_NAME
import com.example.stickers.adapter.OnlineStickerAdapter
import com.example.stickers.databinding.FragmentStickersOnlineBinding
import com.example.stickers.listener.StickerItemClickListener
import com.example.stickers.network.RetroService
import com.example.stickers.network.Status
import com.example.stickers.repository.StickersRepository
import com.example.stickers.utils.gone
import com.example.stickers.utils.isOnline
import com.example.stickers.utils.visible
import com.example.stickers.viewmodel.OnlineStickersViewModel
import com.example.stickers.viewmodel.StickersModelFactory
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.rvadapter.AdmobNativeAdAdapter

class OnlineStickersFragment : Fragment() , StickerItemClickListener {

    private lateinit var binding: FragmentStickersOnlineBinding
    private lateinit var stickersViewModel: OnlineStickersViewModel
    private val retrofitService = RetroService.getInstance()
    private val adapter = OnlineStickerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Fresco.initialize(requireActivity())
        binding = FragmentStickersOnlineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupData()
        setAdmobNativeAds()
        setAllClick()
    }

    private fun setAllClick() {
        binding.noNetwork.tvRefresh.setOnClickListener {
            if ((requireActivity().isOnline())) {
                setupViewModel()
                setupData()
                setAdmobNativeAds()
            }
        }
    }

    private fun setAdmobNativeAds() {
        val admobNativeAdAdapter: AdmobNativeAdAdapter = AdmobNativeAdAdapter.Builder
            .with("ca-app-pub-3940256099942544/2247696110", adapter, "small")
            .adItemInterval(2)
            .forceReloadAdOnBind(true)
            .build()
        binding.rvOnlineStickers.hasFixedSize()
        binding.rvOnlineStickers.adapter = admobNativeAdAdapter
        binding.rvOnlineStickers.layoutManager = LinearLayoutManager(requireActivity())

    }

    private fun setupData() {

        //set adapter in recyclerview
        binding.rvOnlineStickers.hasFixedSize()
        binding.rvOnlineStickers.adapter = adapter

        //the observer will only receive events if the owner(activity) is in active state
        //invoked when onlineStickerList data changes
        stickersViewModel.onlineStickerList.observe(requireActivity(), Observer {
            if (it != null) {
                when(it.status){
                    Status.LOADING -> {
                        Log.w("TAG", "setupData: LOADING")
                        binding.noNetwork.clNoNetwork.gone()
                        binding.progressBar.visible()
                        binding.rvOnlineStickers.gone()

                    }
                    Status.NO_INTERNET -> {
                        binding.noNetwork.clNoNetwork.visible()
                        binding.progressBar.gone()
                        binding.rvOnlineStickers.gone()
                    }
                    Status.SUCCESS -> {
                        Log.w("TAG", "setupData: SUCCESS")
                        binding.rvOnlineStickers.visible()
                        binding.noNetwork.clNoNetwork.gone()
                        binding.progressBar.gone()

                        it.data?.let { it1 -> adapter.setOnlineStickerList(it1) }

                    }
                    Status.ERROR -> {
                        binding.progressBar.gone()
                        binding.rvOnlineStickers.gone()
                        binding.noNetwork.clNoNetwork.gone()

                    }

                }
                Log.d("TAG", "onlinestickersList: $it")

            } else {
                Toast.makeText(requireContext(), "Error in fetching data", Toast.LENGTH_SHORT).show()
            }
        })

        //invoked when a network exception occurred
        stickersViewModel.errorMessage.observe(requireActivity(), Observer {
            Log.d("TAG", "errorMessage: $it")
        })
    }


    private fun setupViewModel() {

        stickersViewModel = ViewModelProvider(
            this,
            StickersModelFactory(requireContext(),StickersRepository(retrofitService))
        )[OnlineStickersViewModel::class.java]

        stickersViewModel.getAllOnlineStickers()
    }

    override fun onDestroy() {
        //don't send events  once the activity is destroyed
        stickersViewModel.disposable.dispose()
        super.onDestroy()
    }

    override fun click(position: Int, stickerId: String, stickerName:String, isVip: String) {
        if (isVip == "0") {
            val intent = Intent(requireContext(), StickerFullScreenActivity::class.java)
            Log.w("msg", "stickerName : $stickerId")
            intent.putExtra(STICKER_ID, stickerId)
            intent.putExtra(STICKER_NAME, stickerName)
            startActivity(intent)
        }
    }

}
