package com.msgkatz.ratesapp.old.data.net.wsocks;

import android.content.Context;
import android.text.TextUtils;

import com.jakewharton.rxrelay2.PublishRelay;


import com.msgkatz.ratesapp.old.R;
import com.msgkatz.ratesapp.old.data.net.wsocks.client.BinanceWSocksClient;
import com.msgkatz.ratesapp.old.utils.Logs;
import com.msgkatz.ratesapp.old.utils.Parameters;
import com.msgkatz.ratesapp.old.utils.WorkerThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by msgkatz on 03/08/2018.
 */

public class WSocksController implements BinanceWSocksApi, IWSocksHandler {

    public static final String TAG = WSocksController.class.getSimpleName();

    private Context context;
    private String currentUrl;

    private List<BinanceWSocksClient> wSocksClientList = new ArrayList<>();
    private Map<String, BinanceWSocksClient> wSocksClientMap = new HashMap<>();

    private PublishRelay<String> relayBase;
    //private Map<Type, PublishRelay<Object>> relayMap = new HashMap<>();

    private WorkerThread thread;

    //private GsonConverterFactory converterFactory;

    public WSocksController(Context context)
    {
        this.context = context;
        this.relayBase = PublishRelay.create();

        thread = new WorkerThread();
        thread.start();

        //this.converterFactory = GsonConverterFactory.create();
    }

    private synchronized void connect2(String url)
    {
        Logs.e(TAG, String.format("pre: currentUrl=%1$s, newUrl=%2$s", currentUrl, url));
        if (url != null && !url.equals(currentUrl)) {
            this.currentUrl = url;

            for (BinanceWSocksClient client : wSocksClientList)
            {
                client.disconnect("");
                wSocksClientMap.remove(client.getHashKey());
            }

            wSocksClientList.clear();
        }

        Logs.e(TAG, String.format("post: currentUrl=%1$s, newUrl=%2$s", currentUrl, url));

        String _hash = generateHashKey();
        //BinanceWSocksClient client = BinanceWSocksClient.create(context,this, url, _hash);
        BinanceWSocksClient client = BinanceWSocksClient.create(context,this, currentUrl, _hash);
        client.connect("");
        wSocksClientList.add(client);
        wSocksClientMap.put(_hash, client);
    }

    private synchronized void connect(String url)
    {
        Logs.e(TAG, String.format("pre: currentUrl=%1$s, newUrl=%2$s", currentUrl, url));




        String _hash = generateHashKey(url);

        BinanceWSocksClient client = wSocksClientMap.get(_hash);
        if (client == null) {
            this.currentUrl = url;

            for (BinanceWSocksClient cl : wSocksClientList)
            {
                cl.disconnect("");
                wSocksClientMap.remove(cl.getHashKey());
            }

            wSocksClientList.clear();

            client = BinanceWSocksClient.create(context,this, currentUrl, _hash, client);
            wSocksClientList.add(client);
            wSocksClientMap.put(_hash, client);
        }

        Logs.e(TAG, String.format("post: currentUrl=%1$s, newUrl=%2$s", currentUrl, url));
        client.connect("");

    }

    public void disconnect()
    {
        for (BinanceWSocksClient client : wSocksClientList)
        {
            client.disconnect("");
            wSocksClientMap.remove(client.getHashKey());
        }

        wSocksClientList.clear();
    }

    private String generateHashKey()
    {
        long time = System.currentTimeMillis();

        return Long.toString(time);
    }

    private String generateHashKey(String string)
    {
        //long time = System.currentTimeMillis();

        return Long.toString(string.hashCode());
    }

    @Override
    public void routeResponse(String hashKey, String response) {
        if (TextUtils.isEmpty(response))
            return;

        thread.doRunnable(new Runnable() {
            @Override
            public void run() {
                relayBase.accept(response);

                BinanceWSocksClient client = wSocksClientMap.get(hashKey);
                if (client != null) {
                    if (client.getMessageCount() == Parameters.WEBSOCK_MESSAGE_LIMIT)
                        connect(currentUrl);
                }
            }
        });

    }

