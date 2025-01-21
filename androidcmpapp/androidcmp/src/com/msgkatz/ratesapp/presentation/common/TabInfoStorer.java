package com.msgkatz.ratesapp.presentation.common;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.feature.quoteasset.QuoteAssetDrawableDataKeeper;
import com.msgkatz.ratesapp.utils.CommonUtil2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.msgkatz.ratesapp.presentation.ui.app.TabItem;

public class TabInfoStorer implements QuoteAssetDrawableDataKeeper {

    private Context appContext;
    private boolean isInitialised = false;
    private List<TabItem> items = new ArrayList<>();
    private Map<String, Integer> tabPositions = new HashMap<>();

    public TabInfoStorer(Context appContext) {
        this.appContext = appContext;
        initTabs();
    }

    public void initTabs()
    {
        Resources res = appContext.getResources();
        TypedArray icons = res.obtainTypedArray(R.array.tab_icons);
        TypedArray names = res.obtainTypedArray(R.array.tab_names);
        //int color = CommonUtil.getColor(App.getInstance(), R.color.main_tabs_item_selected);
        int color = CommonUtil2.getColor(appContext, R.color.main_tabs_item_selected);

        for (int index = 0; index < icons.length(); index++)
        {
            Drawable drawable = icons.getDrawable(index);
            //DrawableCompat.setTint(drawable, color);
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(appContext, R.color.main_tabs_item_selected), PorterDuff.Mode.SRC_IN));

            Drawable sd = new ScaleDrawable(icons.getDrawable(index), 0, 22, 22).getDrawable();
            sd.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(appContext, R.color.main_tabs_item_selected), PorterDuff.Mode.SRC_IN));

            String name = names.getString(index);

            TabItem item = new TabItem(drawable, sd, name);
            item.icon = icons.getResourceId(index, R.drawable.cur_bnb);

            items.add(item);

            //tabImages.put(name, item);
            tabPositions.put(name, index);
        }

        icons.recycle();
        names.recycle();

        if (items.size() > 0)
            isInitialised = true;
    }

    public boolean isInitialised() {
        return isInitialised;
    }

    public Iterable<TabItem> getItems() {
        //return items;
        return new ArrayList<>(items);
    }



    public Drawable getDrawableByQuoteAssetName(String name) {
        if (tabPositions.containsKey(name))
            return items.get(tabPositions.get(name)).iconDrawable;
        else
            return items.get(0).iconDrawable;
    }


    @NonNull
    public Drawable getSmallDrawableByQuoteAssetName(String name) {
        if (tabPositions.containsKey(name))
            return items.get(tabPositions.get(name)).smallDrawable;
        else
            return items.get(0).smallDrawable;
    }
}
