package com.msgkatz.ratesapp.presentation.ui.chart.gdx;

import com.badlogic.gdx.utils.Array;
import com.msgkatz.ratesapp.presentation.entities.CandleData;
import com.msgkatz.ratesapp.presentation.entities.ChartType;

/**
 * Created by msgkatz on 15/09/2018.
 */

public interface StageControllerListener {

    void setChartType(ChartType type);

    void processDataArray(Array<CandleData> _candleDataArray, boolean z);

    void processDataHistoryArray(Array<CandleData> _candleDataArray, String historicalBlockName);

    void processDataItemMerged(CandleData c3792b, float f);

    void processDataItem(CandleData c3792b, float f, float f2, float f3);

    void initLoader(boolean z, String str);
}
