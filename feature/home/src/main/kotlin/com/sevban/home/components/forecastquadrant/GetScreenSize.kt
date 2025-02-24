package com.sevban.home.components.forecastquadrant

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun getScreenSizeDp(): ScreenSizeDp {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    return ScreenSizeDp(screenWidth = screenWidth, screenHeight = screenHeight)
}

@Composable
fun getScreenSizePx(): ScreenSizePx {
    val context = LocalContext.current

    val displayMetrics = context.resources.displayMetrics

    //Width And Height Of Screen
    val screenSize = ScreenSizePx(displayMetrics.widthPixels, displayMetrics.heightPixels)

    //Device Density
    val density = displayMetrics.density

    return screenSize
}

data class ScreenSizePx(
    val screenWidth: Int = 0,
    val screenHeight: Int = 0
)

data class ScreenSizeDp(
    val screenWidth: Dp = 0.dp,
    val screenHeight: Dp = 0.dp
)