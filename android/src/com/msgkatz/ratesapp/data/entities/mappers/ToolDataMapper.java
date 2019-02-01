package com.msgkatz.ratesapp.data.entities.mappers;

import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.data.entities.rest.ToolDT;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.utils.Parameters;

import java.util.Map;

/**
 * Created by msgkatz on 30/08/2018.
 */

public class ToolDataMapper {

    public static Tool transform(ToolDT source, Map<String, Asset> assetMap)
    {
        Asset base = assetMap.get(source.getBaseAsset());
        Asset quote = assetMap.get(source.getQuoteAsset());

        if (base == null)
        {
            base = new Asset(-1, source.getBaseAsset(), source.getBaseAsset());
            assetMap.put(source.getBaseAsset(), base);
        }

        if (quote == null)
        {
            quote = new Asset(-1, source.getQuoteAsset(), source.getQuoteAsset());
            assetMap.put(source.getQuoteAsset(), quote);
        }

        boolean isActive = (Parameters.ACTIVE_TOOL_STATUS.equals(source.getStatus())
                            || Parameters.ACTIVE_TOOL_STATUS.toLowerCase().equals(source.getStatus()));

        Tool tool = new Tool(source.getSymbol(), base, quote, isActive);

        return tool;
    }
}
