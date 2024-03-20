package com.example.beekeeper.presenter.adapter.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.OptionsDarkModeRecyclerItemBinding
import com.example.beekeeper.databinding.OptionsRecyclerItemBinding
import com.example.beekeeper.presenter.model.Option
import com.example.beekeeper.presenter.model.drawer_menu.Options

class OptionsRecyclerAdapter(private val onItemClick: (Option) -> Unit) :
    ListAdapter<Option, RecyclerView.ViewHolder>(OptionsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == OTHER_TYPE) {
            return OptionsViewHolder(
                OptionsRecyclerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return DarkModeViewHolder(
                OptionsDarkModeRecyclerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OptionsViewHolder) {
            holder.bind()
        } else if (holder is DarkModeViewHolder) {
            holder.bind()

        }
    }

    inner class DarkModeViewHolder(private val binding: OptionsDarkModeRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: Option
        fun bind() {
            model = currentList[adapterPosition]
           listeners()

        }

        private fun listeners() {
            binding.apply {
                binding.root.setOnClickListener {  onItemClick.invoke(model)}
            }
        }
    }

    inner class OptionsViewHolder(private val binding: OptionsRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: Option


        fun bind() {
            model = currentList[adapterPosition]

            binding.apply {

                when (model.type) {
                    Options.DARK_MODE -> tvTitle.text = "Dark mode"
                    Options.CHANGE_PASSWORD -> tvTitle.text = "Change password"
                    Options.LOG_OUT -> tvTitle.text = "Log out"
                    Options.LANGUAGE -> tvTitle.text = "Language"
                    Options.PROFILE -> tvTitle.text = "Profile"
                }
            }
            listeners()
        }

        private fun listeners() {
            binding.root.setOnClickListener {
                binding.apply {
                    binding.root.setOnClickListener {  onItemClick.invoke(model)}
                }
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

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].type == Options.DARK_MODE) {
            DARK_MODE_TYPE
        } else {
            OTHER_TYPE
        }
    }

    companion object {
        const val DARK_MODE_TYPE = 1
        const val OTHER_TYPE = 2

    }
}