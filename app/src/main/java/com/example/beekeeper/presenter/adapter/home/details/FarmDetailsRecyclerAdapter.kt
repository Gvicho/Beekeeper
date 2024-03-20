package com.example.beekeeper.presenter.adapter.home.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.beekeeper.R
import com.example.beekeeper.databinding.ItemAnalyticsBarChartBinding
import com.example.beekeeper.databinding.ItemFarmDetailsHeaderBinding
import com.example.beekeeper.databinding.ItemFarmDetailsImagesBinding
import com.example.beekeeper.databinding.ItemFarmDetailsOwnerDetailsBinding
import com.example.beekeeper.databinding.ItemFarmDetailsWeatherBinding
import com.example.beekeeper.presenter.adapter.ImagesPager2Adapter
import com.example.beekeeper.presenter.extension.loadImage
import com.example.beekeeper.presenter.model.home.LocationUi
import com.example.beekeeper.presenter.model.home.details.FarmDetailsItemWrapper
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import me.relex.circleindicator.CircleIndicator3
import java.util.Calendar

class FarmDetailsRecyclerAdapter (
    private val listener:DetailsListener
) : ListAdapter<FarmDetailsItemWrapper, ViewHolder>(
    FarmDetailsDiffUtil()
) {

    companion object{
        const val HEADER = 1
        const val IMAGE_PAGER = 2
        const val WEATHER = 3
        const val OWNER_DETAILS = 4
        const val BEEHIVE_NUMBER_CHART = 5
    }

    class FarmDetailsDiffUtil : DiffUtil.ItemCallback<FarmDetailsItemWrapper>() {
        override fun areItemsTheSame(oldItem: FarmDetailsItemWrapper, newItem: FarmDetailsItemWrapper): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FarmDetailsItemWrapper, newItem: FarmDetailsItemWrapper): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            1 -> DetailsHeaderViewHolder(ItemFarmDetailsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> ImagesPagerViewHolder(ItemFarmDetailsImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            3-> DetailsWeatherViewHolder(ItemFarmDetailsWeatherBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            4 -> OwnerDetailsViewHolder(ItemFarmDetailsOwnerDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> ChartViewHolder(ItemAnalyticsBarChartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DetailsWeatherViewHolder -> holder.bind(position)
            is DetailsHeaderViewHolder -> holder.bind(position)
            is ImagesPagerViewHolder -> holder.bind(position)
            is OwnerDetailsViewHolder -> holder.bind(position)
            is ChartViewHolder -> holder.bind(position)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when(currentList[position].itemType){
            FarmDetailsItemWrapper.ItemType.HEADER -> HEADER
            FarmDetailsItemWrapper.ItemType.IMAGE_PAGER -> IMAGE_PAGER
            FarmDetailsItemWrapper.ItemType.OWNER_DETAILS -> OWNER_DETAILS
            FarmDetailsItemWrapper.ItemType.BEEHIVE_NUMBER_CHART -> BEEHIVE_NUMBER_CHART
            FarmDetailsItemWrapper.ItemType.WEATHER_INFO -> WEATHER
        }
    }

    inner class DetailsHeaderViewHolder(private val binding: ItemFarmDetailsHeaderBinding) :
        ViewHolder(binding.root) {

        fun bind(pos :Int) {
            val header = currentList[pos].header

            header?.let {details->
                binding.apply {
                    tvFarmId.text = details.id.toString()
                    tvName.text = details.name
                    tvFarmLocation.text = details.locationUi.locationName
                    bindLocationBtn(details.locationUi)
                }
            }
        }

        private fun bindLocationBtn(location:LocationUi){
            binding.locationIcon.setOnClickListener{
                listener.locationClicked(location)
            }
        }

    }

    inner class ImagesPagerViewHolder(private val binding: ItemFarmDetailsImagesBinding) : ViewHolder(binding.root) {

        private lateinit var imagesPager2Adapter: ImagesPager2Adapter
        private lateinit var viewPager2: ViewPager2
        private lateinit var indicator: CircleIndicator3

        fun bind(pos :Int) {
            val images = currentList[pos].imagesPager

            images?.let {
                imagesPager2Adapter = ImagesPager2Adapter(images)

                viewPager2 = binding.viewPager2
                indicator = binding.indicator

                viewPager2.adapter = imagesPager2Adapter
                indicator.setViewPager(viewPager2)
                imagesPager2Adapter.registerAdapterDataObserver(indicator.adapterDataObserver)

            }

        }

    }
    inner class DetailsWeatherViewHolder(private val binding: ItemFarmDetailsWeatherBinding) :
        ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(pos :Int) {

            val weatherIconAnim = binding.weatherIcon
            val rotateAnimation = AnimationUtils.loadAnimation(itemView.context, R.anim.rotation_360)
            val weatherInfo = currentList[pos].weatherInfoUI
            weatherInfo?.let { weather->
                binding.apply {
                    weatherIconAnim.startAnimation(rotateAnimation)
                    weatherIcon.loadImage(weather.weather[0].image)
                    tVTemp.text = "${weather.main.temp}C"
                    tVHumidity.text = "${weather.main.humidity}%"
                    tVFeelsTemp.text = "${weather.main.feelsLike}C"
                    tvWeather.text = weather.weather[0].main
                }
            }
        }

    }

    inner class OwnerDetailsViewHolder(private val binding: ItemFarmDetailsOwnerDetailsBinding) :
        ViewHolder(binding.root) {

        fun bind(pos :Int) {

            val ownerDetails = currentList[pos].ownerDetailsUi

            ownerDetails?.let {owner->

                val phone = owner.phone
                val mail = owner.email

                binding.apply {
                    tvName.text = owner.name
                    tvEmail.text = mail
                    tvPhone.text = phone
                    tvNumberOfFarms.text = owner.numberOfFarms.toString()
                    tvBeekeeperId.text = owner.id.toString()

                    profileImage.loadImage(owner.profile)
                    if (phone.isNotEmpty()){
                        phoneBtn.visibility = View.VISIBLE
                        bindPhoneBtnListener(phone)
                    } else{
                        phoneBtn.visibility = View.GONE
                    }
                    if (mail.isNotEmpty()){
                        mailBtn.visibility = View.VISIBLE
                        bindMailBtnListener(mail)
                    } else{
                        mailBtn.visibility = View.GONE
                    }
                }

            }

        }

        private fun bindPhoneBtnListener(phone: String){
            binding.phoneBtn.setOnClickListener{
                listener.phoneNumberClick(phone)
            }
        }

        private fun bindMailBtnListener(mail:String){
            binding.mailBtn.setOnClickListener{
                listener.mailAddressClicked(mail)
            }
        }

    }

    inner class ChartViewHolder(private val binding: ItemAnalyticsBarChartBinding) :
        ViewHolder(binding.root) {

        private lateinit var barChart: HorizontalBarChart

        fun bind(pos :Int) {

            val chartData = currentList[pos].beehiveNumberChartUI

            chartData?.let {

                barChart = binding.barChart
                barChart.apply {

                    description.isEnabled = false // Hide the description label
                    setFitBars(true) // Make the bars fit the available space
                    setDrawValueAboveBar(true) // Show the bar values on top of the bars
                    setMaxVisibleValueCount(30) // Show all 30 values
                    setPinchZoom(false) // Disable pinch-zoom
                    setDrawBarShadow(false) // Disable bar shadows
                    setDrawGridBackground(false) // Disable grid background

                    // Set the axis properties
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM // Set the X-axis position to bottom
                        granularity = 1f // Show all X-axis labels
                        setDrawGridLines(false) // Disable X-axis grid lines
                        valueFormatter = IndexAxisValueFormatter(getPastYears(chartData.lastYearsGrowth)) // Set the custom ValueFormatter
                    }
                    axisLeft.apply {
                        axisMinimum = 0f // Set the minimum Y-axis value to 0
                        granularity = 1f // Show all Y-axis labels
                        setDrawGridLines(true) // Enable Y-axis grid lines
                    }
                    axisRight.isEnabled = false // Disable the right Y-axis
                }
                // Create a list of BarEntry objects from your sensor data
                val entries = List(chartData.lastYearsGrowth.size) { idx ->
                    BarEntry((idx + 1).toFloat(), chartData.lastYearsGrowth[idx].toFloat())
                }
                // Create a BarDataSet from the entries
                val dataSet = BarDataSet(entries, "Beehive Weight").apply {
                    setDrawValues(true) // Show the values on top of the bars
                }
                // Create a BarData object and set it to the chart
                val data = BarData(dataSet)
                barChart.data = data
                // Refresh the chart
                barChart.invalidate()

            }

        }
        // Function to get the list of past years
        private fun getPastYears(lastYearsGrowth:List<Int>): List<String> {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            return (currentYear - lastYearsGrowth.size ..currentYear).map { it.toString() }
        }
    }

    interface DetailsListener{

        fun phoneNumberClick(phone:String)

        fun mailAddressClicked(mail:String)

        fun locationClicked(location: LocationUi)

    }
}