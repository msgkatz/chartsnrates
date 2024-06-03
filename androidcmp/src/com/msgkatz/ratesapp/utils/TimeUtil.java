package com.msgkatz.ratesapp.utils;

/**
 * Created by msgkatz on 26/09/2018.
 */

public class TimeUtil {

    /**
     * When splitting time into set of equal sequential periods of length delta, this function
     * returns first time-value of a period which inputted unixTime belongs to
     *
     * @param delta length of period of time (in seconds)
     * @param unixTime any time value (as timestamp in seconds)
     * @return first time-value of a period which unixTime belongs to (as timestamp in seconds)
     */
    public static long normalizeInSeconds(int delta, long unixTime) {
        return ((long)
                Math.floor(
                        (double) (unixTime / ((long) delta))
                )
        )
                *
                ((long) delta);
    }

    public static long normalizeInSeconds(long delta, long unixTime) {
        return ((long)
                Math.floor(
                        (double) (unixTime / ((long) delta))
                )
        )
                *
                ((long) delta);
    }
}
