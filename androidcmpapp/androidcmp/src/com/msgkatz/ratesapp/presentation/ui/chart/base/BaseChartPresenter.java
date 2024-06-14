package com.msgkatz.ratesapp.presentation.ui.chart.base;

import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.common.mvp.BaseView;

/**
 * Created by msgkatz on 14/09/2018.
 */

public abstract class BaseChartPresenter<vView extends BaseView>
        extends BasePresenter<vView, ChartRouter> {
}
