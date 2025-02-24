package com.sevban.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.home.components.forecastquadrant.GraphStyle
import com.sevban.home.components.forecastquadrant.LineChart

@Composable
fun LineChartContainer(
    yAxisData: List<Int>,
    xAxisData: List<String>,
    modifier: Modifier = Modifier
) {
    val bgColor = MaterialTheme.colorScheme.surfaceContainerLow
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = bgColor)
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(216.dp),
            graphStyle = GraphStyle(
                backgroundColor = bgColor,
                lineColor = MaterialTheme.colorScheme.primary,
                jointColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSurface,
            ),
            yAxisData = yAxisData,
            xAxisData = xAxisData
        )
    }
}