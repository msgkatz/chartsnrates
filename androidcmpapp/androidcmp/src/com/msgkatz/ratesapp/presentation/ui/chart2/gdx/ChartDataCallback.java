package com.msgkatz.ratesapp.presentation.ui.chart2.gdx;

/**
 * Created by msgkatz on 26/09/2018.
 */

public interface ChartDataCallback {

    void initPriceHistory(String toolName, String interval,
                          Long startTime, Long endTime, String rangeName);
    String getToolName();
}
