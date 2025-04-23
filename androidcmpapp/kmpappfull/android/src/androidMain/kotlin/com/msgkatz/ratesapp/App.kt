package com.msgkatz.ratesapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msgkatz.ratesapp.core.uikit.theme.GradientColors
import com.msgkatz.ratesapp.core.uikit.theme.LocalGradientColors
import com.msgkatz.ratesapp.core.uikit.theme.component.CnrBackground
import com.msgkatz.ratesapp.core.uikit.theme.component.CnrGradientBackground
import com.msgkatz.ratesapp.feature.rootkmp.RootContent
import com.msgkatz.ratesapp.feature.rootkmp.RootIFace
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

//import ratesappcmpfull.composeapp.generated.resources.Res
//import ratesappcmpfull.composeapp.generated.resources.compose_multiplatform

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


//@Composable
//@Preview
//fun App(root: RootIFace) {
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
//}