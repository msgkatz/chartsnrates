package com.msgkatz.ratesapp.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.theme.GradientColors
import com.msgkatz.ratesapp.presentation.theme.LocalGradientColors
import com.msgkatz.ratesapp.presentation.theme.component.CnrBackground
import com.msgkatz.ratesapp.presentation.theme.component.CnrGradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    splashUIState: SplashUIState,
    modifier: Modifier = Modifier
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
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) { it ->
                Column(
                    modifier = modifier.padding(it),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Box(modifier = modifier.fillMaxWidth().height(180.dp),
                        ) {
                        Image(
                            modifier = Modifier
                                //.wrapContentSize(align = Center)
                                .align(alignment = Alignment.Center),
                            contentScale = ContentScale.Fit,
                            painter = painterResource(R.drawable.logo),
                            // TODO b/226661685: Investigate using alt text of  image to populate content description
                            contentDescription = null, // decorative image,
                        )
                    }

                    Text(text = "ji", modifier = modifier.padding(it))

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