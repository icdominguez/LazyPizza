package com.seno.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.seno.core.presentation.R

// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = FontFamily(Font(R.font.instrument_sans_medium)),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
    )
val title_1_semiBold =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_semibold)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    )

val title_1_medium =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    )

val title_2 =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_semibold)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    )
val title_3 =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_semibold)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    )
val title_4 =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
val label_2_semiBold =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_semibold)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
val label_3_medium =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_bold)),
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
    )
val body_1_regular =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    )
val body_1_medium =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    )
val body_2_regular =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    )
val body_3_regular =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    )
val body_3_medium =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    )
val body_3_body =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    )
val body_4_regular =
    TextStyle(
        fontFamily = FontFamily(Font(R.font.instrument_sans_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
