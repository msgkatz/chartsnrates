package com.msgkatz.ratesapp.presentation.ui.main.base;

import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.common.mvp.BaseView;

/**
 * Created by msgkatz on 10/09/2018.
 */

public abstract class BaseMainPresenter<vView extends BaseView>
        extends BasePresenter<vView, MainRouter> {
}
