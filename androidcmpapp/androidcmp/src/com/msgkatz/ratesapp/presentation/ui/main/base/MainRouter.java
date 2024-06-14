package com.msgkatz.ratesapp.presentation.ui.main.base;

import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.presentation.common.mvp.router.EmptyRouter;

/**
 * Created by gkostyaev on 10/01/2018.
 */
public interface MainRouter extends EmptyRouter {

    void showChart(PriceSimple priceSimple);
}
