package com.example.beekeeper.presenter.adapter.scan_list_recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.ItemBluetoothDeviceBinding
import com.example.beekeeper.presenter.model.bluetooth_device.BluetoothDeviceUIModel

class BluetoothDevicesRecyclerAdapter (
    private val listener: ClickListener
) : ListAdapter<BluetoothDeviceUIModel, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BluetoothDeviceUIModel>() {
            override fun areItemsTheSame(oldItem: BluetoothDeviceUIModel, newItem: BluetoothDeviceUIModel): Boolean {
                return oldItem.address == newItem.address
            }

            override fun areContentsTheSame(oldItem: BluetoothDeviceUIModel, newItem: BluetoothDeviceUIModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class BluetoothDevicesViewHolder(private val binding: ItemBluetoothDeviceBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int) {
            val device = currentList[position]
            binding.apply {
                tvName.text = device.name?:"Null"
                tvAddress.text = device.address
            }
            bindItemsClickListener(device)
            bindIfBeehive(device.isBeehive)
        }

        private fun bindIfBeehive(isBeehive:Boolean){
            binding.iconBeehive.visibility = if(isBeehive) View.VISIBLE else View.GONE
        }

        private fun bindItemsClickListener(device:BluetoothDeviceUIModel){
            binding.root.setOnClickListener{
                listener.onClick(device)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDevicesViewHolder {
        return BluetoothDevicesViewHolder(ItemBluetoothDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is BluetoothDevicesViewHolder)holder.bind(position)
    }

    interface ClickListener{
        fun onClick(device: BluetoothDeviceUIModel)
    }
}

