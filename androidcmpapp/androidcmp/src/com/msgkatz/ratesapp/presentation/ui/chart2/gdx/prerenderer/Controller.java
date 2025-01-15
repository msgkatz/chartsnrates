package com.msgkatz.ratesapp.presentation.ui.chart2.gdx.prerenderer;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.domain.entities.IntervalJava;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.ChartDataCallback;

import java.util.ArrayList;

/**
 * Created by msgkatz on 15/09/2018.
 */

public interface Controller {

    void renderCandle1(Candle candle);
    void renderCandle2(Candle candle);

    void renderCandlesInterim(ArrayList<Candle> arrayList, IntervalJava interval, String intervalTmp);
    void renderCandlesHistorical(ArrayList<Candle> arrayList, String str);

    void renderLoader(boolean z, String str);

    void setChartDataListener(ChartDataCallback chartDataListener);
}
