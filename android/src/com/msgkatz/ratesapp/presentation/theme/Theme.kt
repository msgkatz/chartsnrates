package com.msgkatz.ratesapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.msgkatz.ratesapp.R




@Composable
fun CnrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColors  = lightColors()
    val darkColors  = darkColors()
    val colors = if (darkTheme) darkColors else lightColors

//    val composeThemeAdapter = ComposeThemeAdapter()
//    val context = LocalContext.current
//    val theme = context.resources.getXml(R.xml.styles)
//    val composeTheme = composeThemeAdapter.getTheme(theme, context)

    MaterialTheme(
        colors = colors,
        content = content
    )
}