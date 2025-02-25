package com.sevban.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sevban.designsystem.R

val RemFontFamily = FontFamily(
    Font(R.font.rem_regular),
    Font(R.font.rem_medium, FontWeight.Medium),
    Font(R.font.rem_semibold, FontWeight.SemiBold),
    Font(R.font.rem_bold, FontWeight.Bold),
    Font(R.font.rem_black, FontWeight.Black),
    Font(R.font.rem_light, FontWeight.Light),
    Font(R.font.rem_thin, FontWeight.Thin),
    Font(R.font.rem_extrabold, FontWeight.ExtraBold),
    Font(R.font.rem_extralight, FontWeight.ExtraLight),
    Font(R.font.rem_italic, FontWeight.Normal),
    Font(R.font.rem_mediumitalic, FontWeight.Medium),
    Font(R.font.rem_semibolditalic, FontWeight.SemiBold),
    Font(R.font.rem_bolditalic, FontWeight.Bold),
    Font(R.font.rem_blackitalic, FontWeight.Black),
    Font(R.font.rem_lightitalic, FontWeight.Light),
    Font(R.font.rem_thinitalic, FontWeight.Thin),
    Font(R.font.rem_extralightitalic, FontWeight.ExtraLight),
    Font(R.font.rem_extrabolditalic, FontWeight.ExtraBold),
)

val RemTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RemFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)