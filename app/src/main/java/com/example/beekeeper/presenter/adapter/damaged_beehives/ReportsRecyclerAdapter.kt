package com.example.beekeeper.presenter.adapter.damaged_beehives

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.ReportRecyclerItemBinding
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI

class ReportsRecyclerAdapter() :
    ListAdapter<DamageReportUI, ReportsRecyclerAdapter.ReportsViewHolder>(ReportsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReportsViewHolder(
        ReportRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        holder.bind()
    }

    inner class ReportsViewHolder(private val binding: ReportRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: DamageReportUI


        fun bind() {
            model = currentList[adapterPosition]
            val picturesList = model.imageUris
            picturesList.let {
                val picturesAdapter = PicturesRecyclerAdapter(it.map { uri-> uri.toString() })
                binding.picturesRecyclerView.adapter = picturesAdapter
                
            }
            binding.apply {

                tvDate.text = model.dateUploaded
                tvDesc.text = model.damageDescription
                tvId.text = model.id.toString()
                picturesRecyclerView.layoutManager = GridLayoutManager(root.context, 3)



            }
            listeners()
        }

        private fun listeners() {
            binding.root.setOnClickListener {

            }
        }

    }

    class ReportsDiffUtil : DiffUtil.ItemCallback<DamageReportUI>() {
        override fun areItemsTheSame(oldItem: DamageReportUI, newItem: DamageReportUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DamageReportUI, newItem: DamageReportUI): Boolean {
            return oldItem == newItem
        }
    }
}