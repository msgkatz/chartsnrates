package com.msgkatz.ratesapp.presentation.ui.main.base;

import android.content.Context;
//import com.msgkatz.ratesapp.presentation.common.fragment.BaseDummyFragment;

import java.lang.ref.WeakReference;

import androidx.lifecycle.Lifecycle;

import com.msgkatz.ratesapp.feature.common.fragment.BaseDummyFragment;

/**
 * Created by msgkatz on 11/09/2018.
 */

public abstract class BaseMainDummyFragment extends BaseDummyFragment {

    public BaseMainDummyFragment(Context context, Lifecycle lifecycle)
    {
        super(context, lifecycle);
    }
}
