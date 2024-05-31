package com.msgkatz.ratesapp.presentation.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.msgkatz.ratesapp.utils.Parameters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by msgkatz on 17/09/2018.
 */

public class ToolFormat implements Parcelable {

    private int maxFractionDigits = 8;
    private double priceExample;

    public ToolFormat(int maxFractionDigits, double priceExample)
    {
        this.maxFractionDigits = maxFractionDigits;
        this.priceExample = priceExample;
        this.checkFractionDigitsByPrice();
    }

    protected ToolFormat(Parcel in) {
        maxFractionDigits = in.readInt();
        priceExample = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxFractionDigits);
        dest.writeDouble(priceExample);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ToolFormat> CREATOR = new Creator<ToolFormat>() {
        @Override
        public ToolFormat createFromParcel(Parcel in) {
            return new ToolFormat(in);
        }

        @Override
        public ToolFormat[] newArray(int size) {
            return new ToolFormat[size];
        }
    };

    private void checkFractionDigitsByPrice()
    {
        if (this.priceExample == 0.0)
            return;

        int decimal = (int) this.priceExample;
        if (decimal == 0)
            return;
        int decLength = Integer.toString(decimal).length();
        if (decimal > 0
                && decLength > 1
                && decLength < this.maxFractionDigits)
            this.maxFractionDigits -= (decLength - 1);
    }

    public int getMaxFractionDigits() {
        return maxFractionDigits;
    }

    public double getPriceExample() {
        return priceExample;
    }

    public DecimalFormat getQuotesFormat(int digits) {
        String pattern = Parameters.QUOTE_NUMBER_FORMAT_NO_DEC;
        String zeros = "";
        if (digits > 0) {
            pattern = pattern.concat(".");
        }
        for (int i=0; i < digits; i++) {
            zeros = zeros.concat("0");
        }
        pattern = pattern.concat(zeros);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');

        return new DecimalFormat(pattern,symbols);
    }
}
