package com.example.beekeeper.presenter.adapter.saved_analytics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.ItemAnalyticsBinding
import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI

class SavedAnalyticsRecyclerAdapter (
    private val listener: ClickListener
) : ListAdapter<BeehiveAnalyticsUI, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BeehiveAnalyticsUI>() {
            override fun areItemsTheSame(oldItem: BeehiveAnalyticsUI, newItem: BeehiveAnalyticsUI): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BeehiveAnalyticsUI, newItem: BeehiveAnalyticsUI): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class AnalyticsViewHolder(private val binding: ItemAnalyticsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int) {
            val analytics = currentList[position]
            val id =analytics.id
            binding.apply {
                tvBeehiveId.text = id.toString()
            }
            bindItemsClickListener(id)
        }

        private fun bindItemsClickListener(id: Int){
            binding.root.setOnClickListener{
                listener.onClick(id)
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
    }
}