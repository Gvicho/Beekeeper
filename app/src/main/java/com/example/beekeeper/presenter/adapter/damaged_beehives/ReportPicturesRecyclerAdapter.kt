package com.example.beekeeper.presenter.adapter.damaged_beehives

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.DamagedPictureRecyclerItemBinding
import com.example.beekeeper.presenter.extension.loadImage


class ReportPicturesRecyclerAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ReportPicturesRecyclerAdapter.PicturesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesViewHolder {
        val binding = DamagedPictureRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PicturesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class PicturesViewHolder(private val binding: DamagedPictureRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            binding.ivCover.loadImage(imageUrl)
        }
    }
}