package com.msgkatz.ratesapp.core.uikitkmp.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import chartsnrates.androidcmpapp.core.uikitkmp.generated.resources.Res
import chartsnrates.androidcmpapp.core.uikitkmp.generated.resources.normativepro_bold
import chartsnrates.androidcmpapp.core.uikitkmp.generated.resources.normativepro_italic
import chartsnrates.androidcmpapp.core.uikitkmp.generated.resources.normativepro_light
import chartsnrates.androidcmpapp.core.uikitkmp.generated.resources.normativepro_medium
import chartsnrates.androidcmpapp.core.uikitkmp.generated.resources.normativepro_regular
//import org.jetbrains.compose.resources.FontResource
//import org.jetbrains.compose.resources.Font


val normativeProFontFamily = FontFamily.Default


//val normativeProFontFamily = FontFamily(
//    Res.font.normativepro_light)
//    Font(Res.font.normativepro_light, FontWeight.Light),
//    Font(Res.font.normativepro_regular, FontWeight.Normal),
//    Font(Res.font.normativepro_italic, FontWeight.Normal, FontStyle.Italic),
//    Font(Res.font.normativepro_medium, FontWeight.Medium),
//    Font(Res.font.normativepro_bold, FontWeight.Bold)
//)

internal val CnrTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Light, //FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Light, //FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = normativeProFontFamily,
        fontWeight = FontWeight.Light, //FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
)