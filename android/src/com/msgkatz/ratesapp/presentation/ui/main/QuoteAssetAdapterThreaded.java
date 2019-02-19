package com.msgkatz.ratesapp.presentation.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.utils.CommonUtil;
import com.msgkatz.ratesapp.utils.NumFormatUtil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by msgkatz on 28/09/2018.
 */

public class QuoteAssetAdapterThreaded extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = QuoteAssetAdapterThreaded.class.getName();

    public interface OnItemClickListener {
        void onItemClicked(PriceSimple priceSimple);
    }

    private Context context;
    private Drawable placeholder;
    private List<PriceSimple> priceSimpleList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private final ArrayDeque<List<PriceSimple>> pendingUpdates = new ArrayDeque<>();
    final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isInitialised = false;

    public QuoteAssetAdapterThreaded(Context context)
    {
        this.context = context;
    }

    public QuoteAssetAdapterThreaded(Context context, Drawable placeholder)
    {
        this.context = context;
        this.placeholder = placeholder;
    }

    public QuoteAssetAdapterThreaded(Context context, List<PriceSimple> priceSimpleList)
    {
        this.context = context;
        this.priceSimpleList = priceSimpleList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateDataOld(List<PriceSimple> priceSimpleList)
    {
        if (this.priceSimpleList.size() != 0) {


            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(new QuoteAssetAdapterDiffCallback(QuoteAssetAdapterThreaded.this.priceSimpleList, priceSimpleList));
            diffResult.dispatchUpdatesTo(this);
            this.priceSimpleList.clear();
            this.priceSimpleList.addAll(priceSimpleList);
        }
        else
        {
            this.priceSimpleList.addAll(priceSimpleList);
            notifyDataSetChanged();
        }
    }

    public void updateData(List<PriceSimple> priceSimpleList)
    {
        if (!isInitialised)
        {
            this.priceSimpleList.addAll(priceSimpleList);
            notifyDataSetChanged();
            isInitialised = true;
        }
        else
        {
            pendingUpdates.add(priceSimpleList);
            if (pendingUpdates.size() > 1) {
                return;
            }
            updateDataInternal(priceSimpleList);

        }


    }

    public void updateDataInternal(List<PriceSimple> priceSimpleList)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult diffResult =
                        DiffUtil.calculateDiff(new QuoteAssetAdapterDiffCallback(QuoteAssetAdapterThreaded.this.priceSimpleList, priceSimpleList));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        applyDiffResult(priceSimpleList, diffResult);
                    }
                });
            }
        }).start();
    }

    protected void applyDiffResult(List<PriceSimple> newItems,
                                   DiffUtil.DiffResult diffResult) {
        pendingUpdates.remove();

        diffResult.dispatchUpdatesTo(this);
        this.priceSimpleList.clear();
        this.priceSimpleList.addAll(newItems);

        if (pendingUpdates.size() > 0) {
            updateDataInternal(pendingUpdates.peek());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuoteAssetViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_quote_asset_alter, parent, false));
                //.inflate(R.layout.item_rv_quote_asset, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PriceSimple priceSimple = priceSimpleList.get(position);

        QuoteAssetViewHolder mHolder = (QuoteAssetViewHolder) holder;
        //mHolder.mTitleMain.setText(priceSimple.getTool().getName());
        String pair = String.format(Locale.getDefault(), "%1$s/%2$s", priceSimple.getTool().getBaseAsset().getNameShort(), priceSimple.getTool().getQuoteAsset().getNameShort());
        mHolder.mTitleMain.setText(pair);
        mHolder.mTitle2nd.setText(priceSimple.getTool().getBaseAsset().getNameLong());

        //mHolder.mPrice.setText(String.format(Locale.getDefault(), "%1$s", Double.toString(priceSimple.getPrice())));
        mHolder.mPrice.setText(NumFormatUtil.getFormattedPrice(priceSimple.getPrice()));
        mHolder.mDeltaLayout.setVisibility(View.INVISIBLE);

        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(priceSimple);
            }
        });

        // TODO iss-33 p.1
        // TODO quick solution implemented w/ changing item height for disabled elements
        // TODO must be fixed on data layer
        if (!priceSimple.getTool().isActive())
        {
            mHolder.mContainer.setMaxHeight(CommonUtil.dpToPx(0));
        }
        else {

            mHolder.mContainer.setMaxHeight(CommonUtil.dpToPx(88));

            //RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);
            RequestOptions requestOptions = new RequestOptions().fallback(placeholder).error(placeholder);
            Glide.with(context)
                    //.asDrawable()
                    .load(priceSimple.getTool().getBaseAsset().getLogoFullUrl())
                    //.transition(DrawableTransitionOptions.withCrossFade())
                    //.transition(withCrossFade())
                    .apply(requestOptions)
                    .into(mHolder.mLogo);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            //return;
        }
        else
        {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if(key.equals(QuoteAssetAdapterDiffCallback.KEY_DIFF))
                {
                    double[] pricesOldNewArray = o.getDoubleArray(QuoteAssetAdapterDiffCallback.KEY_DIFF);
                    if (pricesOldNewArray == null
                            || (pricesOldNewArray != null && pricesOldNewArray.length != 2))
                        return;

                    QuoteAssetViewHolder mHolder = (QuoteAssetViewHolder) holder;
                    mHolder.mDeltaLayout.setVisibility(View.VISIBLE);
                    //mHolder.mPrice.setText(String.format(Locale.getDefault(), "%1$s", Double.toString(pricesOldNewArray[1])));
                    mHolder.mPrice.setText(NumFormatUtil.getFormattedPrice(pricesOldNewArray[1]));

                    double delta = 100 * Math.abs(pricesOldNewArray[1] - pricesOldNewArray[0])/pricesOldNewArray[0];
                    if (((int) (delta * 100)) == 0 && 1==2)
                        mHolder.mPriceDelta.setText(String.format(Locale.getDefault(), "%1$s%%",
                                NumFormatUtil.getFormattedPrice(delta)));
                    else
                        mHolder.mPriceDelta.setText(String.format(Locale.getDefault(), "%.2f%%", delta));

                    if (pricesOldNewArray[1] >= pricesOldNewArray[0])
                    {
                        mHolder.mPrice.setTextColor(CommonUtil.getColor(context, R.color.theme_green));
                        mHolder.mPriceDelta.setTextColor(CommonUtil.getColor(context, R.color.theme_green));
                        mHolder.mDirection.setRotation(270);
                        mHolder.mDirection.setColorFilter(CommonUtil.getColor(context, R.color.theme_green));
                    }
                    else
                    {
                        mHolder.mPrice.setTextColor(CommonUtil.getColor(context, R.color.theme_radical_red));
                        mHolder.mPriceDelta.setTextColor(CommonUtil.getColor(context, R.color.theme_radical_red));
                        mHolder.mDirection.setRotation(90);
                        mHolder.mDirection.setColorFilter(CommonUtil.getColor(context, R.color.theme_radical_red));
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return (priceSimpleList == null) ? 0 : priceSimpleList.size();
    }


    public static class QuoteAssetViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container)
        ConstraintLayout mContainer;
        @BindView(R.id.image) CircleImageView mLogo;
        @BindView(R.id.title_main) TextView mTitleMain;
        @BindView(R.id.title_2nd) TextView mTitle2nd;

        @BindView(R.id.price) TextView mPrice;
        @BindView(R.id.delta_layout) LinearLayout mDeltaLayout;
        @BindView(R.id.price_delta_direction) ImageView mDirection;
        @BindView(R.id.price_delta) TextView mPriceDelta;

        public QuoteAssetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}