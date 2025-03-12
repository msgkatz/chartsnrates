package com.msgkatz.ratesapp.old.data.entities.rest;

/**
 * Created by msgkatz on 22/07/2018.
 *
 *  1499040000000,      // Open time - 0
    "0.01634790",       // Open - 1
    "0.80000000",       // High - 2
    "0.01575800",       // Low - 3
    "0.01577100",       // Close - 4
    "148976.11427815",  // Volume - 5
    1499644799999,      // Close time - 6
    "2434.19055334",    // Quote asset volume - 7
    308,                // Number of trades - 8
    "1756.87402397",    // Taker buy base asset volume - 9
    "28.46694368",      // Taker buy quote asset volume - 10
    "17928899.62484339" // Ignore - 11

 */

public class PriceByCandle {

   private long   openTime;
   private double open;
   private double high;
   private double low;
   private double close;

   private double volume;
   private long   closeTime;
   private double quoteAssetVolume;
   private long   tradesNum;
   private double takerBuyBaseAssetVolume;
   private double takerBuyQuoteAssetVolume;
   private double ignore;

   public long getCloseTime() {
      return closeTime;
   }

   public double getOpen() {
      return open;
   }

   public double getHigh() {
      return high;
   }

   public double getLow() {
      return low;
   }

   public double getClose() {
      return close;
   }

   public void setOpen(double open) {
      this.open = open;
   }

   public void setHigh(double high) {
      this.high = high;
   }

   public void setLow(double low) {
      this.low = low;
   }

   public void setClose(double close) {
      this.close = close;
   }

   public void setCloseTime(long closeTime) {
      this.closeTime = closeTime;
   }

   public long getOpenTime() {
      return openTime;
   }

   public void setOpenTime(long openTime) {
      this.openTime = openTime;
   }

   @Override
   public String toString() {
      return String.format(
              "%1$s,      // Open time\n" +
              "    \"%2$s\",       // Open\n" +
              "    \"%3$s\",       // High\n" +
              "    \"%4$s\",       // Low\n" +
              "    \"%5$s\",       // Close\n" +
              "    \"%6$s\",  // Volume\n" +
              "    %7$s,      // Close time\n" +
              "    \"%8$s\",    // Quote asset volume\n" +
              "    %9$s,                // Number of trades\n" +
              "    \"%10$s\",    // Taker buy base asset volume\n" +
              "    \"%11$s\",      // Taker buy quote asset volume\n" +
              "    \"%12$s\" // Ignore\n",
              Long.toString(openTime),
              Double.toString(open),
              Double.toString(high),
              Double.toString(low),
              Double.toString(close),
              Double.toString(volume),
              Long.toString(closeTime),
              Double.toString(quoteAssetVolume),
              Long.toString(tradesNum),
              Double.toString(takerBuyBaseAssetVolume),
              Double.toString(takerBuyQuoteAssetVolume),
              Double.toString(ignore));
   }
}
