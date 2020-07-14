package com.msgkatz.ratesapp.di.app;

import android.content.Context;

import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.data.net.rest.ApiBuilder;
import com.msgkatz.ratesapp.data.net.rest.BinanceHtmlApi;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.data.repo.DataRepo;
import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer;
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus;
import com.msgkatz.ratesapp.presentation.common.messaging.RxBus;
import com.msgkatz.ratesapp.utils.Parameters;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    public static final String APP_CONTEXT = "AppModule.applicationContext";

    @Provides
    @Named(APP_CONTEXT)
    @Singleton
    Context providesAppContext(App application)
    {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    IDataRepo providesDataRepo(DataRepo dataRepo)
    {
        return dataRepo;
    }

    @Provides
    @Singleton
    BinanceRestApi providesRestApi()
    {
        return ApiBuilder.getRestApiInterface(Parameters.BASE_URL_REST);
    }

    @Provides
    @Singleton
    BinanceHtmlApi providesHtmlApi()
    {
        return ApiBuilder.getHtmlApiInterface(Parameters.BASE_URL);
    }


    @Provides
    @Singleton
    BinanceWSocksApi providesWSocksApi(App application)
    {
        return com.msgkatz.ratesapp.data.net.wsocks.ApiBuilder.getApiInterface(application.getApplicationContext());
    }

    @Provides
    @Singleton
    IRxBus providesUIMessaging()
    {
        return new RxBus();
    }

    @Provides
    @Singleton
    TabInfoStorer providesTabInfo(App application)
    {
        return new TabInfoStorer(application.getApplicationContext());
    }

}
