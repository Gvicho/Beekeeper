package com.example.beekeeper.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beekeeper.databinding.PagerItemFarmImageBinding

class ImagesPager2Adapter(private val imageUrls: List<String>) : RecyclerView.Adapter<ImagesPager2Adapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: PagerItemFarmImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(PagerItemFarmImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(imageUrls[position])
            .into(holder.imageView)
    }
}