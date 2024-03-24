package com.example.beekeeper.presenter.adapter.saved_analytics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.R
import com.example.beekeeper.databinding.ItemAnalyticsBinding
import com.example.beekeeper.presenter.model.beehive_analytics.saved_analytics.SavedAnalyticsPartialUI

class SavedAnalyticsRecyclerAdapter (
    private val listener: ClickListener
) : ListAdapter<SavedAnalyticsPartialUI, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SavedAnalyticsPartialUI>() {
            override fun areItemsTheSame(oldItem: SavedAnalyticsPartialUI, newItem: SavedAnalyticsPartialUI): Boolean {
                return oldItem.beehiveId == newItem.beehiveId
            }

            override fun areContentsTheSame(oldItem: SavedAnalyticsPartialUI, newItem: SavedAnalyticsPartialUI): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class AnalyticsViewHolder(private val binding: ItemAnalyticsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int) {
            val analytics = currentList[position]
            val id =analytics.beehiveId
            binding.apply {
                tvBeehiveId.text = id.toString()
                tvSaveTime.text = analytics.saveTime
            }
            bindUserBackgroundColor(analytics.isSelected)
            bindItemsClickListener(id)
            bindItemsLongClickListener(id)
        }

        private fun bindUserBackgroundColor(isSelected:Boolean){
            if(isSelected) binding.root.setBackgroundResource(R.drawable.bg_button)
            else binding.root.setBackgroundColor(itemView.context.getColor(R.color.transparent))
        }

        private fun bindItemsClickListener(id:Int){
            binding.root.setOnClickListener{
                listener.onClick(id)
            }
        }

        private fun bindItemsLongClickListener(id:Int){
            binding.root.setOnLongClickListener{
                listener.onLongClick(id)
                true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyticsViewHolder {
        return AnalyticsViewHolder(ItemAnalyticsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is AnalyticsViewHolder)holder.bind(position)

    }

    interface ClickListener{
        fun onClick(id:Int)
        fun onLongClick(id:Int)
    }
}