package com.msgkatz.ratesapp.presentation.ui.main;

import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.presentation.common.mvp.BaseView;

import java.util.List;

/**
 * Created by msgkatz on 09/09/2018.
 */

public interface QuoteAssetView extends BaseView {

    void updateQuoteAsset(Asset quoteAset);
    void updatePriceList(List<PriceSimple> list);
}
