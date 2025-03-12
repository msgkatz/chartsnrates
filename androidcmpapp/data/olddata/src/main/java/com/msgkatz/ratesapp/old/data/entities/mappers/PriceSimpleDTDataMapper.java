package com.msgkatz.ratesapp.old.data.entities.mappers;

import com.msgkatz.ratesapp.old.data.entities.rest.PriceSimpleDT;
import com.msgkatz.ratesapp.old.data.entities.wsocks.StreamMarketTickerMini;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msgkatz on 24/08/2018.
 */

public class PriceSimpleDTDataMapper {

    public static PriceSimpleDT transform(StreamMarketTickerMini source)
    {
        if (source == null)
            return null;

        PriceSimpleDT retVal = new PriceSimpleDT(source.symbol, source.close);

        return retVal;
    }

    public static List<PriceSimpleDT> transform(List<StreamMarketTickerMini> source)
    {
        if (source == null)
            return null;

        List<PriceSimpleDT> list = new ArrayList<>();

        for (StreamMarketTickerMini item : source)
        {
            PriceSimpleDT simple = transform(item);
            if (simple != null)
                list.add(simple);
        }

        return list;
    }


}
