package com.msgkatz.ratesapp.presentation.ui.main.base;

import android.os.Bundle;

import com.msgkatz.ratesapp.presentation.common.fragment.BaseFragment;
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.ui.main.MainActivity;

/**
 * Created by msgkatz on 10/09/2018.
 */

public abstract class BaseMainFragment extends BaseFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainRouter mainRouter = (MainRouter) getActivity();
        //noinspection unchecked
        getPresenter().setRouter(mainRouter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getPresenter().setRouter(null);
    }

}
