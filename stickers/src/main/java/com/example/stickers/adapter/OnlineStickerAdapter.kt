package com.example.stickers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stickers.databinding.ItemStickerOnlineBinding
import com.example.stickers.listener.StickerItemClickListener
import com.example.stickers.model.StickerItem
import com.example.stickers.utils.gone
import com.example.stickers.utils.setOnSingleClickListener
import com.example.stickers.utils.visible

class OnlineStickerAdapter(private val clickListener: StickerItemClickListener
) : RecyclerView.Adapter<MainViewHolder>() {

    var stickers = mutableListOf<StickerItem>()

    fun setOnlineStickerList(stickerData: List<StickerItem>) {
        this.stickers = stickerData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemStickerOnlineBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val stickerItem = stickers[position]
        holder.binding.textStickerName.text = stickerItem.name
        with(holder.binding) {

            setupItemAdapter(holder,stickerItem)
            setupUI(holder,stickerItem)
            setAllOnClick(holder,position,stickerItem)

        }

    }

    private fun setupUI(holder: MainViewHolder, stickerItem: StickerItem) {
        with(holder.binding) {
            if (stickerItem.isvip == "1") {
                imgPremiumIcon.visible()
            } else {
                imgPremiumIcon.gone()
            }
        }
    }

    private fun setupItemAdapter(holder: MainViewHolder,stickerItem: StickerItem) {
        holder.binding.rvPreviewSticker.adapter = OnlineStickerItemAdapter(stickerItem.thumb)

    }

    private fun setAllOnClick(holder: MainViewHolder, position: Int, stickerItem: StickerItem) {
        with(holder.binding) {
            clItemClick.setOnSingleClickListener {
                clickListener.click(position, stickerItem.id, stickerItem.name, stickerItem.isvip)
            }
        }
    }

    override fun getItemCount(): Int {
        return stickers.size
    }
}

class MainViewHolder(val binding: ItemStickerOnlineBinding) : RecyclerView.ViewHolder(binding.root)