package com.msgkatz.ratesapp.presentation.ui.splash

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.presentation.common.activity.BaseCompActivity

import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.ui.main.MainActivity
import javax.inject.Inject

class SplashActivityNew : BaseCompActivity() {


    //private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            CnrThemeAlter(
                darkTheme = true,
                androidTheme = false, //shouldUseAndroidTheme(uiState),
                disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
            ) {
                SplashScreen(
                    splashUIState = uiState,
                    onReconnect = { viewModel.tryReconnect() },
                    onContinue = { showHomeActivity() }
                )
            }
        }
    }
    private fun showHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}



@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun ReplyAppPreviewLight() {

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