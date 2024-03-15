package com.example.beekeeper.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.beekeeper.databinding.ItemFarmBinding
import com.example.beekeeper.presenter.model.home.FarmUi
import com.example.beekeeper.presenter.model.home.LocationUi
import me.relex.circleindicator.CircleIndicator3


class FarmsRecyclerAdapter (
    private val listener: ItemClickCallBack
) : ListAdapter<FarmUi, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FarmUi>() {
            override fun areItemsTheSame(oldItem: FarmUi, newItem: FarmUi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FarmUi, newItem: FarmUi): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class FarmViewHolder(private val binding: ItemFarmBinding):RecyclerView.ViewHolder(binding.root){

        private lateinit var imagesPager2Adapter: ImagesPager2Adapter
        private lateinit var viewPager2: ViewPager2
        private lateinit var indicator: CircleIndicator3

        fun bind(position: Int){



            val farm = currentList[position]
            val owner = farm.owner
            val location = farm.location

            imagesPager2Adapter = ImagesPager2Adapter(farm.images)

            viewPager2 = binding.viewPager2
            indicator = binding.indicator

            viewPager2.adapter = imagesPager2Adapter
            indicator.setViewPager(viewPager2)
            imagesPager2Adapter.registerAdapterDataObserver(indicator.adapterDataObserver)

            binding.apply {
                tvName.text = farm.farmName
                tvLocationLabel.text = location.locationName
            }

            setClickListener(farm.id)
            setFarmLocationBtnClickListener(location)
        }

        private fun setClickListener(id:Int){
            binding.root.setOnClickListener{
                listener.onItemClick(id)
            }
        }

        private fun setFarmLocationBtnClickListener(location: LocationUi){
            binding.btnLocation.setOnClickListener{
                listener.onLocationButtonClick(location)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FarmViewHolder(ItemFarmBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is FarmViewHolder) holder.bind(position)
    }
}

interface ItemClickCallBack{
    fun onItemClick(id:Int)
    fun onLocationButtonClick(location: LocationUi)
}