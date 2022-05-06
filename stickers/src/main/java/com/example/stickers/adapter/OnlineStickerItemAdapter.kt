package com.example.stickers.adapter

import android.graphics.drawable.Animatable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stickers.R
import com.example.stickers.databinding.ItemStickerPreviewBinding
import com.example.stickers.model.ThumbItem
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder

class OnlineStickerItemAdapter(private val thumbItem: List<ThumbItem>) : RecyclerView.Adapter<OnlineStickerItemAdapter.ItemViewHolder>()  {

    var thumbData = mutableListOf<ThumbItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStickerPreviewBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        this.thumbData = thumbItem.toMutableList()

//        Log.w("msg","onBindViewHolder : " + thumbData.size)

        if (position < thumbItem.size){
            holder.binding.stickerItemPreview.setImageURI(thumbData[position].image)

//            Log.w("msg", "sticker thumb_preview  ---" + stickerItem.thumb)

            val imageUri = Uri.parse(thumbData[position].image)

//            Log.w("msg", "sticker thumb_preview imageUri  --- $imageUri")

            val imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri)
            val builder = Fresco.newDraweeControllerBuilder()
            builder.imageRequest = imageRequestBuilder.build()
            builder.autoPlayAnimations = true
            builder.controllerListener = object : BaseControllerListener<ImageInfo?>() {
                override fun onFinalImageSet(
                    id: String,
                    imageInfo: ImageInfo?,
                    animatable: Animatable?
                ) {
                }
            }
            holder.binding.stickerItemPreview.controller = builder.build()
            holder.binding.stickerItemPreview.hierarchy.setFailureImage(R.drawable.sticker_error)
            holder.binding.stickerItemPreview.hierarchy.setRetryImage(R.drawable.sticker_error)
            holder.binding.stickerItemPreview.hierarchy.setPlaceholderImage(R.drawable.sticker_error)

//            Log.w("msg","position : " + thumbItem)

        }
    }

    override fun getItemCount(): Int {
//        Log.w("msg","stickerItem.thumb.size "+thumbItem.size)
        return thumbItem.size
    }

    class ItemViewHolder(val binding: ItemStickerPreviewBinding) : RecyclerView.ViewHolder(binding.root)

}

