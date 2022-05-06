package com.example.stickers.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.stickers.activity.DownloadedStickerFullScreenActivity
import com.example.stickers.adapter.DownloadedStickersAdapter
import com.example.stickers.databinding.FragmentStickersDownloadedBinding
import com.example.stickers.listener.DownloadedStickerClickListener
import com.example.stickers.listener.OnStartDragListener
import com.example.stickers.model.DownloadedStickersDataModel
import com.example.stickers.model.StickersListDownload
import com.example.stickers.utils.StickerUtil
import com.example.stickers.utils.getSDCardStickerPath
import com.example.stickers.utils.gone
import java.io.File
import java.util.*

class DownloadedStickersFragment : Fragment(), OnStartDragListener,DownloadedStickerClickListener {

    companion object {
        var downloadedStickersList = ArrayList<DownloadedStickersDataModel>()
    }
    private lateinit var binding: FragmentStickersDownloadedBinding
    private val mItemTouchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStickersDownloadedBinding.inflate(layoutInflater)
        Log.w("msg","onCreateView")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.w("msg","onViewCreated")
        initData()
        setupData()

    }

    private fun initData() {
        this.loadDownloadedStickersPack(getSDCardStickerPath(requireContext()))
    }

    private fun setupData() {
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvDownloadedStickers)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        this.mItemTouchHelper?.startDrag(viewHolder!!)
    }

    private var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val pos = viewHolder.oldPosition
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            Log.w("msg", "sticker1_fromPosition init pos $pos")
            Log.w("msg", "sticker1_fromPosition init $fromPosition")
            Log.w("msg", "sticker1_toPosition init $toPosition")
            Collections.swap(downloadedStickersList,
                fromPosition,
                toPosition
            )
            binding.rvDownloadedStickers.adapter?.notifyItemMoved(fromPosition, toPosition)
            StickerUtil.saveStickerSort(
                activity,
                downloadedStickersList
            )
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        @SuppressLint("NotifyDataSetChanged")
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            StickerUtil.saveStickerSort(
                activity,
                downloadedStickersList
            )
            binding.rvDownloadedStickers.adapter?.notifyDataSetChanged()
        }
    }

    private fun loadDownloadedStickersPack(Path: String) {
//        emojiDowonloadedFolder = ArrayList<WAEmojiDownloadedDataModel>()
        val directory = File(Path)
        Log.w("msg", "Sticker_path== $Path")
        val files = directory.listFiles()
        val directoryPath = File(Path)
        if (!directoryPath.exists()) {
            directoryPath.mkdirs()
        }
        try {
            downloadedStickersList.clear()

            Log.w("msg", "Sticker_path_files== " + files.size)
            if (files.isNotEmpty()) {
                try {
                    for (i in files.indices) {
                        val subDirectory = files[i].listFiles()
                        Log.w("msg", "Sticker_path_files_name== " + files[i].name)
                        if (!files[i].name.contains("zip")) {
                            downloadedStickersList.add(
                                DownloadedStickersDataModel(
                                    i,
                                    files[i].name,
                                    subDirectory,
                                    files[i].path
                                )
                            )
                        }
                    }
                    StickerUtil.sortStickers(
                        activity,
                        downloadedStickersList
                    )
                } catch (e: Exception) {
                    Log.w("msg", "catch == " +e.message)
                }
                Log.w(
                    "msg",
                    "downloadedStickersList size == " + downloadedStickersList.size
                )
                var adapter = DownloadedStickersAdapter(
                    requireActivity(),
                    downloadedStickersList,
                    this,
                )
                binding.rvDownloadedStickers.adapter = adapter

            } else {
                Log.w("msg", "Else123456")
//                if (layout_warning.getVisibility() == View.GONE) {
//                    layout_warning.setVisibility(View.VISIBLE)
//                    recycle_emojilist.setVisibility(View.GONE)
//                    downloadedStickersList.clear()
//                    binding.rvDownloadedStickers.adapter = null
//                }
                downloadedStickersList.clear()
                binding.rvDownloadedStickers.gone()
                binding.rvDownloadedStickers.adapter = null
            }
        } catch (e: Exception) {
            Log.w("msg", "catch Exception == " +e.message)

        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.w("msg","onViewStateRestored")
        initData()
    }

    override fun onResume() {
        super.onResume()
        Log.w("msg","onResume")

        StickerUtil.saveStickerSort(
            activity,
            downloadedStickersList
        )

        initData()
    }

    override fun click(
        position: Int,
        name: String,
        arrayList: ArrayList<StickersListDownload>,
        deletePath: String
    ) {
        val intent = Intent(requireContext(), DownloadedStickerFullScreenActivity::class.java)
        intent.putExtra(DownloadedStickerFullScreenActivity.STICKER_NAME, name)
        intent.putExtra(DownloadedStickerFullScreenActivity.STICKER_ARRAYLIST, arrayList)
        intent.putExtra(DownloadedStickerFullScreenActivity.STICKER_DELETE_PATH, deletePath)
        intent.putExtra(DownloadedStickerFullScreenActivity.STICKER_POSITION, position)
        startActivity(intent)
    }
}