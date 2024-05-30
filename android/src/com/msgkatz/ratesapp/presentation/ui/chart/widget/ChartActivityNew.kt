package com.msgkatz.ratesapp.presentation.ui.chart.widget

import android.content.res.Configuration
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.msgkatz.ratesapp.presentation.common.activity.BaseActivity
import com.msgkatz.ratesapp.presentation.common.activity.BaseCompActivity
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.theme.GradientColors
import com.msgkatz.ratesapp.presentation.theme.LocalGradientColors
import com.msgkatz.ratesapp.presentation.theme.component.CnrBackground
import com.msgkatz.ratesapp.presentation.theme.component.CnrGradientBackground
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper
import com.msgkatz.ratesapp.presentation.ui.chart.ChartActivity
import com.msgkatz.ratesapp.presentation.ui.chart.base.ChartRouter
import javax.inject.Inject

class ChartActivityNew : BaseActivity(), ChartRouter, AndroidFragmentApplication.Callbacks {
//class ChartActivityNew : BaseCompActivity(), AndroidFragmentApplication.Callbacks {

    @Inject
    lateinit var viewModel: ChartParentViewModel

    private var interimVMKeeper: InterimVMKeeper? = null

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //interimVMKeeper = InterimVMKeeper()

        setContent {
            val chartParentToolUiState by viewModel.chartParentToolUiState.collectAsState()
            val chartParentPriceUIState by viewModel.chartParentPriceUIState.collectAsState()
            CnrThemeAlter(
                darkTheme = true,
                androidTheme = false, //shouldUseAndroidTheme(uiState),
                disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
            ) {
                val shouldShowGradientBackground = true
                CnrBackground {
                    CnrGradientBackground(
                        gradientColors = if (shouldShowGradientBackground) {
                            LocalGradientColors.current
                        } else {
                            GradientColors()
                        },
                    ) {
                        Scaffold(
                            modifier = Modifier,
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                        ) { it ->
                            ChartParentScreen(
                                modifier = Modifier.padding(it),
                                chartParentToolUIState = chartParentToolUiState,
                                chartParentPriceUIState = chartParentPriceUIState,
                                onBackClick = { onBackPressed() },
                                onIntervalClick = { it -> viewModel.provideNewInterval(it) }
                            )
                        }
                    }
                }
            }

        }


        /**
         * Flags keeps screen On, but better to use wakelock (somewhere in bg)
         */
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val toolName = intent.getStringExtra(ChartActivity.KEY_TOOL_NAME)
        val toolPrice = intent.getDoubleExtra(ChartActivity.KEY_TOOL_PRICE, 0.0)


        //postponeEnterTransition();
        val mgr = getSystemService(POWER_SERVICE) as PowerManager
        if (mgr != null) {
            wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ":ChartsnRatesWakeLock")
        }

        initScreen(toolName!!, toolPrice)
    }

    override fun onStart() {
        super.onStart()
        handleOrientationChange()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()

        if (wakeLock != null) wakeLock!!.acquire()
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()


        if (wakeLock != null) wakeLock!!.release()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleOrientationChange()
    }

    private fun handleOrientationChange() {
        val islandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        with(viewModel) {
            setConfigurationChange(islandscape)
        }

//        if (chartParentFragment != null) {
//            chartParentFragment.setConfigurationChange(islandscape)
//        }
    }

    private fun initScreen(toolName: String, toolPrice: Double) {
        //add fragment
        //addBackStack(ChartParentFragment.newInstance(toolName, toolPrice), false)
        //TODO make via events
        with(viewModel) {
            setToolNamePrice(toolName, toolPrice)
        }
    }


    override fun exit() {}
    override fun showMain() {

    }

    override fun removeExtras() {

    }
}