package com.msgkatz.ratesapp.core.uikit.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(
                horizontal = 40.dp,
                vertical = 0.dp
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x1AFFFFFF),
                        Color(0xFFE0E9D1),
                        Color(0x1AFFFFFF)
                    )
                )
            )
    )
}