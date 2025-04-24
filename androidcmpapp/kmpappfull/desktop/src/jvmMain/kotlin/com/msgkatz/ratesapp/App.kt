package com.msgkatz.ratesapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msgkatz.ratesapp.core.uikitkmp.theme.GradientColors
import com.msgkatz.ratesapp.core.uikitkmp.theme.LocalGradientColors
import com.msgkatz.ratesapp.core.uikitkmp.theme.component.CnrBackground
import com.msgkatz.ratesapp.core.uikitkmp.theme.component.CnrGradientBackground
import com.msgkatz.ratesapp.feature.rootkmp.RootContent
import com.msgkatz.ratesapp.feature.rootkmp.RootIFace
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun CnrApp(root: RootIFace) {
    val shouldShowGradientBackground = true

    CnrBackground {
        CnrGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            RootContent(root)
        }
    }
}
