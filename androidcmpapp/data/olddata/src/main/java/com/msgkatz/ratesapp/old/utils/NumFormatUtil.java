package com.msgkatz.ratesapp.old.utils;

//import com.msgkatz.ratesapp.presentation.entities.ToolFormat;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by msgkatz on 13/10/2018.
 */

public class NumFormatUtil {

    public static float getFractial(int maxDigits, boolean getMax)
    {
        float min;
        float max;
        switch (maxDigits) {
            case 0:
                min = 5.0f;
                max = 10.0f;
                break;
            case 1:
                min = 0.5f;
                max = 1.0f;
                break;
            case 2:
                min = 0.05f;
                max = 0.1f;
                break;
            case 3:
                min = 0.005f;
                max = 0.01f;
                break;
            case 4:
                min = 5.0E-4f;
                max = 0.001f;
                break;
            case 5:
                min = 5.0E-5f;
                max = 1.0E-4f;
                break;
            case 6:
                min = 5.0E-6f;
                max = 1.0E-5f;
                break;
            case 7:
                min = 5.0E-7f;
                max = 1.0E-6f;
                break;
            case 8:
                min = 5.0E-8f;
                max = 1.0E-7f;
                break;
            default:
                min = 5.0E-6f;
                max = 1.0E-5f;
                break;
        }

        if (getMax)
            return max;
        else
            return min;
    }

    public static String getFormattedPrice(final double price)
    {
        return getFormattedPriceViaNumFormat(price);
        //return getFormattedPriceViaDecFormat(price);
    }

//    private static String getFormattedPriceViaDecFormat(final double price)
//    {
//        ToolFormat retVal = new ToolFormat(Parameters.DEFAULT_MAX_FRACTION_DIGITS, price);
//
//        int maxDigits = retVal.getMaxFractionDigits();
//        DecimalFormat quoteFormat = retVal.getQuotesFormat(maxDigits);
//
//        return quoteFormat.format((double) price);
//    }

    private static String getFormattedPriceViaNumFormat(final double price)
    {
        return numberFormat.format(price);
    }

    private static final NumberFormat numberFormat = NumberFormat.getInstance();
    static {
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
    }
}
