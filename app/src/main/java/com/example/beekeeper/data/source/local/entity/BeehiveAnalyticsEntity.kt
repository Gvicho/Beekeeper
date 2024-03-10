package com.example.beekeeper.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeehiveAnalyticsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "weightData") val weightData: String,
    @ColumnInfo(name = "temperatureData") val temperatureData: String,
)