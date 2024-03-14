package com.example.beekeeper.presenter.adapter.charts_recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeper.databinding.ItemAnalyticsBarChartBinding
import com.example.beekeeper.databinding.ItemAnalyticsLineCahrtBinding
import com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper.AnalyticType
import com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper.AnalyticsWrapper
import com.example.beekeeper.presenter.utils.DateValueFormatter
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.ceil

class ChartsRecyclerAdapter(private val analyticsList: List<AnalyticsWrapper>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val LINEAR = 1
        const val BAR = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (analyticsList[position].analyticType) {
            AnalyticType.BAR_CHART -> BAR
            AnalyticType.LINE_CHART -> LINEAR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BAR -> BarChartViewHolder(ItemAnalyticsBarChartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            LINEAR -> LineChartViewHolder(ItemAnalyticsLineCahrtBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BarChartViewHolder -> holder.bind(position)
            is LineChartViewHolder -> holder.bind(position)
        }
    }

    override fun getItemCount(): Int = analyticsList.size

    inner class BarChartViewHolder(private val binding: ItemAnalyticsBarChartBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var barChart: HorizontalBarChart

        fun bind(pos: Int) {
            val honeyWeightAnalytics = analyticsList[pos].analyticList
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
                    valueFormatter = DateValueFormatter(analyticsList[pos].saveTime) // Set the custom ValueFormatter
                }
                axisLeft.apply {
                    axisMinimum = 0f // Set the minimum Y-axis value to 0
                    granularity = 1f // Show all Y-axis labels
                    setDrawGridLines(true) // Enable Y-axis grid lines
                }
                axisRight.isEnabled = false // Disable the right Y-axis
            }
            // Create a list of BarEntry objects from your sensor data
            val entries = List(30) { idx ->
                BarEntry((idx + 1).toFloat(), honeyWeightAnalytics[idx])
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

    inner class LineChartViewHolder(private val binding: ItemAnalyticsLineCahrtBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var lineChart: LineChart

        fun bind(pos: Int) {
            val temperatureAnalytics = analyticsList[pos].analyticList
            lineChart = binding.lineChart
            val minVal = temperatureAnalytics.min()-1
            val maxVal = temperatureAnalytics.max()
            val charGranularity = ceil((maxVal-minVal)/5)

            lineChart.apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    valueFormatter = DateValueFormatter(analyticsList[pos].saveTime)
                }

                axisLeft.apply {
                    axisMinimum = minVal
                    granularity = charGranularity
                    setDrawGridLines(true)
                }

                axisRight.isEnabled = false
            }

            val entries = List(30) { index ->
                Entry((index + 1).toFloat(), temperatureAnalytics[index])
            }

            val dataSet = LineDataSet(entries, "Beehive Temperature").apply {
                setDrawValues(true)
                setDrawCircles(false)
                setDrawFilled(true)
                //mode = LineDataSet.Mode.CUBIC_BEZIER // Make the line smooth
            }

            val data = LineData(dataSet)
            lineChart.data = data
            lineChart.invalidate()

        }

    }
}

