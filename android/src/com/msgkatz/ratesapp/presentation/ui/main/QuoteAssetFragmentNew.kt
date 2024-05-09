package com.msgkatz.ratesapp.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.msgkatz.ratesapp.databinding.FragmentQuoteAssetAlterNewBinding
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.ui.chart.ChartActivity
import com.msgkatz.ratesapp.presentation.ui.main.base.BaseMainFragment
import com.msgkatz.ratesapp.presentation.ui.main.widget.QuoteAssetScreen
import com.msgkatz.ratesapp.presentation.ui.main.widget.QuoteAssetViewModel
import javax.inject.Inject

class QuoteAssetFragmentNew: BaseMainFragment() {

    //private val viewModel: QuoteAssetViewModel by viewModels()

    @Inject
    lateinit var viewModel: QuoteAssetViewModel

    private var binding: FragmentQuoteAssetAlterNewBinding? = null

    private var mQuoteAssetName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mQuoteAssetName = it.getString(KEY_QUOTE_ASSET_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuoteAssetAlterNewBinding
            .inflate(inflater, container, false)
            .apply {
                composeView.apply {
                    // Dispose the Composition when the view's LifecycleOwner
                    // is destroyed
                    setViewCompositionStrategy(
                        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                    )
                    setContent {
                        val quoteAssetUiState by viewModel.quoteAssetUiState.collectAsState()
                        val priceListUiState by viewModel.priceListUiState.collectAsState()
                        CnrThemeAlter(
                            darkTheme = true,
                            androidTheme = false, //shouldUseAndroidTheme(uiState),
                            disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
                        ) {
                            QuoteAssetScreen(
                                quoteAssetUIState = quoteAssetUiState,
                                priceListUIState = priceListUiState,
                                onPriceItemClick = { it -> showChart(it) }
                            )
                        }
                    }
                }
            }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showChart(priceSimple: PriceSimple?) {
        val intent: Intent = Intent(requireActivity(), ChartActivity::class.java)
        priceSimple?.let {
            intent.putExtra(ChartActivity.KEY_TOOL_NAME, it.getTool().name)
            intent.putExtra(ChartActivity.KEY_TOOL_PRICE, it.getPrice())
        }
        startActivity(intent)
    }

    override fun getPresenter(): BasePresenter<*, *>? = null

    override fun getFragmentName(): String? = mQuoteAssetName

    companion object {
        val TAG: String = QuoteAssetFragment::class.java.simpleName

        const val KEY_QUOTE_ASSET_NAME: String = "com.msgkatz.ratesapp.quoteasset.name"
        fun newInstance(quoteAssetName: String): QuoteAssetFragmentNew {
            val arguments = Bundle().apply {
                putString(KEY_QUOTE_ASSET_NAME, quoteAssetName)
            }
            return QuoteAssetFragmentNew().apply {
                this.arguments = arguments
            }

        }
    }

}