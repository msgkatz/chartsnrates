package com.msgkatz.ratesapp.feature.chartgdx.base;


import com.msgkatz.ratesapp.feature.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.feature.common.mvp.BaseView;

/**
 * Created by msgkatz on 14/09/2018.
 */

public abstract class BaseChartPresenter<vView extends BaseView>
        extends BasePresenter<vView, ChartRouter> {
}
