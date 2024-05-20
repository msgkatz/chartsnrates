package com.msgkatz.ratesapp.presentation.ui.main.widget

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.common.activity.BaseCompActivity
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.ui.app.CnrApp
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper
import com.msgkatz.ratesapp.presentation.ui.chart.ChartActivity
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
        interimVMKeeper = InterimVMKeeper(this, viewModel.mGetAssets, viewModel.mGetPlatformInfo,
            viewModel.mGetQuoteAssetsMap, viewModel.mGetToolListPrices, tabInfoStorer)
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
        val intent: Intent = Intent(this, ChartActivity::class.java)
        priceSimple?.let {
            intent.putExtra(ChartActivity.KEY_TOOL_NAME, it.getTool().name)
            intent.putExtra(ChartActivity.KEY_TOOL_PRICE, it.getPrice())
        }
        startActivity(intent)
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

    }
}