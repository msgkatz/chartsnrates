package com.msgkatz.ratesapp.feature.rootkmp.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.feature.rootkmp.rootkmp.generated.resources.Res
import com.msgkatz.ratesapp.feature.rootkmp.rootkmp.generated.resources.logo
import com.msgkatz.ratesapp.feature.rootkmp.rootkmp.generated.resources.screen_splash
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SplashContent(
    modifier: Modifier = Modifier,
    component: SplashIFace,
    onContinue: () -> Unit = {},
) {
    val uiState by component.uiState.collectAsState()

    SplashScreen(
        splashUIState = uiState,
        onReconnect = { component.tryReconnect() },
        onContinue = onContinue,
        modifier = modifier,
    )
}

@Composable
fun SplashScreen(
    splashUIState: SplashUIState,
    onReconnect: () -> Unit = {},
    onContinue: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = Color.Transparent,
        //backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { it ->
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.Center),
                    contentScale = ContentScale.Fit,
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = null,
                )
            }
            if (splashUIState.errorLoading) {
                FilledTonalButton(onClick = onReconnect) {
                    Text(stringResource(Res.string.screen_splash))
                }
            }
            if (splashUIState.loaded) {
                onContinue.invoke()
            }

        }

    }
}

@Preview
@Composable
fun SplashScreenPreview(){
    SplashScreen(
        splashUIState = SplashUIState(loading = true)
    )
}