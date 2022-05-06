package com.example.stickers.adapter

import android.graphics.drawable.Animatable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stickers.databinding.ItemStickerPreviewBinding
import com.example.stickers.model.StickersListDownload
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import java.io.File

class DownloadedStickerItemAdapter(private val stickersDataModelList: ArrayList<StickersListDownload>)
    : RecyclerView.Adapter<DownloadedStickerItemAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStickerPreviewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.w("msg","onBindViewHolder : "+stickersDataModelList[position].absolutePath)
        val imageUri = Uri.fromFile(File(stickersDataModelList[position].absolutePath))
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
    }

    override fun getItemCount(): Int  {
        Log.w("msg","getItemCount stickersDataModelList "+stickersDataModelList.size)
       return stickersDataModelList.size
    }

    class ViewHolder(val binding: ItemStickerPreviewBinding) : RecyclerView.ViewHolder(binding.root)

}