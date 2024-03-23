package com.example.beekeeper.presenter.adapter.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.OptionsRecyclerItemBinding
import com.example.beekeeper.presenter.model.drawer_menu.Option
import com.example.beekeeper.presenter.model.drawer_menu.Options

class OptionsRecyclerAdapter(private val onItemClick: (Option) -> Unit) :
    ListAdapter<Option, OptionsRecyclerAdapter.OptionsViewHolder>(OptionsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OptionsViewHolder(
        OptionsRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )


    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bind()
    }


    inner class OptionsViewHolder(private val binding: OptionsRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: Option


        fun bind() {
            model = currentList[adapterPosition]

            binding.apply {

                ivIcon.setImageResource(model.icon)
                tvTitle.text = model.name


            }
            listeners()
        }

        private fun listeners() {
            binding.apply {
                binding.root.setOnClickListener { onItemClick.invoke(model) }
            }

        }

    }

    class OptionsDiffUtil : DiffUtil.ItemCallback<Option>() {
        override fun areItemsTheSame(oldItem: Option, newItem: Option): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Option, newItem: Option): Boolean {
            return oldItem == newItem
        }
    }


}