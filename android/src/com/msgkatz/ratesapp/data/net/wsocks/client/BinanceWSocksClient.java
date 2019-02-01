package com.msgkatz.ratesapp.data.net.wsocks.client;

import android.content.Context;
import android.text.TextUtils;

import com.msgkatz.ratesapp.data.entities.wsocks.WSocksStatusType;
import com.msgkatz.ratesapp.data.net.wsocks.IWSocksHandler;
import com.msgkatz.ratesapp.utils.Logs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Created by msgkatz on 02/08/2018.
 */

public class BinanceWSocksClient {

    private static final String TAG = BinanceWSocksClient.class.getSimpleName();

    private Context context;
    private IWSocksHandler wSocksHandler;
    private String wSocksUrl;
    private String hashKey;
    private int messageCount;

    private static volatile BinanceWSocksClient binanceWSocksClient;
    //private /*static*/ volatile BinanceWSocksClient binanceWSocksClient;

    private static volatile OkHttpClient okHttpClient =
            new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .pingInterval(4, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

    private Thread webSocketConnectionThread;
    private volatile WebSocket webSocket;
    private volatile WSocksStatusType webSocketStatus = WSocksStatusType.getType(WSocksStatusType.DISCONNECT);
    //BinanceWSocksClient


    //private WebSocketCallBack f16498q = new C40386(this);
    private WebSocketCallBackInner webSocketCallBackInner = new WebSocketCallBackInner(this);

    class WebSocketCallBackInner extends WSocksListener {

        final BinanceWSocksClient binanceWSocksClientInternal;

        WebSocketCallBackInner(BinanceWSocksClient client)
        {
            binanceWSocksClientInternal = client;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            if (webSocket.equals(this.binanceWSocksClientInternal.webSocket)) {
                this.binanceWSocksClientInternal.webSocketStatus.setCurrentType(WSocksStatusType.CONNECT);
                this.binanceWSocksClientInternal.setSocketStatus(WSocksStatusType.CONNECT);
                return;
            }
            webSocket.cancel();
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            if (webSocket.close(1000, "Confirm disconnect")) {
                if (webSocket.equals(this.binanceWSocksClientInternal.webSocket)) {
                    synchronized (BinanceWSocksClient.class) {
                        this.binanceWSocksClientInternal.webSocket = null;
                        this.binanceWSocksClientInternal.webSocketStatus.setCurrentType(WSocksStatusType.DISCONNECT);
                    }
                }
                this.binanceWSocksClientInternal.setSocketStatus(WSocksStatusType.RECONNECT);
                this.binanceWSocksClientInternal.connect("");
                return;
            }
            this.binanceWSocksClientInternal.webSocketStatus.setCurrentType(WSocksStatusType.DISCONNECT);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            if (this.binanceWSocksClientInternal.webSocket == null) {
                this.binanceWSocksClientInternal.webSocketStatus.setCurrentType(WSocksStatusType.DISCONNECT);
            }
            if (this.binanceWSocksClientInternal.webSocket == null || !webSocket.equals(this.binanceWSocksClientInternal.webSocket)) {
                webSocket.cancel();
                return;
            }
            webSocket.cancel();
            synchronized (BinanceWSocksClient.class) {
                this.binanceWSocksClientInternal.webSocket = null;
                this.binanceWSocksClientInternal.webSocketStatus.setCurrentType(WSocksStatusType.DISCONNECT);
                this.binanceWSocksClientInternal.setSocketStatus(WSocksStatusType.DISCONNECT);
            }

        }

        @Override
        public void parseMessage(WebSocket webSocket, String message) {

            if (TextUtils.isEmpty(message))
                    return;

            if (this.binanceWSocksClientInternal.wSocksHandler != null) {
                this.binanceWSocksClientInternal.messageCount++;
                this.binanceWSocksClientInternal.wSocksHandler.routeResponse(
                        this.binanceWSocksClientInternal.hashKey, message
                );
            }
        }

        public void processFailure(WebSocket webSocket, Boolean reconnect) {
            if (this.binanceWSocksClientInternal.webSocket == null) {
                this.binanceWSocksClientInternal.setSocketStatus(WSocksStatusType.DISCONNECT, reconnect);
            }
            if (this.binanceWSocksClientInternal.webSocket == null || !webSocket.equals(this.binanceWSocksClientInternal.webSocket)) {
                webSocket.cancel();
                return;
            }
            synchronized (BinanceWSocksClient.class) {
                this.binanceWSocksClientInternal.webSocket = null;
                this.binanceWSocksClientInternal.setSocketStatus(WSocksStatusType.DISCONNECT, reconnect);
                //this.binanceWSocksClientInternal.m23455i();
                webSocket.cancel();
                if (reconnect) {
                    this.binanceWSocksClientInternal.setSocketStatus(WSocksStatusType.RECONNECT);
                    this.binanceWSocksClientInternal.connect("");
                    //this.wsSingletoneHolder.connect("");
                } else {
                    this.binanceWSocksClientInternal.setSocketStatus(WSocksStatusType.ERROR);
                }
            }
        }
    }

    public static BinanceWSocksClient create(Context context, IWSocksHandler handler, String url, String hashKey) {
        BinanceWSocksClient tmp = binanceWSocksClient;
        if (tmp == null) {
            synchronized (BinanceWSocksClient.class) {
                tmp = binanceWSocksClient;
                if (tmp == null) {
                    tmp = new BinanceWSocksClient(context, handler, url, hashKey);
                    binanceWSocksClient = tmp;
                }
            }
        }

        //tmp.setUrl(url);

        return tmp;
    }

    public static BinanceWSocksClient create(Context context, IWSocksHandler handler, String url,
                                                String hashKey, BinanceWSocksClient oldClient) {
        BinanceWSocksClient tmp = oldClient;
        if (tmp == null) {
            synchronized (BinanceWSocksClient.class) {
                tmp = oldClient;
                if (tmp == null) {
                    tmp = new BinanceWSocksClient(context, handler, url, hashKey);
                    //oldClient = tmp;
                }
            }
        }

        //tmp.setUrl(url);

        return tmp;
    }

    private BinanceWSocksClient(Context context, IWSocksHandler handler, String url, String hashKey)
    {
        this.context = context;
        this.wSocksHandler = handler;
        this.wSocksUrl = url;
        this.hashKey = hashKey;
        this.messageCount = 0;
    }

    private void setUrl(String url)
    {
        this.wSocksUrl = url;
    }

    public boolean connect(String cmdToSend) {

        Logs.e(TAG, "connecting websock... instance=" + this.toString());
        Logs.e(TAG, "websock URL=" + wSocksUrl);

        okhttp3.Request.Builder b =
                new okhttp3.Request.Builder().url(wSocksUrl)
                        .addHeader("Accept-Encoding", "gzip")
                        .addHeader("Accept", "utf-8")
                        .addHeader("APIver", "v2");


        (webSocketConnectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (BinanceWSocksClient.class) {
                    if (BinanceWSocksClient.this.webSocket != null) {
                        WebSocket agVar = BinanceWSocksClient.this.webSocket;
                        BinanceWSocksClient.this.webSocket = null;
                        BinanceWSocksClient.this.webSocketStatus.setCurrentType(WSocksStatusType.DISCONNECT);
                        agVar.cancel();
                    }

                    BinanceWSocksClient.this.webSocketStatus.setCurrentType(WSocksStatusType.RECONNECT);

//                    if (cmdToSend != null && cmdToSend.length() > 0
//                            && WebSocketSingletoneBtx.this.webSocketCallBackDemo instanceof C403312)
//                        ((C403312)WebSocketSingletoneBtx.this.webSocketCallBackDemo).setCmdToRunOnConnect(cmdToSend);

                    BinanceWSocksClient.this.webSocket
                            = okHttpClient.newWebSocket(b.build(), BinanceWSocksClient.this.webSocketCallBackInner);

//                    BinanceWSocksClient.this.webSocket
//                            = !Parameters.DEBUG_STUB_WS
//                            ? okHttpClient.newWebSocket(b.build(), BinanceWSocksClient.this.webSocketCallBackInner)
//                            : TestingWebSocket.makeWebSocket("demo", BinanceWSocksClient.this.webSocketCallBackReal);
//

                }
            }
        })).start();
        return true;
    }

    public void disconnect(String hash) {
        if (this.wSocksHandler != null) {
            this.wSocksHandler = null;
        }


        if (this.webSocket != null) {
            WebSocket tmp = this.webSocket;
            synchronized (BinanceWSocksClient.class) {
                this.webSocket = null;
                this.webSocketStatus.setCurrentType(WSocksStatusType.DISCONNECT);
            }
            tmp.close(1000, "in need for disconnect");
            try {
                okHttpClient.dispatcher().cancelAll();
            } catch (Exception ex)
            {
                Logs.e(TAG, ex.toString());
            }
        }

        Logs.e(TAG, "disconnecting websock... instance=" + this.toString());

        if (webSocketConnectionThread != null)
            webSocketConnectionThread.interrupt();
        webSocketConnectionThread = null;
    }

    private boolean checkHandler() {
        if (this.wSocksHandler != null) {
            return true;
        }
        disconnect("");
        return false;
    }



    private void setSocketStatus(WSocksStatusType statusType, boolean reconnect) {
        if (checkHandler()) {
            if (wSocksHandler != null) {
                if (statusType.equals(WSocksStatusType.CONNECT))
                {
                    Logs.e(TAG, "client connect...");
                    this.messageCount = 0;
                    //wSocksHandler.mo2910u();
                }

                if (statusType.equals(WSocksStatusType.RECONNECT))
                {
                    Logs.e(TAG, "client reconnect...");
                    //wSocksHandler.mo2911v();
                }

                if (statusType.equals(WSocksStatusType.DISCONNECT) && !reconnect)
                {
                    Logs.e(TAG, "client disconnect...");
                    wSocksHandler.onClientDisconnect(hashKey);
                    wSocksHandler = null;
                }

                if (statusType.equals(WSocksStatusType.ERROR))
                {
                    Logs.e(TAG, "client error...");
                    wSocksHandler.onClientError(hashKey);
                    wSocksHandler = null;
                }
            }
        }
    }

    private void setSocketStatus(int statusType) {
        setSocketStatus(WSocksStatusType.getType(statusType), false);
    }

    private void setSocketStatus(int statusType, boolean reconnect) {
        setSocketStatus(WSocksStatusType.getType(statusType), reconnect);
    }

    public String getHashKey() {
        return hashKey;
    }

    public int getMessageCount() {
        return messageCount;
    }

}
