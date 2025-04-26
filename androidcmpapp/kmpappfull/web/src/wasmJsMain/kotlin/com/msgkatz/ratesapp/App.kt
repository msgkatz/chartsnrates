package com.msgkatz.ratesapp

import androidx.compose.runtime.*
import com.msgkatz.ratesapp.core.uikitkmp.theme.GradientColors
import com.msgkatz.ratesapp.core.uikitkmp.theme.LocalGradientColors
import com.msgkatz.ratesapp.core.uikitkmp.theme.component.CnrBackground
import com.msgkatz.ratesapp.core.uikitkmp.theme.component.CnrGradientBackground
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
            RootContent(root)
        }
    }
}

//@Composable
//fun App() {
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