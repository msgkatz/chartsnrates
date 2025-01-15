package com.msgkatz.ratesapp.presentation.ui.chart2.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.utils.CommonUtil;
import com.msgkatz.ratesapp.utils.NumFormatUtil;

import java.util.Locale;

/**
 * Created by msgkatz on 17/09/2018.
 */

public class CurrentRateLayout extends RelativeLayout {

    TextView mPrice;
    LinearLayout mDeltaLayout;
    ImageView mDirection;
    TextView mPriceDelta;

    private double lastRate = -1;
    private boolean isInitialized = false;

    public CurrentRateLayout(Context context) {
        super(context);
        initComponent();
    }

    public CurrentRateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public CurrentRateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        View view = inflate(getContext(), R.layout.view_current_rate, this);

        mPrice = view.findViewById(R.id.price);
        mDeltaLayout = view.findViewById(R.id.delta_layout);
        mDirection = view.findViewById(R.id.price_delta_direction);
        mPriceDelta = view.findViewById(R.id.price_delta);

        mDeltaLayout.setVisibility(View.INVISIBLE);
    }

    public void setRate(double newRate)
    {
        if (!isInitialized)
        {
            mPrice.setText(NumFormatUtil.getFormattedPrice(newRate));
            //mPrice.setText(String.format(Locale.getDefault(), "%1$s", Double.toString(newRate)));
            lastRate = newRate;
            isInitialized = true;
        }
        else
        {
            mDeltaLayout.setVisibility(View.VISIBLE);
            mPrice.setText(NumFormatUtil.getFormattedPrice(newRate));
            //mPrice.setText(String.format(Locale.getDefault(), "%1$s", Double.toString(newRate)));

            double delta = 100 * Math.abs(newRate - lastRate)/lastRate;
            if (((int) (delta * 100)) == 0 && 1==2)
                mPriceDelta.setText(String.format(Locale.getDefault(), "%1$s%%",
                        NumFormatUtil.getFormattedPrice(delta)));
            else
                mPriceDelta.setText(String.format(Locale.getDefault(), "%.2f%%", delta));
            if (newRate >= lastRate)
            {
                mPrice.setTextColor(CommonUtil.getColor(getContext(), R.color.theme_green));
                mPriceDelta.setTextColor(CommonUtil.getColor(getContext(), R.color.theme_green));
                mDirection.setRotation(270);
                mDirection.setColorFilter(CommonUtil.getColor(getContext(), R.color.theme_green));
            }
            else
            {
                mPrice.setTextColor(CommonUtil.getColor(getContext(), R.color.theme_radical_red));
                mPriceDelta.setTextColor(CommonUtil.getColor(getContext(), R.color.theme_radical_red));
                mDirection.setRotation(90);
                mDirection.setColorFilter(CommonUtil.getColor(getContext(), R.color.theme_radical_red));
            }

            lastRate = newRate;
        }
    }
}
