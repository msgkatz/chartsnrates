package com.msgkatz.ratesapp.feature.chartgdx.base;


import com.msgkatz.ratesapp.feature.chartgdx.entities.ToolFormat;
import com.msgkatz.ratesapp.feature.chartgdx.gdx.prerenderer.Controller;

/**
 * Useless class - created only to fool dagger 2.18
 * otherwise (w/o this class) dagger curses on cycling presenters 
 */
public abstract class BaseChartGdxPresenter { //} extends BaseChartPresenter<ChartGdxView> implements ChartDataCallback {

    public abstract void setController(Controller controller);
    public abstract void setToolFormat(ToolFormat toolFormat);
    public abstract void setToolName(String toolName);
    public abstract void setInterval(String interval);

}
