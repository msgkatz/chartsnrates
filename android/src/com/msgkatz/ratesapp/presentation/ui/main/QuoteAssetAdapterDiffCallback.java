package com.msgkatz.ratesapp.presentation.ui.main;

import android.os.Bundle;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * Created by msgkatz on 13/09/2018.
 */

public class QuoteAssetAdapterDiffCallback extends DiffUtil.Callback {

    public final static String KEY_DIFF = "KEY_DIFF";

    private List<PriceSimple> priceSimpleListOld;
    private List<PriceSimple> priceSimpleListNew;

    public QuoteAssetAdapterDiffCallback(List<PriceSimple> priceSimpleListOld, List<PriceSimple> priceSimpleListNew)
    {
        this.priceSimpleListOld = priceSimpleListOld;
        this.priceSimpleListNew = priceSimpleListNew;
    }

    @Override
    public int getOldListSize() {
        return priceSimpleListOld.size();
    }

    @Override
    public int getNewListSize() {
        return priceSimpleListNew.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return priceSimpleListOld.get(oldItemPosition).getTool().getName()
                .equals(priceSimpleListNew.get(newItemPosition).getTool().getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return (priceSimpleListOld.get(oldItemPosition).getPrice()
                == priceSimpleListNew.get(newItemPosition).getPrice());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        PriceSimple newPriceSimple = priceSimpleListNew.get(newItemPosition);
        PriceSimple oldPriceSimple = priceSimpleListOld.get(oldItemPosition);

        Bundle diffBundle = new Bundle();
        if (newPriceSimple.getPrice() != oldPriceSimple.getPrice())
        {
            diffBundle.putDoubleArray(KEY_DIFF,
                    new double[]{oldPriceSimple.getPrice(), newPriceSimple.getPrice()});
        }

        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}
