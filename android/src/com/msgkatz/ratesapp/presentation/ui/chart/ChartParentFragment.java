package com.msgkatz.ratesapp.presentation.ui.chart;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.presentation.common.Layout;
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.common.widget.EndOffsetItemDecoration;
import com.msgkatz.ratesapp.presentation.entities.ToolFormat;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartFragment;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartParentPresenter;
import com.msgkatz.ratesapp.presentation.ui.chart.widget.CurrentRateLayout;
import com.msgkatz.ratesapp.presentation.common.widget.StartOffsetItemDecoration;
import com.msgkatz.ratesapp.utils.CommonUtil;
import com.msgkatz.ratesapp.utils.Logs;
import com.msgkatz.ratesapp.utils.Parameters;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by msgkatz on 14/09/2018.
 */

@SuppressWarnings("WeakerAccess")
@Layout(id = R.layout.fragment_parent_chart)
public class ChartParentFragment extends BaseChartFragment implements ChartParentView {

    public static final String TAG = ChartParentFragment.class.getSimpleName();

    public static final String KEY_TOOL_NAME = "com.msgkatz.ratesapp.tool.name";
    public static final String KEY_TOOL_PRICE = "com.msgkatz.ratesapp.tool.price";

    @Inject
    BaseChartParentPresenter mChartParentPresenter;

    @BindView(R.id.constraintLayout) View rootView;
    @BindView(R.id.imageView) ImageView mBackButton;
    @BindView(R.id.title_main) TextView mTitleMain;
    @BindView(R.id.title_main2) TextView mTitleMain2;
    @BindView(R.id.title_2nd) TextView mTitle2nd;
    @BindView(R.id.rate) CurrentRateLayout mRate;
    @BindView(R.id.rate2) CurrentRateLayout mRate2;
    @BindView(R.id.rv_interval_list)
    RecyclerView mRecyclerView;

    private ChartIntervalsAdapter mAdapter;
    private Handler mHandler = new Handler();

    private String mToolName;
    private double mToolPrice;
    private String mInterval = Parameters.defaulScaletList.get(2).getValue();

    public static ChartParentFragment newInstance(String toolName, double toolPrice)
    {
        Bundle args = new Bundle();
        args.putString(KEY_TOOL_NAME, toolName);
        args.putDouble(KEY_TOOL_PRICE, toolPrice);

        ChartParentFragment fragment = new ChartParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mToolName = getArguments().getString(KEY_TOOL_NAME);
            mToolPrice = getArguments().getDouble(KEY_TOOL_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChartParentPresenter)getPresenter()).getRouter().showMain();
            }
        });

        setupRecycler();

        mTitleMain.setText(mToolName);
        mRate.setRate(mToolPrice);
        mRate2.setRate(mToolPrice);
        mTitle2nd.setVisibility(View.INVISIBLE);
        ((ChartParentPresenter)getPresenter()).provideToolName(mToolName);

        modifyBackButtonHitArea();

//        final ViewTreeObserver observer = rootView.getViewTreeObserver();
//        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                rootView.getViewTreeObserver().removeOnPreDrawListener(this);
//                //getActivity().startPostponedEnterTransition();
//                return true;
//            }
//        });

//        if (view.getViewTreeObserver() != null) {
//            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    view.getViewTreeObserver().removeOnPreDrawListener(this);
//                    initGdxFragment();
//                    return true;
//                }
//            });
//        }
//        else
//        {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    initGdxFragment();
//                }
//            }, 500);
//        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGdxFragment();

