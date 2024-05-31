package com.msgkatz.ratesapp.presentation.ui.main.widget

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.common.activity.BaseCompActivity
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.ui.app.CnrApp
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartActivityNew
import javax.inject.Inject

class MainActivityNew : BaseCompActivity() {

    //private val viewModel: MainActivityNewViewModel by viewModels()

    @Inject
    lateinit var viewModel: MainActivityNewViewModel

    @Inject
    lateinit var tabInfoStorer: TabInfoStorer

    private var interimVMKeeper: InterimVMKeeper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mgr = getSystemService(POWER_SERVICE) as PowerManager
        interimVMKeeper = InterimVMKeeper(this,
            viewModel.mGetAssets,
            viewModel.mGetPlatformInfo,
            viewModel.mGetQuoteAssetsMap,
            viewModel.mGetToolListPrices,
            tabInfoStorer
        )
        setContent {
            CnrThemeAlter(
                darkTheme = true,
                androidTheme = false, //shouldUseAndroidTheme(uiState),
                disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
            ) {
                CnrApp(
                    tabInfoStorer = tabInfoStorer,
                    onPriceItemClick = { it -> showChart(it) },
                    interimVMKeeper = interimVMKeeper!!
                )

            }

        }
    }

    private fun showChart(priceSimple: PriceSimple?) {
        val intent: Intent = Intent(this, ChartActivityNew::class.java)
        priceSimple?.let {
            intent.putExtra(ChartActivityNew.KEY_TOOL_NAME, it.getTool().name)
            intent.putExtra(ChartActivityNew.KEY_TOOL_PRICE, it.getPrice())
        }
        startActivity(intent)
    }

    /** ChartScreen related funcs **/
    private var wakeLock: PowerManager.WakeLock? = null
    private var mgr: PowerManager? = null
    private fun processScreenLock(toSet: Boolean) {
        if (toSet) {
            /**
             * Flags keeps screen On, but better to use wakelock (somewhere in bg)
             */
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            //if (mgr != null) {
            if (wakeLock == null)
                wakeLock = mgr?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ":ChartsnRatesWakeLock")
            //}
            wakeLock?.acquire(10 * 60 * 1000L /*10 minutes*/)

        } else {
            wakeLock?.release()
            /**
             * Flags keeps screen On, but better to use wakelock (somewhere in bg)
             */
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
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
fun CnrAppPreviewLight() {
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {

    }
}