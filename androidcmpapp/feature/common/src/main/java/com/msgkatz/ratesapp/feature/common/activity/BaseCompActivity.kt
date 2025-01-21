package com.msgkatz.ratesapp.feature.common.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.msgkatz.ratesapp.feature.common.activity.rotation.BaseRotationCompActivity
import com.msgkatz.ratesapp.feature.common.activity.rotation.behavior.BaseOrientationBehaviour
import com.msgkatz.ratesapp.feature.common.activity.rotation.behavior.DefaultOrientationBehaviour
import com.msgkatz.ratesapp.feature.common.activity.rotation.behavior.TabletOrientationBehaviour
import com.msgkatz.ratesapp.feature.common.utils.isTablet

import dagger.android.AndroidInjection
//import dagger.android.AndroidInjector
//import dagger.android.DispatchingAndroidInjector
//import dagger.android.support.HasSupportFragmentInjector
//import javax.inject.Inject

open class BaseCompActivity : BaseRotationCompActivity() /**, HasSupportFragmentInjector**/ {

//    @Inject
//    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    public override fun onCreate(savedInstanceState: Bundle?) {
        //AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

//    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
//        return fragmentDispatchingAndroidInjector
//    }

    override fun setFullscreenMode(isFullscreen: Boolean) {
    }

    override fun onCreateOrientationBehaviour(): BaseOrientationBehaviour {
        val isTablet = isTablet(this)
        return if (isTablet) {
            TabletOrientationBehaviour(this, this)
        } else {
            DefaultOrientationBehaviour(applicationContext, this)
        }
    }
}