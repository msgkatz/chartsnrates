package com.msgkatz.ratesapp.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.databinding.ActivitySplashBinding
import com.msgkatz.ratesapp.domain.entities.PlatformInfo
import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.presentation.common.activity.BaseActivity
import com.msgkatz.ratesapp.presentation.ui.main.MainActivity
import com.msgkatz.ratesapp.utils.Logs
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    companion object {
        val MAX_COUNT = 1
    }


    private lateinit var binding: ActivitySplashBinding

    @Inject
    public lateinit var mGetAssets: GetAssets

    @Inject
    public lateinit var mGetPlatformInfo: GetPlatformInfo

    private val mHandler = Handler()
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AnimatedActivity)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reconnect.setOnClickListener(View.OnClickListener {
            binding.reconnect.setVisibility(View.GONE)
            counter = 0
            loadAssets()
        })

        counter = 0
        loadAssets()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun loadAssets() {
        mGetAssets!!.execute(object :
            ResponseObserver<Optional<Map<String?, Asset?>?>?, Map<String?, Asset?>?>() {
            override fun doNext(stringAssetMap: Map<String?, Asset?>?) {
                if (stringAssetMap != null) mHandler.post {
                    counter = 0
                    loadPlatformInfo()
                }
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)

                counter++
                if (counter < SplashActivity.MAX_COUNT) mHandler.postDelayed(
                    { loadAssets() }, 50
                )
                else {
                    Logs.e(this@SplashActivity, counter.toString() + " " + exception.message)
                    initErrorMessage()
                }
            }


        }, null)
    }

    /** step Two  */
    private fun loadPlatformInfo() {
        mGetPlatformInfo!!.execute(object :
            ResponseObserver<Optional<PlatformInfo?>?, PlatformInfo?>() {
            override fun doNext(platformInfo: PlatformInfo?) {
                if (platformInfo != null) mHandler.post { initUI() }
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)

                counter++
                if (counter < SplashActivity.MAX_COUNT) mHandler.postDelayed(
                    { loadPlatformInfo() }, 50
                )
                else {
                    Logs.e(this@SplashActivity, counter.toString() + " " + exception.message)
                    initErrorMessage()
                }
            }
        }, null)
    }

    private fun initErrorMessage() {
        binding.reconnect.setVisibility(View.VISIBLE)
        binding.reconnect.setAlpha(0.0f)
        binding.reconnect.animate().alpha(1.0f)
    }

    private fun initUI() {
        showHomeActivity()
        finish()
    }

    private fun showHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}