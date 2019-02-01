package com.msgkatz.ratesapp.di.app;

import android.content.Context;

import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.data.net.rest.ApiBuilder;
import com.msgkatz.ratesapp.data.net.rest.BinanceHtmlApi;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.data.repo.DataRepo;
import com.msgkatz.ratesapp.domain.IDataRepo;
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

//    private final App application;
//    private final BinanceHtmlApi htmlApi;
//    private final BinanceRestApi restApi;
//    private final BinanceWSocksApi wsApi;
//
//    private InnerModel innerModel;
//    private AssetToolDataStore assetToolDataStore;
//    private CurrentToolPriceDataStore currentToolPriceDataStore;
//    private ToolListPriceDataStore toolListPriceDataStore;


    //public AppModule(App application)
//    public AppModule()
//    {
//        this.application = App.getInstance();
//
//        this.htmlApi = ApiBuilder.getHtmlApiInterface(Parameters.BASE_URL);
//        this.restApi = ApiBuilder.getRestApiInterface(Parameters.BASE_URL_REST);
//        this.wsApi = com.msgkatz.ratesapp.data.net.wsocks.ApiBuilder.getApiInterface(application.getApplicationContext());
//
//        this.innerModel = new InnerModel();
//        this.assetToolDataStore = new AssetToolDataStore(application.getApplicationContext(), restApi, htmlApi, innerModel);
//        this.currentToolPriceDataStore = new CurrentToolPriceDataStore(restApi, wsApi, innerModel);
//        this.toolListPriceDataStore = new ToolListPriceDataStore(restApi, wsApi, innerModel);
//    }
//
//    @Provides
//    @Singleton
//    BinanceRestApi providesRestApi()
//    {
//        return restApi;
//    }
//
//    @Provides
//    @Singleton
//    BinanceWSocksApi providesWSocksApi()
//    {
//        return wsApi;
//    }
//

    @Provides
    @Named(APP_CONTEXT)
    @Singleton
    Context providesAppContext(App application)
    {
        return application.getApplicationContext();
    }

//    @Binds
//    abstract Context bindContext(App application);

    @Provides
    @Singleton
    IDataRepo providesDataRepo(DataRepo dataRepo)
    {
        //return new DataRepo(assetToolDataStore, currentToolPriceDataStore, toolListPriceDataStore);
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

}
