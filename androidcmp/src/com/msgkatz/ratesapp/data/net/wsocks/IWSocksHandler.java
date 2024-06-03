package com.msgkatz.ratesapp.data.net.wsocks;

/**
 * Created by msgkatz on 03/08/2018.
 */

public interface IWSocksHandler {

    void routeResponse(String hashKey, String response);

    void onClientDisconnect(String hashKey);

    void onClientError(String hashKey);



}
