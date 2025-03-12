package com.msgkatz.ratesapp.feature.chartgdx.base.gdx;

import android.view.View;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badlogic.gdx.backends.android.DefaultAndroidInput;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by msgkatz on 09/09/2018.
 */

public class BaseGdxAppFragment extends AndroidFragmentApplication {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final InternalRunnable internalRunnable = new InternalRunnable(this);

    class InternalRunnable implements Runnable
    {
        final BaseGdxAppFragment base;

        public InternalRunnable(BaseGdxAppFragment base)
        {
            this.base = base;
        }

        @Override
        public void run() {

            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            this.base.graphics.onDrawFrame(null);
        }
    }

    @Override
    public void onPause() {

        this.executorService.submit(internalRunnable);
        super.onPause();
    }

    @Override
    public View initializeForView(ApplicationListener listener, AndroidApplicationConfiguration config) {

        View view = super.initializeForView(listener, config);
        this.input = new DefaultAndroidInput(this,
                getContext().getApplicationContext(), view, config);
//        this.input = AndroidInputFactory.newAndroidInput(this,
//                getContext().getApplicationContext(), view, config);
        return view;
    }
}
