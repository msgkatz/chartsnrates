package com.msgkatz.ratesapp.feature.chartgdx.base;

//import com.msgkatz.ratesapp.presentation.common.mvp.router.EmptyRouter;

import com.msgkatz.ratesapp.feature.common.mvp.router.EmptyRouter;

/**
 * Created by msgkatz on 14/09/2018.
 */

public interface ChartRouter extends EmptyRouter {

    void showMain();

    void removeExtras();
}
