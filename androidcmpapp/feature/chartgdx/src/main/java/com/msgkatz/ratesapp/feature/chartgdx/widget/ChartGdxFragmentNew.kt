package com.msgkatz.ratesapp.feature.chartgdx.widget

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidGraphics
import com.msgkatz.ratesapp.feature.chartgdx.base.BaseGdxCompFragment
import com.msgkatz.ratesapp.feature.chartgdx.entities.ToolFormat
import com.msgkatz.ratesapp.old.utils.Logs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChartGdxFragmentNew : BaseGdxCompFragment() { //BaseChartGdxFragment() {

    companion object {
        val TAG: String = "ChartGdxFragmentNew2" //ChartGdxFragmentNew::class.java.getSimpleName()

        const val KEY_TOOL_NAME: String = "com.msgkatz.ratesapp.tool.name"
        const val KEY_TOOL_FORMAT: String = "com.msgkatz.ratesapp.tool.format"
        const val KEY_TOOL_INTERVAL: String = "com.msgkatz.ratesapp.tool.interval"
    }

    @Inject
    lateinit var parentviewmodel: ChartParentViewModel

    @Inject
    lateinit var viewmodel: ChartGdxViewModel

//    private val androidApplicationConfiguration: AndroidApplicationConfiguration? = null
//    private val chartGdxGame: ChartGdxGame? = null
//    private val preRenderer: PreRenderer? = null
    private var mToolName: String? = null
    private var mToolFormat: ToolFormat? = null
    private var mInterval: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as? ChartActivityNew)?.chartComponent?.inject(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mToolName = requireArguments().getString(ChartGdxFragmentNew.KEY_TOOL_NAME)
            mToolFormat = requireArguments().getParcelable(ChartGdxFragmentNew.KEY_TOOL_FORMAT)
            mInterval = requireArguments().getString(ChartGdxFragmentNew.KEY_TOOL_INTERVAL)
        }



        viewmodel.updateParams(
            toolName = (requireActivity() as ChartActivityNew).viewModel.mToolName!!,
            toolFormat = (requireActivity() as ChartActivityNew).viewModel.getToolFormat(),
            interval = (requireActivity() as ChartActivityNew).viewModel.mInterval

        )

//        viewmodel.updateParams(
//            toolName = parentviewmodel.mToolName!!,
//            toolFormat = parentviewmodel.getToolFormat(),
//            interval = parentviewmodel.mInterval
//
//        )

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                (requireActivity() as ChartActivityNew).viewModel.chartParentForGdxUIState.collect { it ->
                    when (it) {
                        is ChartParentForGdxUIState.Loading, is ChartParentForGdxUIState.Empty -> {}
                        is ChartParentForGdxUIState.Data -> {

//                            viewmodel.updateParams(
//                                toolName = it.toolName!!,
//                                toolFormat = it.toolFormat,
//                                interval = it.interval
//
//                            )
                        }
                    }

                }
            }
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

    override fun onStart() {
        super.onStart()
        viewmodel.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewmodel.onStop()
    }

//    fun updateToolInfo(toolName: String?, toolFormat: ToolFormat?, interval: String?) {
//        this.mToolName = toolName
//        this.mToolFormat = toolFormat
//        this.mInterval = interval
//        this.mChartGdxPresenter.setToolFormat(mToolFormat)
//        this.mChartGdxPresenter.setToolName(mToolName)
//        this.mChartGdxPresenter.setInterval(mInterval)
//    }


    //override fun getPresenter() = null
}



fun CoroutineScope.launchUntilPaused(lifecycleOwner: LifecycleOwner, block: suspend CoroutineScope.() -> Unit){
    val job = launch(block = block)
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            job.cancel()
            lifecycleOwner.lifecycle.removeObserver(this)
        }
    })
}