package com.example.stickers.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.stickers.databinding.ItemStickerDownloadedBinding
import com.example.stickers.listener.DownloadedStickerClickListener
import com.example.stickers.listener.ItemTouchHelperAdapter
import com.example.stickers.model.DownloadedStickersDataModel
import com.example.stickers.model.StickersListDownload
import com.example.stickers.utils.StickerUtil
import com.example.stickers.utils.getSDCardStickerPath
import com.example.stickers.utils.setOnSingleClickListener
import java.util.*

class DownloadedStickersAdapter(
    private val mContext: Context,
    private val stickersDataModel: ArrayList<DownloadedStickersDataModel>,
    private val clickListener: DownloadedStickerClickListener
)  : RecyclerView.Adapter<DownloadedStickersAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    private var arrayListNew: ArrayList<StickersListDownload>? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStickerDownloadedBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.w("msg", "stickersDataModel : " + stickersDataModel[position]._packName)
        with(holder.binding) {
            rvPreviewSticker.itemAnimator = DefaultItemAnimator()
            textStickerName.text = stickersDataModel[position]._packName
        }
        val arrayList: ArrayList<StickersListDownload> = ArrayList<StickersListDownload>()
        arrayListNew = ArrayList()


        try {
            for (i in 0..4) {
                Log.w("msg", "sticker  arrayList == " + stickersDataModel[position]._count[i].absolutePath)
                arrayList.add(StickersListDownload(
                        stickersDataModel[position]._count[i].absolutePath,
                        stickersDataModel[position]._count[i].name
                    )
                )
            }
            for (i in 0 until stickersDataModel[position]._count.size) {
                Log.w("msg", "sticker  arrayList == " + stickersDataModel[position]._count[i].absolutePath)
                arrayListNew?.add(
                    StickersListDownload(
                        stickersDataModel[position]._count[i].absolutePath,
                        stickersDataModel[position]._count[i].name
                    )
                )
            }
            val downloadedStickerItemAdapter = DownloadedStickerItemAdapter(arrayList)
            holder.binding.rvPreviewSticker.adapter = downloadedStickerItemAdapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.w("msg", "sticker arrayList size == " + arrayList.size)

        setAllClick(holder,position,arrayListNew)
    }

    private fun setAllClick(
        holder: ViewHolder,
        position: Int,
        arrayListNew: ArrayList<StickersListDownload>?
    ) {
        with(holder.binding) {
            if (arrayListNew != null) {
                clItemClick.setOnSingleClickListener {
                    clickListener.click(
                        position,
                        stickersDataModel[position].packName,
                        arrayListNew,
                        getSDCardStickerPath(mContext) + stickersDataModel[position]._packName
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = stickersDataModel.size


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(this.stickersDataModel, fromPosition, toPosition)
        notifyItemChanged(fromPosition, toPosition)
        StickerUtil.saveStickerSort(mContext, this.stickersDataModel)
    }

    override fun onItemDismiss(position: Int) {
//        TODO("Not yet implemented")
    }

    class ViewHolder(val binding: ItemStickerDownloadedBinding) : RecyclerView.ViewHolder(binding.root)


}