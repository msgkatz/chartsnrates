package com.msgkatz.ratesapp.data.net.wsocks;

import android.content.Context;

import com.msgkatz.ratesapp.data.net.Api;

/**
 * Created by msgkatz on 02/08/2018.
 */

public class ApiBuilder implements Api.WSocks {

    public static BinanceWSocksApi getApiInterface(Context context)
    {
        BinanceWSocksApi api = new WSocksController(context);

        return api;

    }

}
