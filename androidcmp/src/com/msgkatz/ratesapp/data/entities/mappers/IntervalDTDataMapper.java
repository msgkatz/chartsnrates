package com.msgkatz.ratesapp.data.entities.mappers;

import com.msgkatz.ratesapp.data.entities.IntervalDT;
import com.msgkatz.ratesapp.domain.entities.Interval;

import java.util.ArrayList;
import java.util.List;

public class IntervalDTDataMapper {

    public static List<Interval> transform(List<IntervalDT> inputList)
    {
        List<Interval> retList = new ArrayList<>();

        if (inputList != null)
        {
            for (IntervalDT item : inputList) {
                Interval interval = transform(item);
                if (interval != null)
                    retList.add(interval);
            }
        }

        return retList;
    }

    public static Interval transform(IntervalDT input)
    {
        if (input == null)
            return null;

        return new Interval(input.id, input.type,
                            input.symbol, input.symbolApi,
                            input.perItemDefaultMs, input.perBlockDefaultMs,
                            input.perItemMinMs, input.perBlockMinMs,
                            input.perItemMaxMs, input.perBlockMaxMs, input.inUse);
    }
}
