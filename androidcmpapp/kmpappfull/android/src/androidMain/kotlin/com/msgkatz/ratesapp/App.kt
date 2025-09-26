package com.msgkatz.ratesapp


import androidx.compose.runtime.*
import com.msgkatz.ratesapp.core.uikit.theme.GradientColors
import com.msgkatz.ratesapp.core.uikit.theme.LocalGradientColors
import com.msgkatz.ratesapp.core.uikit.theme.component.CnrBackground
import com.msgkatz.ratesapp.core.uikit.theme.component.CnrGradientBackground
import com.msgkatz.ratesapp.feature.rootkmp.RootContent
import com.msgkatz.ratesapp.feature.rootkmp.RootIFace
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
            //var showSplash by rememberSaveable { mutableStateOf(true) }
            RootContent(root)
        }
    }
}
