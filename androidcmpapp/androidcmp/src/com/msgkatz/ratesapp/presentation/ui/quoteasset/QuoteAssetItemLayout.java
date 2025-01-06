package com.msgkatz.ratesapp.presentation.ui.quoteasset;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.msgkatz.ratesapp.R;

import butterknife.ButterKnife;

/**
 * Created by msgkatz on 12/09/2018.
 */

public class QuoteAssetItemLayout extends RelativeLayout {

    public QuoteAssetItemLayout(Context context) {
        super(context);
        initComponent();
    }

    public QuoteAssetItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public QuoteAssetItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        View view = inflate(getContext(), R.layout.view_quote_asset_item, this);
        ButterKnife.bind(this, view);
    }
}
