package com.msgkatz.ratesapp.feature.chartgdx.base;

import com.msgkatz.ratesapp.domain.entities.IntervalJava;


/**
 * Useless class - created only to fool dagger 2.18
 * otherwise (w/o this class) dagger curses on cycling presenters
 */
public abstract class BaseChartParentPresenter { //extends BaseChartPresenter<ChartParentView> {

    public abstract void provideNewInterval(IntervalJava interval);
}
