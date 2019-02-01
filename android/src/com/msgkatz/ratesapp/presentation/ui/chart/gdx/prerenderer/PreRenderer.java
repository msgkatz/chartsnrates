package com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.presentation.entities.CandleData;
import com.msgkatz.ratesapp.presentation.entities.ChartType;
import com.msgkatz.ratesapp.presentation.entities.NewVerticalData;
import com.msgkatz.ratesapp.presentation.entities.ToolFormat;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.ChartDataCallback;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.StageControllerListener;
import com.msgkatz.ratesapp.utils.Logs;
import com.msgkatz.ratesapp.utils.TimeUtil;
import com.msgkatz.ratesapp.utils.gdx.GdxSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class PreRenderer implements Controller {

    private static final String TAG = PreRenderer.class.getSimpleName();

    private ChartDataCallback chartDataListener;
    private StageControllerListener stageControllerListener;
    private ToolFormat mToolFormat;
    private ChartType chartType = ChartType.Curve;

    private Array<CandleData> candleDataArray;
    private Array<NewVerticalData> newVerticalDataArray = new Array();

    private Calendar calendar = Calendar.getInstance();
    private StringBuilder stringBuilder = new StringBuilder();


    private Interval currentInterval;
    private int chartTimeFrame;
    private int chartTimeFrameInSecs = 60;

    private long minTime;


    public void reInitChart()
    {
        if (this.candleDataArray != null && this.candleDataArray.size > 0) {
            initData();
            initVerticals();
        }
    }

    private void initData()
    {
        for (int i = 0; i < this.candleDataArray.size; i++) {
            ((CandleData) this.candleDataArray.get(i))
                    .setIdxByScaledDensity((double) (((float) ((CandleData) this.candleDataArray.get(i)).getIdxByTime()) * GdxSettings.chartBlockWidthDensityEffective));
        }
        if (this.stageControllerListener != null) {
            this.stageControllerListener.processDataArray(this.candleDataArray, false);
        }
    }

    private void initVerticals()
    {
        for (int i = 0; i < this.newVerticalDataArray.size; i++) {
            ((NewVerticalData) this.newVerticalDataArray.get(i)).updateX();
        }
    }
    
    // region Rendering control

    @Override
    public void renderCandle1(Candle candle) {
        if (Gdx.app != null) {
            Gdx.app.postRunnable(new Runnable() {
                final PreRenderer _graphicController = PreRenderer.this;

                public void run() {
                    if (this._graphicController.candleDataArray != null && this._graphicController.candleDataArray.size > 0) {
                        float d = (float) ((CandleData) this._graphicController.candleDataArray.peek()).getPriceClose();
                        CandleData candleData = new CandleData(candle, ((CandleData) this._graphicController.candleDataArray.peek()).getIdxByTime() + 1);
                        candleData.setIdxByScaledDensity((double) (GdxSettings.chartBlockWidthDensityEffective * ((float) candleData.getIdxByTime())));
                        this._graphicController.checkForVertical((CandleData) candleData);
                        this._graphicController.candleDataArray.add(candleData);
                        if (this._graphicController.stageControllerListener != null) {
                            this._graphicController.stageControllerListener
                                    .processDataItem((CandleData) this._graphicController.candleDataArray.peek(),
                                                (float) ((CandleData) this._graphicController.candleDataArray.first()).getIdxByScaledDensity(),
                                                (float) ((CandleData) this._graphicController.candleDataArray.peek()).getIdxByScaledDensity(),
                                                d);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void renderCandle2(Candle candle) {
        if (Gdx.app != null) {
            Gdx.app.postRunnable(new Runnable() {
                final PreRenderer _graphicController = PreRenderer.this;

                public void run() {
                    if (candle != null && this._graphicController.candleDataArray != null && this._graphicController.candleDataArray.size > 0) {
                        float d = (float) ((CandleData) this._graphicController.candleDataArray.peek()).getPriceClose();
                        ((CandleData) this._graphicController.candleDataArray.peek()).merge(candle);
                        if (this._graphicController.stageControllerListener != null) {
                            this._graphicController.stageControllerListener.processDataItemMerged((CandleData) this._graphicController.candleDataArray.peek(), d);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void renderCandlesInterim(ArrayList<Candle> arrayList, Interval interval, String intervalTmp)
    {
        Logs.e(TAG, "renderCandlesInterim posting candleList to gdx, array size="
                + (arrayList==null
                ?"null"
                :(arrayList.size() + ", first=" + arrayList.get(0).getTime() + ", last=" + arrayList.get(arrayList.size() - 1).getTime())
        ));

        if (Gdx.app != null) {
            Gdx.app.postRunnable(new Runnable() {
                final PreRenderer _graphicController = PreRenderer.this;

                public void run() {
                    if (this._graphicController.candleDataArray == null) {
                        this._graphicController.candleDataArray = new Array();
                    } else {
                        this._graphicController.candleDataArray.clear();
                    }
                    if (this._graphicController.newVerticalDataArray == null) {
                        this._graphicController.newVerticalDataArray = new Array();
                    } else {
                        this._graphicController.newVerticalDataArray.clear();
                    }

                    this._graphicController.currentInterval = interval;

                    GdxSettings.init(this._graphicController.chartType, 1.0f, this._graphicController.getExtraChartCount());
                    for (int i = 0; i < arrayList.size(); i++) {

                        CandleData candleData = new CandleData((Candle) arrayList.get(i), i);
                        candleData.setIdxByScaledDensity((double) (GdxSettings.chartBlockWidthDensityEffective * ((float) candleData.getIdxByTime())));

                        this._graphicController.checkForVertical((CandleData) candleData);
                        this._graphicController.candleDataArray.add(candleData);
                    }
                    this._graphicController.minTime = ((CandleData) this._graphicController.candleDataArray.first()).getTime();
                    if (this._graphicController.stageControllerListener != null) {
                        this._graphicController.stageControllerListener.setChartType(this._graphicController.chartType);
                    }
                    if (this._graphicController.stageControllerListener != null) {
                        this._graphicController.stageControllerListener.processDataArray(this._graphicController.candleDataArray, true);
                    }
                }
            });
        }
    }

    @Override
    public void renderCandlesHistorical(ArrayList<Candle> arrayList, String str) {

        Logs.e(TAG, "renderCandlesHistorical posting historical candleList to gdx, array size="
                + (arrayList==null
                ?"null"
                :(arrayList.size() + ", first=" + arrayList.get(0).getTime() + ", last=" + arrayList.get(arrayList.size() - 1).getTime())
        ));

        if (this.candleDataArray != null) {
            Array tmpArray1 = new Array();
            for (int i = 0; i < arrayList.size(); i++) {
                CandleData candleData = new CandleData((Candle) arrayList.get(i), ((CandleData) this.candleDataArray.first()).getIdxByTime() - (arrayList.size() - i));
                candleData.setIdxByScaledDensity((double) (GdxSettings.chartBlockWidthDensityEffective * ((float) candleData.getIdxByTime())));
                checkForVertical((CandleData) candleData);
                tmpArray1.add(candleData);
            }

            Array tmpArray2 = new Array();
            tmpArray2.addAll(this.candleDataArray);
            final Array tmpArrayTotal = new Array();
            tmpArrayTotal.addAll(tmpArray1);
            tmpArrayTotal.addAll(tmpArray2);
            Gdx.app.postRunnable(new Runnable() {
                final PreRenderer _graphicController = PreRenderer.this;

                public void run() {
                    this._graphicController.candleDataArray = tmpArrayTotal;
                    this._graphicController.minTime = ((CandleData) this._graphicController.candleDataArray.first()).getTime();
                    this._graphicController.newVerticalDataArray.clear();
                    for (int i = 0; i < this._graphicController.candleDataArray.size; i++) {
                        this._graphicController.checkForVertical((CandleData) this._graphicController.candleDataArray.get(i));
                    }
                    if (this._graphicController.stageControllerListener != null) {
                        this._graphicController.stageControllerListener.processDataHistoryArray(this._graphicController.candleDataArray, str);
                    }
                }
            });
        }
        else
        {
            Logs.e(TAG, "renderCandlesHistorical prev candleArray is null... So do nothing");
        }
    }

    @Override
    public void renderLoader(boolean z, String str) {
        Gdx.app.postRunnable(new Runnable() {
            final /* synthetic */ PreRenderer _graphicController = PreRenderer.this;

            public void run() {
                this._graphicController.stageControllerListener.initLoader(z, str);
            }
        });
    }
    
    // endregion


    // region Getters && Setters

    public void setStageControllerListener(StageControllerListener stageControllerListener)
    {
        this.stageControllerListener = stageControllerListener;
    }

    public void setChartDataListener(ChartDataCallback chartDataListener) {
        this.chartDataListener = chartDataListener;
    }

    public void setToolFormat(ToolFormat toolFormat) {
        this.mToolFormat = toolFormat;
    }

    public ToolFormat getToolFormat() {
        return mToolFormat;
    }

    public Array<CandleData> getCandleDataArray() {
        return candleDataArray;
    }

    public Array<NewVerticalData> getVerticalDataArray() {
        return newVerticalDataArray;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public int getExtraChartCount()
    {
        return 0;
    }

    // endregion

    // region Helper methods

    private long lastTime = Long.MIN_VALUE;
    private void checkForVertical(CandleData candleData) {

        long i = currentInterval.getPerBlockDefaultMs() / 1000;
        long tmpTimeInSecs = TimeUtil.normalizeInSeconds(currentInterval.getPerItemDefaultMs()/1000, candleData.getTime());

        /**
        int i = this.chartTimeFrame * 60;
        if (this.chartType == ChartType.Candle) {
            i = this.chartTimeFrame * 5;
            if (this.chartTimeFrame == 15) {
                i = this.chartTimeFrame * 8;
            }
        }
        long tmpTimeInSecs = TimeUtil.normalizeInSeconds(60, candleData.getTime());
         **/

        if (tmpTimeInSecs % ((long) i) == 0
                && tmpTimeInSecs != this.lastTime)
        {
            this.lastTime = tmpTimeInSecs;
            this.calendar.setTimeInMillis(tmpTimeInSecs * 1000);
            this.stringBuilder.delete(0, this.stringBuilder.length());

            if (this.calendar.get(Calendar.DATE) < 10) {
                this.stringBuilder.append("0");
            }
            this.stringBuilder.append(this.calendar.get(Calendar.DATE));
            this.stringBuilder.append("/");

            if (this.calendar.get(Calendar.MONTH) + 1 < 10) {
                this.stringBuilder.append("0");
            }
            this.stringBuilder.append(this.calendar.get(Calendar.MONTH) + 1);
            this.stringBuilder.append("\n");

            if (this.calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                this.stringBuilder.append(0);
            }
            this.stringBuilder.append(this.calendar.get(Calendar.HOUR_OF_DAY));
            this.stringBuilder.append(":");

            if (this.calendar.get(Calendar.MINUTE) < 10) {
                this.stringBuilder.append(0);
            }
            this.stringBuilder.append(this.calendar.get(Calendar.MINUTE));
            this.newVerticalDataArray.add(new NewVerticalData((float) candleData.getIdxByScaledDensity(), candleData.getTime(), this.stringBuilder.toString(), candleData.getIdxByTime()));
            // Logs.d(TAG, "newVerticalDataArray size = " + newVerticalDataArray.size);
        }
    }

    public void loadHistoryBlock() {
        long j = this.minTime;
        //this.minTime = (this.minTime - ((long) this.chartTimeFrame)) - ((long) (600 * this.chartTimeFrame));
        this.minTime = (this.minTime - currentInterval.getPerBlockDefaultMs()/1000);
        long j2 = this.minTime;
        UUID randomUUID = UUID.randomUUID();
        renderLoader(true, randomUUID.toString());
        this.chartDataListener.initPriceHistory(chartDataListener.getToolName(),
                //TimeIntervalUtil.getIntervalNameByValue(this.chartTimeFrame),
                currentInterval.getSymbol(),
                j2, j, randomUUID.toString());
    }



    // endregion
}
