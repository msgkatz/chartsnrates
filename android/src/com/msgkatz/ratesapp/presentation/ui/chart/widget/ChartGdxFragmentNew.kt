package com.msgkatz.ratesapp.presentation.ui.chart.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidGraphics
import com.msgkatz.ratesapp.presentation.entities.ToolFormat
import com.msgkatz.ratesapp.presentation.ui.chart.ChartGdxFragment
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartGdxFragment
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.common.ChartGdxGame
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.PreRenderer
import com.msgkatz.ratesapp.utils.Logs
import javax.inject.Inject

class ChartGdxFragmentNew : BaseChartGdxFragment() {

    @Inject
    lateinit var viewmodel: ChartGdxViewModel

//    private val androidApplicationConfiguration: AndroidApplicationConfiguration? = null
//    private val chartGdxGame: ChartGdxGame? = null
//    private val preRenderer: PreRenderer? = null
    private var mToolName: String? = null
    private var mToolFormat: ToolFormat? = null
    private var mInterval: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mToolName = requireArguments().getString(ChartGdxFragment.KEY_TOOL_NAME)
            mToolFormat = requireArguments().getParcelable(ChartGdxFragment.KEY_TOOL_FORMAT)
            mInterval = requireArguments().getString(ChartGdxFragment.KEY_TOOL_INTERVAL)
        }

        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logs.d(this, "onCreateView...")
        if ((Gdx.graphics == null || (Gdx.graphics as AndroidGraphics).view == null || Gdx.input == null) || this.input == null) {
            return initializeForView(viewmodel.chartGdxGame, viewmodel.androidApplicationConfiguration)
        }
        return (Gdx.graphics as AndroidGraphics).view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Gdx.graphics.supportsDisplayModeChange()
        Gdx.graphics.isContinuousRendering = true
    }

//    fun updateToolInfo(toolName: String?, toolFormat: ToolFormat?, interval: String?) {
//        this.mToolName = toolName
//        this.mToolFormat = toolFormat
//        this.mInterval = interval
//        this.mChartGdxPresenter.setToolFormat(mToolFormat)
//        this.mChartGdxPresenter.setToolName(mToolName)
//        this.mChartGdxPresenter.setInterval(mInterval)
//    }


    override fun getPresenter() = null
}