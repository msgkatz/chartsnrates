package com.msgkatz.ratesapp.di.app;

import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.interactors.GetAssets;
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPrice;
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPricesInterim;
import com.msgkatz.ratesapp.domain.interactors.GetIntervalByName;
import com.msgkatz.ratesapp.domain.interactors.GetIntervals;
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo;
import com.msgkatz.ratesapp.domain.interactors.GetPriceHistory;
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssets;
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap;
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices;
import com.msgkatz.ratesapp.domain.interactors.GetTools;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by msgkatz on 21/08/2018.
 */

@Module
public class InteractorsModule {

    @Provides
    @Singleton
    GetAssets provideGetAssets(IDataRepo repo) {
        return new GetAssets(repo);
    }

    @Provides
    @Singleton
    GetQuoteAssets provideGetQuoteAssets(IDataRepo repo) {
        return new GetQuoteAssets(repo);
    }

    @Provides
    @Singleton
    GetQuoteAssetsMap provideGetQuoteAssetsMap(IDataRepo repo) {
        return new GetQuoteAssetsMap(repo);
    }

    @Provides
    @Singleton
    GetPlatformInfo provideGetPlatformInfo(IDataRepo repo) {
        return new GetPlatformInfo(repo);
    }

    @Provides
    @Singleton
    GetToolListPrices provideGetToolListPrices(IDataRepo repo) {
        return new GetToolListPrices(repo);
    }

    @Provides
    @Singleton
    GetPriceHistory provideGetPriceHistory(IDataRepo repo) {
        return new GetPriceHistory(repo);
    }

    @Provides
    @Singleton
    GetCurrentPrice provideGetCurrentPrice(IDataRepo repo) { return new GetCurrentPrice(repo); }

    @Provides
    @Singleton
    GetCurrentPricesInterim provideGetCurrentPricesInterim(IDataRepo repo) { return new GetCurrentPricesInterim(repo); }

    @Provides
    @Singleton
    GetTools provideGetTools(IDataRepo repo) { return new GetTools(repo); }

    @Provides
    @Singleton
    GetIntervals provideGetIntervals(IDataRepo repo) { return new GetIntervals(repo); }

    @Provides
    @Singleton
    GetIntervalByName provideGetIntervalByName(IDataRepo repo) { return new GetIntervalByName(repo); }


}
