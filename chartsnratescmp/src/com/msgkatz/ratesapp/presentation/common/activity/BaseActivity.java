package com.msgkatz.ratesapp.presentation.common.activity;


import android.os.Bundle;
import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by msgkatz on 15/08/2018.
 */

public class BaseActivity extends BaseLayoutDummyActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public void setFullscreenMode(boolean isFullscreen) {

    }
}
