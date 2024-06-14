package com.msgkatz.ratesapp.data.net.rest;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.msgkatz.ratesapp.BuildConfig;
import com.msgkatz.ratesapp.data.net.Api;
import com.msgkatz.ratesapp.utils.Parameters;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by msgkatz on 01/08/2018.
 */

public class ApiBuilder implements Api.Rest {

    public static BinanceRestApi getRestApiInterface(String url) {

        OkHttpClient okHttpClient = createHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(BinanceRestApi.class);
    }

    public static BinanceHtmlApi getHtmlApiInterface(String url) {

        OkHttpClient okHttpClient = createHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(BinanceHtmlApi.class);
    }


    private static OkHttpClient createHttpClient() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    //.header("X-Extra-Header", "1.0")
                    //.header("User-Agent", "binance_android")
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        if (Parameters.DEBUG)
        {
            httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        }

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(logging);
        }
        return httpClientBuilder.build();
    }
}
