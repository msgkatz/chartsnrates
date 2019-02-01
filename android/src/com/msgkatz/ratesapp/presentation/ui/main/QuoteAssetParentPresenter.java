package com.msgkatz.ratesapp.presentation.ui.main;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo;
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver;
import com.msgkatz.ratesapp.presentation.ui.main.base.BaseMainPresenter;
import com.msgkatz.ratesapp.utils.Logs;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by msgkatz on 11/09/2018.
 */

public class QuoteAssetParentPresenter extends BaseMainPresenter<QuoteAssetParentView> {

    private final static String TAG = QuoteAssetParentPresenter.class.getSimpleName();

    private final Relay<Object> _bus = PublishRelay.create().toSerialized();

    @Inject
    GetPlatformInfo mGetPlatformInfo;

    @Inject
    GetToolListPrices mGetToolListPrices;


    @Override
    public void onStart() {
        //initPlatformInfo();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    private void initPlatformInfo()
    {
        mGetPlatformInfo.execute(new ResponseObserver<Optional<PlatformInfo>, PlatformInfo>() {
            @Override
            public void doNext(PlatformInfo platformInfo) {
                if (platformInfo != null)
                    initToolListPrices();
            }
        }, null);
    }

    private void initToolListPrices()
    {
        mGetToolListPrices.execute(new ResponseObserver<Optional<Map<String,Set<PriceSimple>>>, Map<String, Set<PriceSimple>>>() {
            @Override
            public void doNext(Map<String, Set<PriceSimple>> stringSetMap) {


                _bus.accept(stringSetMap);
//                Set<PriceSimple> prices = stringSetMap.get(quoteAsset.getNameShort());
//                StringBuilder sb = new StringBuilder(quoteAsset.getNameShort());
//                sb.append("::");
//                for (PriceSimple item : prices) {
//
//                    sb.append(item.toString());
//                    sb.append("__");
//                }
//                Logs.d(TAG, sb.toString() + "\n");
//                Logs.d(TAG, "++++===============++++" + "\n");
            }
        }, null);

    }

//    @Override
//    public void inject() {
//        App.getComponent().inject(this);
//    }
}
