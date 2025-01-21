package com.msgkatz.ratesapp.presentation.ui.main.base;

import android.os.Bundle;

import com.msgkatz.ratesapp.feature.common.fragment.BaseFragment;

//import com.msgkatz.ratesapp.presentation.common.fragment.BaseFragment;

/**
 * Created by msgkatz on 10/09/2018.
 */

public abstract class BaseMainFragment extends BaseFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainRouter mainRouter = (MainRouter) getActivity();
        //noinspection unchecked
        if (getPresenter() != null)
            getPresenter().setRouter(mainRouter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getPresenter() != null)
            getPresenter().setRouter(null);
    }

}
