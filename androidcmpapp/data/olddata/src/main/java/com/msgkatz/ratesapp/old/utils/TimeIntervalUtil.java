package com.msgkatz.ratesapp.old.utils;

/**
 * Created by msgkatz on 27/09/2018.
 */

@Deprecated
public class TimeIntervalUtil {

    public static String getIntervalNameByValue(int interval)
    {
        switch (interval)
        {
            case 1:
                return "1s";
            default:
                    return "1s";
        }
    }

    public static int getIntervalValueByName(String intervalName)
    {
        switch (intervalName)
        {
            case "1s":
                return 1;
            default:
                return 1;
        }
    }
}
