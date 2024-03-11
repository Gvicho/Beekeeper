package com.example.beekeeper.presenter.adapter.damaged_beehives

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.DamagePicturesRecyclerItemBinding
import com.example.beekeeper.databinding.ReportRecyclerItemBinding
import com.example.beekeeper.presenter.extension.loadImage

class DamagePicturesRecyclerAdapter :
    ListAdapter<Uri, DamagePicturesRecyclerAdapter.DamagePicturesViewHolder>(DamagePicturesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DamagePicturesViewHolder(
        DamagePicturesRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DamagePicturesViewHolder, position: Int) {
        holder.bind()
    }

    inner class DamagePicturesViewHolder(private val binding: DamagePicturesRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: Uri


        fun bind() {
            model = currentList[adapterPosition]
            binding.apply {
                ivDamage.loadImage(model.toString())
            }
            listeners()
        }

        private fun listeners() {
            binding.root.setOnClickListener {

            }
        }

    }

    class DamagePicturesDiffUtil : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }
}