package com.msgkatz.ratesapp.feature.chartgdx.base;

import android.os.Bundle;

import com.msgkatz.ratesapp.feature.common.fragment.BaseFragment;


/**
 * Created by msgkatz on 14/09/2018.
 */

public abstract class BaseChartFragment extends BaseFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ChartRouter chartRouter = (ChartRouter) getActivity();
        //noinspection unchecked
        getPresenter().setRouter(chartRouter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getPresenter().setRouter(null);
    }

}
