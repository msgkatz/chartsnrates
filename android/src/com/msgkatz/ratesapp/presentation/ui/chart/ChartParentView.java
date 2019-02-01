package com.msgkatz.ratesapp.presentation.ui.chart;

import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.presentation.common.mvp.BaseView;

import java.util.List;

/**
 * Created by msgkatz on 14/09/2018.
 */

public interface ChartParentView extends BaseView {

    void updatePrice(double newPrice);
    void updateTitle(Tool tool);
    void updateIntervals(List<Interval> intervals);
}
