package com.msgkatz.ratesapp.presentation.ui.chart.base;

import com.msgkatz.ratesapp.domain.entities.Interval;


/**
 * Useless class - created only to fool dagger 2.18
 * otherwise (w/o this class) dagger curses on cycling presenters
 */
public abstract class BaseChartParentPresenter { //extends BaseChartPresenter<ChartParentView> {

    public abstract void provideNewInterval(Interval interval);
}
