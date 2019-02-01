package com.msgkatz.ratesapp.presentation.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.presentation.common.Layout;
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.ui.main.base.BaseMainFragment;
import com.msgkatz.ratesapp.presentation.ui.main.base.MainRouter;
import com.msgkatz.ratesapp.presentation.common.widget.EndOffsetItemDecoration;
import com.msgkatz.ratesapp.utils.CommonUtil;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by msgkatz on 09/09/2018.
 */

@SuppressWarnings("WeakerAccess")
@Layout(id = R.layout.fragment_quote_asset_alter)
public class QuoteAssetFragment extends BaseMainFragment implements QuoteAssetView
{
    public static final String TAG = QuoteAssetFragment.class.getSimpleName();

    public static final String KEY_QUOTE_ASSET_NAME = "com.msgkatz.ratesapp.quoteasset.name";
    public static final String KEY_QUOTE_ASSET_ID = "com.msgkatz.ratesapp.quoteasset.id";

    @BindView(R.id.title_main) TextView mTitleMain;
    @BindView(R.id.title_2nd) TextView mTitle2nd;
    @BindView(R.id.rvlist) RecyclerView mRecyclerView;

    @Inject
    QuoteAssetPresenter mQuoteAssetPresenter;

    private String mQuoteAssetName;
    private QuoteAssetAdapterThreaded mAdapter;

    public static QuoteAssetFragment newInstance(String quoteAssetName) {

        Bundle args = new Bundle();
        args.putString(KEY_QUOTE_ASSET_NAME, quoteAssetName);

        QuoteAssetFragment fragment = new QuoteAssetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mQuoteAssetName = getArguments().getString(KEY_QUOTE_ASSET_NAME);
        }
        mQuoteAssetPresenter.setQuoteAsset(mQuoteAssetName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setupRecycler();

        return view;
    }



    private void setupRecycler()
    {
        Drawable placeholder = ((MainActivity)getActivity()).getImageByTabName(mQuoteAssetName);
        mAdapter = new QuoteAssetAdapterThreaded(this.getContext(), placeholder);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mAdapter);

        EndOffsetItemDecoration itemDecoration = new EndOffsetItemDecoration(CommonUtil.dpToPx(60));
        mRecyclerView.addItemDecoration(itemDecoration);

        //ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        mAdapter.setOnItemClickListener(new QuoteAssetAdapterThreaded.OnItemClickListener() {
            @Override
            public void onItemClicked(PriceSimple priceSimple) {
                startChart(priceSimple);
            }
        });
    }

    private void startChart(PriceSimple priceSimple)
    {
        ((MainRouter)getPresenter().getRouter()).showChart(priceSimple);
    }

    /**
     * QuoteAssetView's methods implementation
     */
    @Override
    public void updateQuoteAsset(Asset quoteAset) {
        mTitleMain.setText(String.format(Locale.getDefault(), "%1$s %2$s",
                quoteAset.getNameShort(), getResources().getString(R.string.screen_one_markets)));
        mTitle2nd.setText(quoteAset.getNameLong());
    }

    @Override
    public void updatePriceList(List<PriceSimple> list) {
        mAdapter.updateData(list);

    }


    /**
     * BaseMainFragment's methods overriding
     */
    @Override
    public String getFragmentName() {
        return (mQuoteAssetName == null) ? super.getFragmentName() : mQuoteAssetName;
    }

    @Override
    public BasePresenter getPresenter() {
        return mQuoteAssetPresenter;
    }
}
