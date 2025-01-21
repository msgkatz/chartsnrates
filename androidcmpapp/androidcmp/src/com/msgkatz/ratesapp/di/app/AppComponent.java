package com.msgkatz.ratesapp.di.app;

import static com.msgkatz.ratesapp.old.data.DataParams.APP_CONTEXT;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.msgkatz.ratesapp.App;
//import com.msgkatz.ratesapp.domain.interactors.GetCurrentPrice;
//import com.msgkatz.ratesapp.domain.interactors.GetCurrentPricesInterim;
//import com.msgkatz.ratesapp.domain.interactors.GetIntervalByName;
//import com.msgkatz.ratesapp.domain.interactors.GetIntervals;
//import com.msgkatz.ratesapp.domain.interactors.GetPriceHistory;
//import com.msgkatz.ratesapp.domain.interactors.GetTools;
//import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus;
//import com.msgkatz.ratesapp.presentation.ui.chart2.base.di.ChartDeps;
//import com.msgkatz.ratesapp.feature.chartgdx.base.di.ChartDeps;
import com.msgkatz.ratesapp.feature.common.messaging.IRxBus;
import com.msgkatz.ratesapp.old.domain.interactors.*;
import com.msgkatz.ratesapp.presentation.ui.main.widget.MainActivityNew;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Provides;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        //AndroidSupportInjectionModule.class,
        AppModule.class,
        //ActivityBindingModule.class,
        InteractorsModule.class})
public interface AppComponent /**extends ChartDeps**/ {

//    @NonNull
//    @Override
//    GetIntervals getMGetIntervals();
//
//    @NonNull
//    @Override
//    GetTools getMGetTools();
//
//    //@Nullable
//    @Override
//    IRxBus getRxBus();
//
//    @Named(APP_CONTEXT)
//    Context getAppContext();
//
//    @NonNull
//    @Override
//    GetCurrentPrice getMGetCurrentPrice();
//
//    @NonNull
//    @Override
//    GetCurrentPricesInterim getMGetCurrentPricesInterim();
//
//    @NonNull
//    @Override
//    GetIntervalByName getMGetIntervalByName();
//
//    @NonNull
//    @Override
//    GetPriceHistory getMGetPriceHistory();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);
        AppComponent build();
    }

    void inject(App app);
    void inject(MainActivityNew mainActivityNew);
}
