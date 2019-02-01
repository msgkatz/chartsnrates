package com.msgkatz.ratesapp.presentation.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.interactors.GetAssets;
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver;
import com.msgkatz.ratesapp.presentation.common.activity.BaseActivity;
import com.msgkatz.ratesapp.presentation.ui.main.MainActivity;
import com.msgkatz.ratesapp.presentation.common.Layout;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by msgkatz on 15/08/2018.
 */

@SuppressWarnings ("WeakerAccess")
@Layout(id = R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Inject
    GetAssets mGetAssets;

    @Inject
    GetPlatformInfo mGetPlatformInfo;

    Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AnimatedActivity);
        super.onCreate(savedInstanceState);

        loadAssets();

    }

    /** step One **/
    private void loadAssets()
    {
        mGetAssets.execute(new ResponseObserver<Optional<Map<String,Asset>>, Map<String, Asset>>() {
            @Override
            public void doNext(Map<String, Asset> stringAssetMap) {
                if (stringAssetMap != null)
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadPlatformInfo();
                        }
                    });

            }
        }, null);
    }

    /** step Two **/
    private void loadPlatformInfo()
    {
        mGetPlatformInfo.execute(new ResponseObserver<Optional<PlatformInfo>, PlatformInfo>() {

            @Override
            public void doNext(PlatformInfo platformInfo) {
                if (platformInfo != null)
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            initUI();
                        }
                    });

            }
        }, null);
    }

    private void initUI()
    {
        showHomeActivity();
        finish();
    }

    private void showHomeActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
