package com.sevban.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ForecastWithWeatherItems(
    @Embedded val forecast: ForecastEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "forecastId"
    )
    val weatherItems: List<ForecastWeatherEntity>
) 