package com.msgkatz.ratesapp.presentation.ui.splash2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.core.uikit.theme.CnrThemeAlter
import com.msgkatz.ratesapp.core.uikit.theme.GradientColors
import com.msgkatz.ratesapp.core.uikit.theme.LocalGradientColors
import com.msgkatz.ratesapp.core.uikit.theme.component.CnrBackground
import com.msgkatz.ratesapp.core.uikit.theme.component.CnrGradientBackground


@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
    interimVMKeeper : SplashKeeper,
    onContinue: () -> Unit = {},
) {
    val viewModel: SplashViewModel = interimVMKeeper.makeSplash5()
    val uiState by viewModel.uiState.collectAsState()

    SplashScreen(
        splashUIState = uiState,
        onReconnect = { viewModel.tryReconnect() },
        onContinue = onContinue,
        modifier = modifier,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    splashUIState: SplashUIState,
    onReconnect: () -> Unit = {},
    onContinue: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    CnrBackground {
        val shouldShowGradientBackground = true
        CnrGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            //var isError by rememberSaveable { mutableStateOf(splashUIState) }
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) { it ->
                Column(
                    modifier = modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                        ) {
                        Image(
                            modifier = Modifier
                                .align(alignment = Alignment.Center),
                            contentScale = ContentScale.Fit,
                            painter = painterResource(R.drawable.logo),
                            contentDescription = null,
                        )
                    }
                    if (splashUIState.errorLoading) {
                        FilledTonalButton(onClick = onReconnect) {
                            Text(stringResource(R.string.screen_splash))
                        }
                    }
                    if (splashUIState.loaded) {
                        onContinue.invoke()
                    }

                }

            }
        }
    }


}

@Preview
@Composable
fun SplashScreenPreview(){
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        SplashScreen(
            splashUIState = SplashUIState(loading = true)
        )
    }

}