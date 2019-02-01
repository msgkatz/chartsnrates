package com.msgkatz.ratesapp.presentation.ui.main.base;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import com.msgkatz.ratesapp.presentation.common.fragment.BaseDummyFragment;

import java.lang.ref.WeakReference;

/**
 * Created by msgkatz on 11/09/2018.
 */

public abstract class BaseMainDummyFragment extends BaseDummyFragment {

    public BaseMainDummyFragment(Context context, Lifecycle lifecycle)
    {
        super(context, lifecycle);
    }
}
