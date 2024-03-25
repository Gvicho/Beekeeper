package com.example.beekeeper.presenter.adapter.damaged_beehives

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.beekeeper.R
import com.example.beekeeper.databinding.ReportRecyclerItemBinding
import com.example.beekeeper.presenter.adapter.images_pager.ImagesPager2Adapter
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI
import me.relex.circleindicator.CircleIndicator3

class ReportsRecyclerAdapter(private val onItemClick: (DamageReportUI) -> Unit) :
    ListAdapter<DamageReportUI, ReportsRecyclerAdapter.ReportsViewHolder>(ReportsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReportsViewHolder(
        ReportRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ReportsViewHolder(private val binding: ReportRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var imagesPager2Adapter: ImagesPager2Adapter
        private lateinit var viewPager2: ViewPager2
        private lateinit var indicator: CircleIndicator3
        private lateinit var report: DamageReportUI

        fun bind(pos: Int) {
            report = currentList[pos]
            val picturesList = report.imageUris.map { it.toString() }
            val damage = report.damageLevelIndicator

            imagesPager2Adapter = ImagesPager2Adapter(picturesList)

            viewPager2 = binding.viewPager2
            indicator = binding.indicator

            viewPager2.adapter = imagesPager2Adapter
            indicator.setViewPager(viewPager2)
            imagesPager2Adapter.registerAdapterDataObserver(indicator.adapterDataObserver)


            binding.apply {
                tvDate.text = report.dateUploaded
                tvDesc.text = report.damageDescription
                tvId.text = report.id.toString()
                if (damage > 2) {
                    indicatorLogo.setImageResource(R.drawable.circle_red)
                } else if (damage == 2) {
                    indicatorLogo.setImageResource(R.drawable.circle_orange)
                } else {
                    indicatorLogo.setImageResource(R.drawable.circle_green)
                }
            }

            listeners()
        }

        private fun listeners() {
            binding.root.setOnClickListener {
                onItemClick.invoke(report)
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