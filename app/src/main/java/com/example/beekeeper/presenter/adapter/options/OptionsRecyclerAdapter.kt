package com.example.beekeeper.presenter.adapter.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.OptionsRecyclerItemBinding
import com.example.beekeeper.databinding.ReportRecyclerItemBinding
import com.example.beekeeper.presenter.model.drawer_menu.Options

class OptionsRecyclerAdapter :
    ListAdapter<Options, OptionsRecyclerAdapter.OptionsViewHolder>(OptionsDiffUtil()) {

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
        private lateinit var model: Options


        fun bind() {
            model = currentList[adapterPosition]

            binding.apply {

                when(model){
                    Options.DARK_MODE -> tvTitle.text = "Dark mode"
                    Options.CHANGE_PASSWORD -> tvTitle.text = "Change password"
                    Options.LOG_OUT -> tvTitle.text = "Log out"
                    Options.LANGUAGE -> tvTitle.text = "Language"
                }
            }
            listeners()
        }

        private fun listeners() {
            binding.root.setOnClickListener {

            }
        }

    }

    class OptionsDiffUtil : DiffUtil.ItemCallback<Options>() {
        override fun areItemsTheSame(oldItem: Options, newItem: Options): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Options, newItem: Options): Boolean {
            return oldItem == newItem
        }
    }
}