package com.example.beekeeper.presenter.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateValueFormatter(private val baseDate: Long) : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = baseDate
            add(Calendar.DAY_OF_YEAR, -30 + value.toInt())
        }
        return dateFormat.format(calendar.time)
    }
}