//        getActivity().startPostponedEnterTransition();
//        mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    initGdxFragment();
//                }
//            }, 500);

    }

    @Override
    public void setConfigurationChange(boolean isLandscape) {
        Log.e(TAG, "setConfigurationChange isLandscape=" + isLandscape);
        int height = 0;
        if (isLandscape) {
            mRate2.setVisibility(View.VISIBLE);
            mRate.setVisibility(View.GONE);
            height = CommonUtil.dpToPx(50);
        } else {
            mRate.setVisibility(View.VISIBLE);
            mRate2.setVisibility(View.GONE);
            height = CommonUtil.dpToPx(100);
        }

        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
        lp.height = height;
        mRecyclerView.setLayoutParams(lp);
    }

    private void modifyBackButtonHitArea()
    {
        final int extraArea = CommonUtil.dpToPx(35);
        final View parent = (View) mBackButton.getParent();  // button: the view you want to enlarge hit area
        parent.post( new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                mBackButton.getHitRect(rect);
                rect.top -= extraArea;    // increase top hit area
                rect.left -= extraArea;   // increase left hit area
                rect.bottom += extraArea; // increase bottom hit area
                rect.right += extraArea;  // increase right hit area
                parent.setTouchDelegate( new TouchDelegate( rect , mBackButton));
            }
        });
    }

    private void setupRecycler()
    {
        mAdapter = new ChartIntervalsAdapter(this.getContext(), mInterval);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);

        //ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        mAdapter.setOnItemClickListener(new ChartIntervalsAdapter.OnItemClickListener() {

            @Override
            public void onItemClicked(Interval interval) {

                if (interval == null)
                    return;

                mInterval = interval.getSymbol();
                if (mChartParentPresenter != null)
                    mChartParentPresenter.provideNewInterval(interval);
            }
        });

        StartOffsetItemDecoration itemDecorationStart = new StartOffsetItemDecoration(CommonUtil.dpToPx(10));
        EndOffsetItemDecoration itemDecorationEnd = new EndOffsetItemDecoration(CommonUtil.dpToPx(10));
        mRecyclerView.addItemDecoration(itemDecorationStart);
        mRecyclerView.addItemDecoration(itemDecorationEnd);
    }

    @Override
    public void updatePrice(double newPrice) {
        mRate.setRate(newPrice);
        mRate2.setRate(newPrice);
    }

    @Override
    public void updateTitle(Tool tool) {
        mTitleMain.setText(tool.getBaseAsset().getNameShort());
        String quote = String.format(Locale.getDefault(), "/%1$s", tool.getQuoteAsset().getNameShort());
        mTitleMain2.setText(quote);
        mTitle2nd.setText(tool.getBaseAsset().getNameLong());
        mTitle2nd.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateIntervals(List<Interval> intervals) {
        mAdapter.setData(intervals);
    }

    private void initGdxFragment()
    {
        ToolFormat toolFormat = new ToolFormat(8, mToolPrice);

//        mChartGdxPresenter.setChartParentPresenter(mChartParentPresenter);
//        mChartGdxPresenter.setToolFormat(toolFormat);
//        mChartGdxPresenter.setToolName(mToolName);
//        mChartGdxPresenter.setInterval(mInterval);

        Logs.e(this, "initGdxFragment() started...");

        if (((ChartGdxFragment) getChildFragmentManager().findFragmentByTag("chartGdxFragment")) == null) {

            Logs.e(this, "initGdxFragment() chartGdxFragment == null, so creating new one");
            replaceGraphicFragment();

        }
        else {
            Logs.e(this, "initGdxFragment() chartGdxFragment != null, so we'll make other steps");

            ChartGdxFragment fr = ((ChartGdxFragment) getChildFragmentManager().findFragmentByTag("chartGdxFragment"));

            if (fr != null)
                fr.updateToolInfo(mToolName, toolFormat, mInterval);

            try {
                Logs.e(this, "initGdxFragment() chartGdxFragment 2) trying to attach...wasDetached=" + fr.isDetached());
                if (fr.isDetached()) {
                    FragmentTransaction b = getChildFragmentManager().beginTransaction();
                    b.attach(fr);
                    b.commit();
                }
            } catch (Exception ex) { Logs.e(TAG, ex.toString()); }

            try {
                Logs.e(this, "initGdxFragment() chartGdxFragment 3) trying to show...wasHidden=" + fr.isHidden());
                if (fr.isHidden())
                {
                    FragmentTransaction c = getChildFragmentManager().beginTransaction();
                    c.show(fr);
                    c.commit();
                }
            } catch (Exception ex) { Logs.e(TAG, ex.toString()); }

//            if (socketSwap) {
//                GraphicPresenterV2 graphicPresenter
//                        = (GraphicPresenterV2) PresentersManager.getPresenterByFragmentNameAndClass("GraphicFragment", GraphicPresenterImplV2.class);
//                if (graphicPresenter != null) {
//                    Logs.e(this, "initGraphic() graphicFragment's presenter exists!!! So calling it's WS methods...");
//                    graphicPresenter.m22553e();
//                } else
//                    Logs.e(this, "initGraphic() graphicFragment's presenter is absent...");
//                socketSwap = false;
//            }
        }
    }

    private void replaceGraphicFragment()
    {
        Fragment fragment = new ChartGdxFragment();

        Bundle args = new Bundle();
        args.putString(ChartGdxFragment.KEY_TOOL_NAME, mToolName);
        args.putParcelable(ChartGdxFragment.KEY_TOOL_FORMAT, new ToolFormat(8, mToolPrice));
        args.putString(ChartGdxFragment.KEY_TOOL_INTERVAL, mInterval);
        fragment.setArguments(args);

        FragmentTransaction a = getChildFragmentManager().beginTransaction();
        a.replace(R.id.chart_container, fragment, "chartGdxFragment");
        a.commit();
    }

    @Override
    public BasePresenter getPresenter() {
        return mChartParentPresenter;
    }
}
