package com.msgkatz.ratesapp.presentation.ui.main;

import android.support.annotation.Nullable;

import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.interactors.GetAssets;
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap;
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver;
import com.msgkatz.ratesapp.presentation.ui.main.base.BaseMainPresenter;
import com.msgkatz.ratesapp.utils.Logs;
import com.msgkatz.ratesapp.utils.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by msgkatz on 10/09/2018.
 */

public class QuoteAssetPresenter extends BaseMainPresenter<QuoteAssetView> {

    private final static String TAG = QuoteAssetPresenter.class.getSimpleName();

    @Inject
    GetQuoteAssetsMap mGetQuoteAssetsMap;

    @Inject
    GetToolListPrices mGetToolListPrices;

    private ResponseObserver observerToolListPrices;
    private ResponseObserver observerQuoteAssets;

    private String quoteAssetName;
    private Asset quoteAsset;

    @Inject
    public QuoteAssetPresenter(@Nullable String quoteAssetName)
    {
        this.quoteAssetName = quoteAssetName;
    }

    // TODO exclude this method after testings of iss-31
    public void setQuoteAsset(String quoteAssetName)
    {
        //this.quoteAssetName = quoteAssetName;
    }

    @Override
    public void onStart() {

        observerQuoteAssets = new ResponseObserver<Optional<Map<String, Asset>>, Map<String, Asset>>() {
            @Override
            public void doNext(Map<String, Asset> stringAssetMap) {
                quoteAsset = stringAssetMap.get(quoteAssetName);

                if (getView() !=  null)
                    getView().updateQuoteAsset(quoteAsset);
                initToolListPrices();
            }
        };

        if (quoteAsset == null)
        {
            mGetQuoteAssetsMap.execute(observerQuoteAssets, null);
        }
        else
        {
            initToolListPrices();
        }

    }



    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        if (observerQuoteAssets != null)
            observerQuoteAssets.dispose();
        if (observerToolListPrices != null)
            observerToolListPrices.dispose();
    }

    private void initToolListPrices()
    {
        observerToolListPrices = new ResponseObserver<Optional<Map<String,Set<PriceSimple>>>, Map<String, Set<PriceSimple>>>() {
            @Override
            public void doNext(Map<String, Set<PriceSimple>> stringSetMap) {


                Set<PriceSimple> prices = stringSetMap.get(quoteAsset.getNameShort());
                List<PriceSimple> priceSimpleList = new ArrayList<>(prices);

                //Collections.sort(priceSimpleList);

                if (getView() !=  null)
                    getView().updatePriceList(priceSimpleList);

                if (Parameters.DEBUG)
                {
                    StringBuilder sb = new StringBuilder(quoteAsset.getNameShort());
                    sb.append("::");
                    for (PriceSimple item : prices) {

                        sb.append(item.toString());
                        sb.append("__");
                    }
                    Logs.d(TAG, sb.toString() + "\n");
                    Logs.d(TAG, "++++===============++++" + "\n");
                }
            }
        };

        mGetToolListPrices.execute(observerToolListPrices, null);

    }

//    @Override
//    public void inject() {
//        App.getComponent().inject(this);
//    }
}
