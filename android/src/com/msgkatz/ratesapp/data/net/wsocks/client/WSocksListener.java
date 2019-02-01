package com.msgkatz.ratesapp.data.net.wsocks.client;

import android.util.Log;

import com.msgkatz.ratesapp.utils.Logs;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by msgkatz on 02/08/2018.
 */

public abstract class WSocksListener extends WebSocketListener {

    /**
     * Error lists
     */
    List<String> stopList = Arrays.asList("SSLHandshakeException","UnknownHostException");
    List<String> reconnectList = Arrays.asList("EOFException","ConnectException"
            ,"IOException", "SocketTimeoutException"
            , "SocketException","ProtocolException"
            ,"SSLException");

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {

        if (t == null) {
            Logs.e("Sock err:", (new Exception("OkHttp WebSocket error: Throwable is null.")).toString());
            return;
        }

        String simpleName = "";
        try {
            simpleName = t.getClass().getSimpleName();
        } catch (Exception ex)
        {
            //Logs.e(TAG, ex.toString());
        }

        if (simpleName == null)
        {
            processFailure(webSocket, Boolean.TRUE);
            return;
        }
        else
        {
            if (this.reconnectList.contains(simpleName)) {
                processFailure(webSocket, Boolean.TRUE);
                return;
            }
            if (this.stopList.contains(simpleName)) {
                processFailure(webSocket, Boolean.FALSE);
                return;
            }

            processFailure(webSocket, null);


        }
    }



    @Override
    public void onMessage(WebSocket webSocket, String text) {
        parseMessage(webSocket, text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        parseMessage(webSocket, bytes.toString());
    }

    public abstract void parseMessage(WebSocket webSocket, String message);

    public abstract void processFailure(WebSocket webSocket, Boolean reconnect);
}
