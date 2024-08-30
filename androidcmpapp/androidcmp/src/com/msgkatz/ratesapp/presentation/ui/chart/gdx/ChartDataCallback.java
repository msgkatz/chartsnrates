package com.msgkatz.ratesapp.presentation.ui.chart.gdx;

/**
 * Created by msgkatz on 26/09/2018.
 */

public interface ChartDataCallback {

    void initPriceHistory(String toolName, String interval,
                          Long startTime, Long endTime, String rangeName);
    String getToolName();
}