    @Override
    public void onClientDisconnect(String hashKey) {
        BinanceWSocksClient client = wSocksClientMap.get(hashKey);
        if (client != null) {
            client.disconnect("");
            wSocksClientList.remove(client);
            wSocksClientMap.remove(client);
        }
    }

    @Override
    public void onClientError(String hashKey) {
        BinanceWSocksClient client = wSocksClientMap.get(hashKey);
        if (client != null) {
            client.disconnect("");
            wSocksClientList.remove(client);
            wSocksClientMap.remove(client);
        }
    }


    /**
     * BinanceWSocksApi methods implementation
     *
     * Single streams
     */
    @Override
    public Flowable<String> getKlineStream(String symbol, String interval) {
        thread.doRunnable(new Runnable() {
            @Override
            public void run() {
                connect(String.format(Locale.getDefault(),
                        Parameters.BASE_URL_WSOCK
                                + Parameters.BASE_URL_WSOCK_RAW
                                + context.getString(R.string.ws_kline), symbol, interval));
            }
        });
        return relayBase.toFlowable(BackpressureStrategy.DROP);
    }

    @Override
    public Flowable<String> getMiniTickerStream(String symbol) {
        thread.doRunnable(new Runnable() {
            @Override
            public void run() {
                connect(String.format(Locale.getDefault(),
                        Parameters.BASE_URL_WSOCK
                                + Parameters.BASE_URL_WSOCK_RAW
                                + context.getString(R.string.ws_mini_ticker), symbol));
            }
        });
        return relayBase.toFlowable(BackpressureStrategy.DROP);
    }

    @Override
    public Flowable<String> getMiniTickerStreamAll() {
        thread.doRunnable(new Runnable() {
            @Override
            public void run() {
                connect(String.format(Locale.getDefault(),
                        Parameters.BASE_URL_WSOCK
                                + Parameters.BASE_URL_WSOCK_RAW
                                + context.getString(R.string.ws_mini_ticker_all)));
            }
        });
        return relayBase.toFlowable(BackpressureStrategy.DROP);
    }

    @Override
    public Flowable<String> getTickerStream(String symbol) {
        thread.doRunnable(new Runnable() {
            @Override
            public void run() {
                connect(String.format(Locale.getDefault(),
                        Parameters.BASE_URL_WSOCK
                                + Parameters.BASE_URL_WSOCK_RAW
                                + context.getString(R.string.ws_ticker), symbol));
            }
        });
        return relayBase.toFlowable(BackpressureStrategy.DROP);
    }

    @Override
    public Flowable<String> getTickerStreamAll() {
        thread.doRunnable(new Runnable() {
            @Override
            public void run() {
                connect(String.format(Locale.getDefault(),
                        Parameters.BASE_URL_WSOCK
                                + Parameters.BASE_URL_WSOCK_RAW
                                + context.getString(R.string.ws_ticker_all)));
            }
        });
        return relayBase.toFlowable(BackpressureStrategy.DROP);
    }

    /**
     * BinanceWSocksApi methods implementation
     *
     * Combo streams
     */
    @Override
    public Flowable<String> getKlineAndMiniTickerComboStream(String symbol, String interval) {
        thread.doRunnable(new Runnable() {
            @Override
            public void run() {
                connect(String.format(Locale.getDefault(),
                        Parameters.BASE_URL_WSOCK
                                + Parameters.BASE_URL_WSOCK_COMBINED
                                + context.getString(R.string.ws_kline),
                        symbol, interval)
                        + "/"
                        + String.format(Locale.getDefault(), context.getString(R.string.ws_mini_ticker),
                        symbol)
                );
            }
        });
        return relayBase.toFlowable(BackpressureStrategy.DROP);
    }
}
