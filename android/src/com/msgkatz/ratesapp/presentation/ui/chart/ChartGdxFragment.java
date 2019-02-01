package com.msgkatz.ratesapp.presentation.ui.chart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.entities.ToolFormat;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartGdxFragment;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartGdxPresenter;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.common.ChartGdxGame;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.PreRenderer;
import com.msgkatz.ratesapp.utils.Logs;

import javax.inject.Inject;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class ChartGdxFragment extends BaseChartGdxFragment implements ChartGdxView {

    public static final String TAG = ChartGdxFragment.class.getSimpleName();

    public static final String KEY_TOOL_NAME = "com.msgkatz.ratesapp.tool.name";
    public static final String KEY_TOOL_FORMAT = "com.msgkatz.ratesapp.tool.format";
    public static final String KEY_TOOL_INTERVAL = "com.msgkatz.ratesapp.tool.interval";

    @Inject
    BaseChartGdxPresenter mChartGdxPresenter;

    private AndroidApplicationConfiguration androidApplicationConfiguration;
    private ChartGdxGame chartGdxGame;
    private PreRenderer preRenderer;
    private String mToolName;
    private ToolFormat mToolFormat;
    private String mInterval;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mToolName = getArguments().getString(KEY_TOOL_NAME);
            mToolFormat = getArguments().getParcelable(KEY_TOOL_FORMAT);
            mInterval = getArguments().getString(KEY_TOOL_INTERVAL);
        }

        this.preRenderer = new PreRenderer();
        this.preRenderer.setToolFormat(mToolFormat);
        this.mChartGdxPresenter.setToolFormat(mToolFormat);
        this.mChartGdxPresenter.setToolName(mToolName);
        this.mChartGdxPresenter.setController(preRenderer);
        this.mChartGdxPresenter.setInterval(mInterval);
        this.chartGdxGame = new ChartGdxGame(getActivity(), this.preRenderer);

        this.androidApplicationConfiguration = new AndroidApplicationConfiguration();
        this.androidApplicationConfiguration.useImmersiveMode = false;
        this.androidApplicationConfiguration.numSamples = 4;
        this.androidApplicationConfiguration.useAccelerometer = false;
        this.androidApplicationConfiguration.useGyroscope = false;
        this.androidApplicationConfiguration.useCompass = false;
        this.androidApplicationConfiguration.useGLSurfaceView20API18 = true;

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logs.d(this, "onCreateView...");
        if (Gdx.graphics == null || ((AndroidGraphics) Gdx.graphics).getView() == null || Gdx.input == null || this.input == null) {
            return initializeForView(this.chartGdxGame, this.androidApplicationConfiguration);
        }
        return ((AndroidGraphics) Gdx.graphics).getView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Gdx.graphics.supportsDisplayModeChange();
        Gdx.graphics.setContinuousRendering(true);
    }

    public void updateToolInfo(String toolName, ToolFormat toolFormat, String interval)
    {
        this.mToolName = toolName;
        this.mToolFormat = toolFormat;
        this.mInterval = interval;
        this.mChartGdxPresenter.setToolFormat(mToolFormat);
        this.mChartGdxPresenter.setToolName(mToolName);
        this.mChartGdxPresenter.setInterval(mInterval);
    }

    @Override
    public BasePresenter getPresenter() {
        return mChartGdxPresenter;
    }
}
