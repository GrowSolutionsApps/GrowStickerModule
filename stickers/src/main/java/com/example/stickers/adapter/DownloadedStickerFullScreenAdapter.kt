package com.example.stickers.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stickers.R
import com.example.stickers.databinding.ItemStickerFullscreenPreviewBinding
import com.example.stickers.listener.StickerExpandedClickListener
import com.example.stickers.model.PreviewItem
import com.example.stickers.model.StickersListDownload
import com.example.stickers.utils.setOnSingleClickListener

class DownloadedStickerFullScreenAdapter(private var context: Context, private val clickListener: StickerExpandedClickListener):
    RecyclerView.Adapter<DownloadedStickerFullScreenAdapter.AllStickerViewHolder>() {


    var allStickers = mutableListOf<StickersListDownload>()

    fun setAllStickerList(stickerData: List<StickersListDownload>) {
        this.allStickers = stickerData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllStickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val binding = ItemStickerFullscreenPreviewBinding.inflate(inflater, parent, false)
        return AllStickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllStickerViewHolder, position: Int) {

        with(holder.binding) {

            Glide.with(context)
                .load(allStickers[position].absolutePath)
                .placeholder(R.drawable.sticker_error)
                .into(stickerItemPreview)

            stickerItemPreview.setOnSingleClickListener {
                clickListener.onDownloadedClickExpand(position, stickerItemPreview, allStickers)
            }
        }
    }

    override fun getItemCount(): Int {
//        Log.d("TAG", "getItemCount : " + allStickers.size)

        return allStickers.size
    }

    class AllStickerViewHolder(val binding: ItemStickerFullscreenPreviewBinding) : RecyclerView.ViewHolder(binding.root)
}
