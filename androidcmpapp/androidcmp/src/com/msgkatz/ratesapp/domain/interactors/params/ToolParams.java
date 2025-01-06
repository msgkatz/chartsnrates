package com.msgkatz.ratesapp.domain.interactors.params;

import com.msgkatz.ratesapp.data.entities.rest.AssetDT;

/**
 * Created by msgkatz on 29/08/2018.
 */

public class ToolParams extends CommonParams {

    private String assetSymbol;
    private AssetDT asset;

    public ToolParams(String assetSymbol)
    {
        this.assetSymbol = assetSymbol;
    }

    public ToolParams(AssetDT asset)
    {
        this.asset = asset;
    }

    public String getAssetSymbol()
    {
        return (asset == null) ? assetSymbol : asset.getNameShort();
    }
}
