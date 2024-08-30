package com.msgkatz.ratesapp.presentation.ui.chart.base;

import com.msgkatz.ratesapp.presentation.common.mvp.router.EmptyRouter;

/**
 * Created by msgkatz on 14/09/2018.
 */

public interface ChartRouter extends EmptyRouter {

    void showMain();

    void removeExtras();
}
