package com.msgkatz.ratesapp.presentation.ui.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.databinding.ActivityChartBinding;
import com.msgkatz.ratesapp.presentation.common.Layout;
import com.msgkatz.ratesapp.presentation.common.activity.BaseActivity;
import com.msgkatz.ratesapp.presentation.common.fragment.BaseFragment;
import com.msgkatz.ratesapp.presentation.entities.ToolFormat;
import com.msgkatz.ratesapp.presentation.ui.chart.base.ChartRouter;
import com.msgkatz.ratesapp.utils.UtilsKt;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * Created by msgkatz on 09/09/2018.
 */

@SuppressWarnings ("WeakerAccess")
//@Layout(id = R.layout.activity_chart)
public class ChartActivity extends BaseActivity implements ChartRouter, AndroidFragmentApplication.Callbacks
{
    private final static String TAG = ChartActivity.class.getSimpleName();

    public static final String KEY_TOOL_NAME = "com.msgkatz.ratesapp.tool.name";
    public static final String KEY_TOOL_PRICE = "com.msgkatz.ratesapp.tool.price";

    private ActivityChartBinding binding;

//    @BindView(R.id.image1) ImageView mImage1;
//    @BindView(R.id.image2) ImageView mImage2;
//    @BindView(R.id.image3) ImageView mImage3;
    private String mToolName;
    private double mToolPrice;

    private PowerManager.WakeLock wakeLock;

    private BaseFragment chartParentFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /**
         * Flags keeps screen On, but better to use wakelock (somewhere in bg)
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final String toolName = getIntent().getStringExtra(KEY_TOOL_NAME);
        double toolPrice = getIntent().getDoubleExtra(KEY_TOOL_PRICE, 0);
        //postponeEnterTransition();

        PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
        if (mgr != null) {
            wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ":ChartsnRatesWakeLock");

        }


        initScreen(toolName, toolPrice);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleOrientationChange();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //updatePaddings2();

        if (wakeLock != null)
            wakeLock.acquire();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (wakeLock != null)
            wakeLock.release();
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleOrientationChange();
    }

    private void handleOrientationChange() {
        boolean islandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (chartParentFragment != null) {
            chartParentFragment.setConfigurationChange(islandscape);
        }
    }

    private void initScreen(String toolName, double toolPrice)
    {
        //add fragment
        addBackStack(ChartParentFragment.newInstance(toolName, toolPrice), false);
    }

    private void addBackStack(BaseFragment fragment, boolean isAddToBackStack)
    {
        chartParentFragment = fragment;
        addBackStack(fragment, isAddToBackStack, false);
    }

    private void addBackStack(BaseFragment fragment, boolean isAddToBackStack, boolean justShow) {
        if (fragment == null)
            return;

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

        if (justShow)
        {
            tx.show(fragment);
        } else {
            tx.replace(R.id.content, fragment, fragment.getFragmentName());
        }

        if (isAddToBackStack) {
            tx.addToBackStack(fragment.getFragmentName());
        }
        tx.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void removeExtras() {
        if (binding.image1 != null && (((ViewManager)binding.image1.getParent()) != null))
            ((ViewManager)binding.image1.getParent()).removeView(binding.image1);
        if (binding.image2 != null && (((ViewManager)binding.image2.getParent()) != null))
            ((ViewManager)binding.image2.getParent()).removeView(binding.image2);
        if (binding.image3 != null && (((ViewManager)binding.image3.getParent()) != null))
            ((ViewManager)binding.image3.getParent()).removeView(binding.image3);
    }

    @Override
    public void showMain() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void exit() {

    }

    @Override
    protected void onDestroy() {
        chartParentFragment = null;
        super.onDestroy();
    }


    private void updatePaddings() {
        boolean isHidden = UtilsKt.isSystemUiVisible(getWindow());
        if (Build.VERSION.SDK_INT <= 18) {
            UtilsKt.setSystemUiHidden(true, this);
        } else {
            UtilsKt.setSystemUiHiddenSticky(true, this);
        }
    }

    private void updatePaddings2() {
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
