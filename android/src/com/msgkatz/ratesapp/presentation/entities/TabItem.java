package com.msgkatz.ratesapp.presentation.entities;

import android.graphics.drawable.Drawable;

/**
 * Bottom sheet navigation's tab item presentation
 *
 * Created by msgkatz on 09/09/2018.
 */

public class TabItem {

    public int icon;
    public Drawable iconDrawable;
    public Drawable smallDrawable;
    public String quoteAssetName;

    public TabItem(int icon, String quoteAssetName)
    {
        this.icon = icon;
        this.quoteAssetName = quoteAssetName;
    }

    public TabItem(Drawable iconDrawable, String quoteAssetName)
    {
        this.iconDrawable = iconDrawable;
        this.quoteAssetName = quoteAssetName;
    }

    public TabItem(Drawable iconDrawable, Drawable smallDrawable, String quoteAssetName)
    {
        this.iconDrawable = iconDrawable;
        this.smallDrawable = smallDrawable;
        this.quoteAssetName = quoteAssetName;
    }

}
