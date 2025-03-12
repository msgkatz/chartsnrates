package com.msgkatz.ratesapp.old.data.entities.mappers;

import com.msgkatz.ratesapp.old.data.entities.IntervalDT;
import com.msgkatz.ratesapp.old.domain.entities.IntervalJava;

import java.util.ArrayList;
import java.util.List;

public class IntervalDTDataMapper {

    public static List<IntervalJava> transform(List<IntervalDT> inputList)
    {
        List<IntervalJava> retList = new ArrayList<>();

        if (inputList != null)
        {
            for (IntervalDT item : inputList) {
                IntervalJava interval = transform(item);
                if (interval != null)
                    retList.add(interval);
            }
        }

        return retList;
    }

    public static IntervalJava transform(IntervalDT input)
    {
        if (input == null)
            return null;

        return new IntervalJava(input.id, input.type,
                            input.symbol, input.symbolApi,
                            input.perItemDefaultMs, input.perBlockDefaultMs,
                            input.perItemMinMs, input.perBlockMinMs,
                            input.perItemMaxMs, input.perBlockMaxMs, input.inUse);
    }
}
