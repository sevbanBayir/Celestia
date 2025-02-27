package com.sevban.home.components.forecastquadrant

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class GraphStyle(
    val lineColor: Color = Color.Gray,
    val lineStroke: Float = 6f,
    val backgroundColor: Color = Color.Transparent,
    val jointColor: Color = Color.Black,
    val jointRadius: Float = 10f,
    val jointStroke: Float = 2f,
    val textColor: Color = Color.Black,
    val gridLineColor: Color = Color.Transparent,
    val textStyle: TextStyle = TextStyle.Default
)
