package com.msgkatz.ratesapp.old.data.entities.mappers;

import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.old.data.entities.rest.ToolDT;
import com.msgkatz.ratesapp.old.domain.entities.ToolJava;
import com.msgkatz.ratesapp.old.utils.Parameters;

import java.util.Map;

/**
 * Created by msgkatz on 30/08/2018.
 */

public class ToolDataMapper {

    public static ToolJava transform(ToolDT source, Map<String, AssetDT> assetMap)
    {
        AssetDT base = assetMap.get(source.getBaseAsset());
        AssetDT quote = assetMap.get(source.getQuoteAsset());

        if (base == null)
        {
            base = new AssetDT(-1, source.getBaseAsset(), source.getBaseAsset());
            assetMap.put(source.getBaseAsset(), base);
        }

        if (quote == null)
        {
            quote = new AssetDT(-1, source.getQuoteAsset(), source.getQuoteAsset());
            assetMap.put(source.getQuoteAsset(), quote);
        }

        boolean isActive = (Parameters.ACTIVE_TOOL_STATUS.equals(source.getStatus())
                            || Parameters.ACTIVE_TOOL_STATUS.toLowerCase().equals(source.getStatus()));

        ToolJava tool = new ToolJava(source.getSymbol(), base, quote, isActive);

        return tool;
    }
}
