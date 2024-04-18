package com.msgkatz.ratesapp.presentation.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.interactors.GetAssets;
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver;
import com.msgkatz.ratesapp.presentation.common.activity.BaseActivity;
import com.msgkatz.ratesapp.presentation.ui.main.MainActivity;
import com.msgkatz.ratesapp.utils.Logs;

import java.util.Map;

import javax.inject.Inject;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by msgkatz on 15/08/2018.
 */

@SuppressWarnings ("WeakerAccess")
//@Layout(id = R.layout.activity_splash)
public class SplashActivityOld extends BaseActivity {

    private final static int MAX_COUNT = 1;

    //@BindView(R.id.reconnect)
    ConstraintLayout btn_reconnect;

    @Inject
    GetAssets mGetAssets;

    @Inject
    GetPlatformInfo mGetPlatformInfo;

    private Handler mHandler = new Handler();
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AnimatedActivity);
        super.onCreate(savedInstanceState);

        btn_reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_reconnect.setVisibility(View.GONE);
                counter = 0;
                loadAssets();
            }
        });

        counter = 0;
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
                            counter = 0;
                            loadPlatformInfo();
                        }
                    });

            }

            @Override
            public void onError(Throwable exception) {
                super.onError(exception);

                counter++;
                if (counter < MAX_COUNT)
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadAssets();
                        }
                    }, 50);
                else {
                    Logs.e(SplashActivityOld.this, counter + " " + exception.getMessage());
                    initErrorMessage();
                }
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

            @Override
            public void onError(Throwable exception) {
                super.onError(exception);

                counter++;
                if (counter < MAX_COUNT)
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadPlatformInfo();
                        }
                    }, 50);
                else {
                    Logs.e(SplashActivityOld.this, counter + " " + exception.getMessage());
                    initErrorMessage();
                }

            }
        }, null);
    }

    private void initErrorMessage()
    {
        btn_reconnect.setVisibility(View.VISIBLE);
        btn_reconnect.setAlpha(0.0f);
        btn_reconnect.animate().alpha(1.0f);
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
