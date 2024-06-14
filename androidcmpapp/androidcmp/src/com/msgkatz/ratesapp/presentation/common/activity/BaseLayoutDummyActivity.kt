package com.msgkatz.ratesapp.presentation.common.activity

import android.os.Bundle
import com.msgkatz.ratesapp.presentation.common.activity.rotation.BaseRotationActivity
import com.msgkatz.ratesapp.presentation.common.activity.rotation.behavior.BaseOrientationBehaviour
import com.msgkatz.ratesapp.presentation.common.activity.rotation.behavior.DefaultOrientationBehaviour
import com.msgkatz.ratesapp.presentation.common.activity.rotation.behavior.TabletOrientationBehaviour
import com.msgkatz.ratesapp.utils.Logs
import com.msgkatz.ratesapp.utils.isTablet

public abstract class BaseLayoutDummyActivity : BaseRotationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Logs.d("onCreate %s", this.javaClass.simpleName)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        Logs.d("onDestroy %s", this.javaClass.simpleName)
        super.onDestroy()
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