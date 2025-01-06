package com.msgkatz.ratesapp.presentation.ui.main.widget

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.data.network.rest.RestController
import com.msgkatz.ratesapp.domain.entities.PriceSimpleJava
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.common.activity.BaseCompActivity
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.ui.app.cnrapp2.CnrApp
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper
import com.msgkatz.ratesapp.presentation.ui.app.TmpDataKeeper
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartActivityNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        val coroutineScope = lifecycle.coroutineScope
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        val keeper = TmpDataKeeper(coroutineScope = null, //coroutineScope,
            ioDispatcher = ioDispatcher
        )
        interimVMKeeper = InterimVMKeeper(this,
            viewModel.mGetAssets,
            viewModel.mGetPlatformInfo,
            viewModel.mGetQuoteAssetsMap,
            viewModel.mGetToolListPrices,
            tabInfoStorer,
            tmpDataKeeper = keeper
        )
        setContent {
            CnrThemeAlter(
                darkTheme = true,
                androidTheme = false, //shouldUseAndroidTheme(uiState),
                disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
            ) {
                if (1 == 2) makeTEsts2(keeper = keeper)
                else
                CnrApp(
                    tabInfoStorer = tabInfoStorer,
                    onPriceItemClick = { it -> showChart(it) },
                    interimVMKeeper = interimVMKeeper!!
                )

            }

        }


    }

    private fun makeTEsts2(keeper: TmpDataKeeper) {
        val coroutineScope = lifecycle.coroutineScope
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        val symbol = "1000SATSUSDT"
        val symbol2 = "btcusdt"
        val interval = "5m"

        val startTime = null
        val endTime = 1724166300000
        val limit = 300
        lifecycleScope.launch {
            //val keeper = TmpDataKeeper(coroutineScope, ioDispatcher)
            val ver = 2
            if (ver == 1) {
                keeper.currentToolPriceRepository.getToolRealtimePriceCombo(symbol2, interval)
                    .collect {
                        println("${it?.toString()} ")

                    }
            } else if (ver == 2) {
                keeper.currentToolPriceRepository.getToolRealtimePrice(symbol2, interval)
                    .collect {
                        println("${it.toString()} ")

                    }
            }
        }
    }

    private fun makeTEsts() {
        val coroutineScope = lifecycle.coroutineScope
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        val symbol = "1000SATSUSDT"
        val interval = "5m"

        val startTime = null
        val endTime = 1724166300000
        val limit = 300
        lifecycleScope.launch {
            val res = RestController(coroutineScope, ioDispatcher)
                .getPriceByCandle(
                    symbol = symbol,
                    interval = interval,
                    startTime = startTime,
                    endTime = endTime,
                    limit = limit
                )
            if (res.isSuccess) {
                res.getOrNull()?.forEach {
                    println("list:")
                    it.forEach { item -> println(item) }
                }
            } else {
                res.exceptionOrNull()?.let { println("FOCKEN ERROR: ${it.message}") }
            }
            println("done kmm")
        }
    }

    private fun showChart(priceSimple: PriceSimpleJava?) {
        val intent: Intent = Intent(this, ChartActivityNew::class.java)
        priceSimple?.let {
            intent.putExtra(ChartActivityNew.KEY_TOOL_NAME, it.getTool().name)
            intent.putExtra(ChartActivityNew.KEY_TOOL_PRICE, it.getPrice())
        }
        startActivity(intent)
    }

    private fun showChart(priceSimple: PriceSimple?) {
        val intent: Intent = Intent(this, ChartActivityNew::class.java)
        priceSimple?.let {
            intent.putExtra(ChartActivityNew.KEY_TOOL_NAME, it.tool.name)
            intent.putExtra(ChartActivityNew.KEY_TOOL_PRICE, it.price)
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