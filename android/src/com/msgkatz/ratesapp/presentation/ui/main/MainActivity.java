package com.msgkatz.ratesapp.presentation.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.databinding.ActivityMainBinding;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.domain.interactors.GetPriceHistory;
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssets;
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver;
import com.msgkatz.ratesapp.domain.interactors.params.PriceHistoryParams;
import com.msgkatz.ratesapp.presentation.common.Layout;
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer;
import com.msgkatz.ratesapp.presentation.common.activity.BaseActivity;
import com.msgkatz.ratesapp.presentation.common.fragment.BaseFragment;
import com.msgkatz.ratesapp.presentation.entities.TabItem;
import com.msgkatz.ratesapp.presentation.ui.chart.ChartActivity;
import com.msgkatz.ratesapp.presentation.ui.main.base.MainRouter;
import com.msgkatz.ratesapp.utils.CommonUtil;
import com.msgkatz.ratesapp.utils.Logs;
import com.msgkatz.ratesapp.utils.Parameters;
import com.msgkatz.ratesapp.utils.UtilsKt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.DaggerFragment;

/**
 * Created by msgkatz on 15/08/2018.
 */

@SuppressWarnings ("WeakerAccess")
//@Layout(id = R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainRouter, TabLayout.OnTabSelectedListener
{
    private final static String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    @Inject
    TabInfoStorer tabInfoStorer;

    private TabItem[] tabItems = new TabItem[4];
    private Map<String, Integer> tabPositions = new HashMap<>();
    private Map<String, Drawable> tabImages = new HashMap<>();

    private QuoteAssetParentViewImpl dummyView;

    private boolean isFirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initTabs();

        dummyView = new QuoteAssetParentViewImpl(this, getLifecycle());
    }

    @Override
    protected void onStart() {
        super.onStart();



        if (isFirst) {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (binding.bottomNavigation.getTabAt(0) != null) {
                                binding.bottomNavigation.getTabAt(0).select();
                                isFirst = false;
                            }
                        }
                    }, 100);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //updatePaddings2();

        //initQuoteAssets();

    }

    private void initTabs() {

        if (!tabInfoStorer.isInitialised())
            tabInfoStorer.initTabs();

        int idx = 0;
        for (TabItem item : tabInfoStorer.getItems())
        {
            if (idx == 0) {
                binding.bottomNavigation.addTab(binding.bottomNavigation.newTab()
                        .setIcon(item.iconDrawable)
                        //.setText(name)
                        .setTag(item.quoteAssetName), true);
            }
            else {
                binding.bottomNavigation.addTab(binding.bottomNavigation.newTab()
                        .setIcon(item.iconDrawable)
                        //.setText(name)
                        .setTag(item.quoteAssetName));
            }
            idx++;
        }

        binding.bottomNavigation.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.main_tabs_item_selected));
        binding.bottomNavigation.setSelectedTabIndicatorHeight(CommonUtil.dpToPx(1));
        binding.bottomNavigation.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        clearBackStack();
        //showQuoteAssetFragment(tab.getText().toString());
        showQuoteAssetFragment((String) tab.getTag());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            showQuoteAssetFragment((String) tab.getTag());

            binding.bottomNavigation.clearOnTabSelectedListeners();
            binding.bottomNavigation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    clearBackStack();
                    //showQuoteAssetFragment(tab.getText().toString());
                    showQuoteAssetFragment((String) tab.getTag());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }
    }

    @Override
    public void showChart(PriceSimple priceSimple) {
        Intent intent = new Intent(this, ChartActivity.class);
        if (priceSimple != null) {
            intent.putExtra(ChartActivity.KEY_TOOL_NAME, priceSimple.getTool().getName());
            intent.putExtra(ChartActivity.KEY_TOOL_PRICE, priceSimple.getPrice());
        }
        //startActivityForResult(intent, REQUEST_CODE);
        startActivity(intent);
    }

    private void showQuoteAssetFragment(String quoteAssetName)
    {
        addBackStack(QuoteAssetFragment.newInstance(quoteAssetName), false);
        //addBackStack(QuoteAssetFragmentNew.Companion.newInstance(quoteAssetName), false);
    }

    private void addBackStack(BaseFragment fragment, boolean isAddToBackStack)
    {
        addBackStack(fragment, isAddToBackStack, false);
    }

    private void addBackStack(BaseFragment fragment, boolean isAddToBackStack, boolean justShow) {
        if (fragment == null)
            return;

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        //tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        tx.setCustomAnimations(R.anim.fade_in, 0, R.anim.fade_in, 0);

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

    private void updatePaddings() {
        if (1==1) return;
        boolean isHidden = UtilsKt.isSystemUiVisible(getWindow());
        if (!isHidden) return;
        if (Build.VERSION.SDK_INT <= 18) {
            UtilsKt.setSystemUiHidden(!isHidden, this);
        } else {
            UtilsKt.setSystemUiHiddenSticky(!isHidden, this);
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
