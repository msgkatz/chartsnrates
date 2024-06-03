package com.msgkatz.ratesapp.domain.interactors.params;

import com.msgkatz.ratesapp.data.entities.rest.Asset;

/**
 * Created by msgkatz on 29/08/2018.
 */

public class ToolParams extends CommonParams {

    private String assetSymbol;
    private Asset asset;

    public ToolParams(String assetSymbol)
    {
        this.assetSymbol = assetSymbol;
    }

    public ToolParams(Asset asset)
    {
        this.asset = asset;
    }

    public String getAssetSymbol()
    {
        return (asset == null) ? assetSymbol : asset.getNameShort();
    }
}
