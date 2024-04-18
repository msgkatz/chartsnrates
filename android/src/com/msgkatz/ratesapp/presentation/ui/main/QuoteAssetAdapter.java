package com.msgkatz.ratesapp.presentation.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.utils.CommonUtil;
import com.msgkatz.ratesapp.utils.NumFormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by msgkatz on 12/09/2018.
 */

public class QuoteAssetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = QuoteAssetAdapter.class.getName();

    public interface OnItemClickListener {
        void onItemClicked(PriceSimple priceSimple);
    }

    private Context context;
    private Drawable placeholder;
    private List<PriceSimple> priceSimpleList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    public QuoteAssetAdapter(Context context)
    {
        this.context = context;
    }

    public QuoteAssetAdapter(Context context, Drawable placeholder)
    {
        this.context = context;
        this.placeholder = placeholder;
    }

    public QuoteAssetAdapter(Context context, List<PriceSimple> priceSimpleList)
    {
        this.context = context;
        this.priceSimpleList = priceSimpleList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateData(List<PriceSimple> priceSimpleList)
    {
        if (this.priceSimpleList.size() != 0) {


            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(new QuoteAssetAdapterDiffCallback(QuoteAssetAdapter.this.priceSimpleList, priceSimpleList));
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
        mHolder.mTitleMain.setText(priceSimple.getTool().getName());
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

        RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);
        Glide.with(context)
                .load(priceSimple.getTool().getBaseAsset().getLogoFullUrl())
                .apply(requestOptions)
                .into(mHolder.mLogo);
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

        //public @BindView(R.id.image)
        CircleImageView mLogo;
        //@BindView(R.id.title_main)
        TextView mTitleMain;
        //@BindView(R.id.title_2nd)
        TextView mTitle2nd;
        //@BindView(R.id.price)
        TextView mPrice;
        //@BindView(R.id.delta_layout)
        LinearLayout mDeltaLayout;
        //@BindView(R.id.price_delta_direction)
        ImageView mDirection;
        //@BindView(R.id.price_delta)
        TextView mPriceDelta;

        public QuoteAssetViewHolder(View itemView) {
            super(itemView);

            mLogo = itemView.findViewById(R.id.image);

            mTitleMain = itemView.findViewById(R.id.title_main);

            mTitle2nd = itemView.findViewById(R.id.title_2nd);

            mPrice = itemView.findViewById(R.id.price);

            mDeltaLayout = itemView.findViewById(R.id.delta_layout);

            mDirection = itemView.findViewById(R.id.price_delta_direction);

            mPriceDelta = itemView.findViewById(R.id.price_delta);
            //ButterKnife.bind(this, itemView);
        }
    }
}
