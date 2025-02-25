package com.sevban.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sevban.designsystem.R

val SwitzerFontFamily = FontFamily(
    Font(R.font.switzer_regular),
    Font(R.font.switzer_medium, FontWeight.Medium),
    Font(R.font.switzer_semibold, FontWeight.SemiBold),
    Font(R.font.switzer_bold, FontWeight.Bold),
    Font(R.font.switzer_light, FontWeight.Light),
    Font(R.font.switzer_extralight, FontWeight.ExtraLight),
    Font(R.font.switzer_italic, FontWeight.Normal),
    Font(R.font.switzer_mediumitalic, FontWeight.Medium),
    Font(R.font.switzer_semibolditalic, FontWeight.SemiBold),
    Font(R.font.switzer_bolditalic, FontWeight.Bold),
    Font(R.font.switzer_lightitalic, FontWeight.Light),
    Font(R.font.switzer_extralightitalic, FontWeight.ExtraLight),
    Font(R.font.switzer_black, FontWeight.Black),
    Font(R.font.switzer_blackitalic, FontWeight.Black),
    Font(R.font.switzer_thin, FontWeight.Thin),
    Font(R.font.switzer_thinitalic, FontWeight.Thin),
    Font(R.font.switzer_extrabold, FontWeight.ExtraBold),
    Font(R.font.switzer_extrabolditalic, FontWeight.ExtraBold),
)

val SwitzerTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SwitzerFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)
