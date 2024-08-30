package com.msgkatz.ratesapp.data.entities.mappers;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.rest.PriceByCandle;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamKline;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamMarketTickerMini;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.utils.Logs;
import com.msgkatz.ratesapp.utils.TimeIntervalUtil;
import com.msgkatz.ratesapp.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msgkatz on 23/08/2018.
 */

public class CandleDataMapper {

    private final static String TAG = CandleDataMapper.class.getSimpleName();

    /**
     * PriceByCandle entity transformation to Candle
     */
    public static Candle transformVector(List<String> source)
    {
        if (source == null)
            return null;
        if (source.size() < 7)
            return null;

        try {
            PriceByCandle priceByCandle = new PriceByCandle();
            priceByCandle.setOpen(Double.valueOf((String)source.get(1)));
            priceByCandle.setHigh(Double.valueOf((String)source.get(2)));
            priceByCandle.setLow(Double.valueOf((String)source.get(3)));
            priceByCandle.setClose(Double.valueOf((String)source.get(4)));
            priceByCandle.setCloseTime(Long.valueOf((String)source.get(6)));

            Candle candle = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getCloseTime());
            return candle;

        } catch (Exception ex)
        {
            Logs.e(TAG, ex.toString());
            return null;
        }
    }

    public static List<Candle> transformVectorWithInterval(List<String> source, String interval, Long startTime, Long endTime)
    {
        if (source == null)
            return null;
        if (source.size() < 7)
            return null;

        try {
            PriceByCandle priceByCandle = new PriceByCandle();
            priceByCandle.setOpenTime(Long.valueOf((String)source.get(0)));
            priceByCandle.setOpen(Double.valueOf((String)source.get(1)));
            priceByCandle.setHigh(Double.valueOf((String)source.get(2)));
            priceByCandle.setLow(Double.valueOf((String)source.get(3)));
            priceByCandle.setClose(Double.valueOf((String)source.get(4)));
            priceByCandle.setCloseTime(Long.valueOf((String)source.get(6)));

            if (endTime != null && priceByCandle.getOpenTime() >= (TimeUtil.normalizeInSeconds(60, endTime.longValue()/1000)*1000))
                return null;

            //Candle candle = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getCloseTime());
            int intervalInSec = TimeIntervalUtil.getIntervalValueByName(interval) * 60;
            int itemCount = intervalInSec - 2;
            int deltaTime = (int)Math.floor((priceByCandle.getCloseTime() - priceByCandle.getOpenTime())/itemCount);

            List<Candle> tmp = new ArrayList<>();

            //Candle candleOpen = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getOpenTime());
            Candle candleClose = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getCloseTime());
            //tmp.add(candleOpen);
            boolean wasBrokeByEndTime = false;
            for (int i = 0; i <= itemCount; i++)
            {
                Candle cdl = new Candle(priceByCandle.getOpen(),
                        priceByCandle.getHigh(),
                        priceByCandle.getLow(),
                        priceByCandle.getClose(),
                        priceByCandle.getOpenTime() + (i * deltaTime));
                if (startTime != null && cdl.getTime() < startTime.longValue())
                    continue;
                if (endTime != null && cdl.getTime() > endTime.longValue()) {
                    wasBrokeByEndTime = true;
                    continue;
                }
                tmp.add(cdl);
            }
            if (!wasBrokeByEndTime)
                tmp.add(candleClose);
            return tmp;

        } catch (Exception ex)
        {
            Logs.e(TAG, ex.toString());
            return null;
        }
    }

    //ver2
    public static List<Candle> transformVectorWithInterval(List<String> source, Interval interval, Long startTime, Long endTime) {
        if (source == null)
            return null;
        if (source.size() < 7)
            return null;

        try {
            PriceByCandle priceByCandle = new PriceByCandle();
            priceByCandle.setOpenTime(Long.valueOf((String) source.get(0)));
            priceByCandle.setOpen(Double.valueOf((String) source.get(1)));
            priceByCandle.setHigh(Double.valueOf((String) source.get(2)));
            priceByCandle.setLow(Double.valueOf((String) source.get(3)));
            priceByCandle.setClose(Double.valueOf((String) source.get(4)));
            priceByCandle.setCloseTime(Long.valueOf((String) source.get(6)));

            long delta = priceByCandle.getCloseTime() - priceByCandle.getOpenTime() + 1;


            List<Candle> tmp = new ArrayList<>();

            if (delta == interval.getPerItemDefaultMs())
            {
                /** not using normalization, because openTime is normalized already **/
                Candle candle = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getOpenTime());
                if (priceByCandle.getOpenTime() < endTime)
                    tmp.add(candle);
            }
            else if (delta > interval.getPerItemDefaultMs())
            {
                int itemCount = (int) Math.floor(delta / interval.getPerItemDefaultMs());
                for (int i = 0; i < itemCount; i++)
                {
                    long newTime = priceByCandle.getOpenTime() + (i * interval.getPerItemDefaultMs());
                    if ((newTime < endTime) && (newTime <= priceByCandle.getCloseTime()))
                    {
                        Candle cdl = new Candle(priceByCandle.getOpen(),
                                priceByCandle.getHigh(),
                                priceByCandle.getLow(),
                                priceByCandle.getClose(), newTime);
                        tmp.add(cdl);
                    }
                }
            }
            else
            {
                //not in use
                throw new Exception("Something went wrong w/ historical data processing");
            }

            return tmp;

        } catch (Exception ex) {
            Logs.e(TAG, ex.toString());
            return null;
        }
    }


    /**
    public static List<Candle> transformVectorWithIntervalInternal(List<String> source, Interval interval, Long startTime, Long endTime)
    {
        if (source == null)
            return null;
        if (source.size() < 7)
            return null;

        try {
            PriceByCandle priceByCandle = new PriceByCandle();
            priceByCandle.setOpenTime(Long.valueOf((String)source.get(0)));
            priceByCandle.setOpen(Double.valueOf((String)source.get(1)));
            priceByCandle.setHigh(Double.valueOf((String)source.get(2)));
            priceByCandle.setLow(Double.valueOf((String)source.get(3)));
            priceByCandle.setClose(Double.valueOf((String)source.get(4)));
            priceByCandle.setCloseTime(Long.valueOf((String)source.get(6)));

            if (endTime != null && priceByCandle.getOpenTime() >= (TimeUtil.normalizeInSeconds(60, endTime.longValue()/1000)*1000))
                return null;

            //Candle candle = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getCloseTime());
            int intervalInSec = TimeIntervalUtil.getIntervalValueByName(interval) * 60;
            int itemCount = intervalInSec - 2;
            int deltaTime = (int)Math.floor((priceByCandle.getCloseTime() - priceByCandle.getOpenTime())/itemCount);

            List<Candle> tmp = new ArrayList<>();

            //Candle candleOpen = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getOpenTime());
            Candle candleClose = new Candle(priceByCandle.getOpen(), priceByCandle.getHigh(), priceByCandle.getLow(), priceByCandle.getClose(), priceByCandle.getCloseTime());
            //tmp.add(candleOpen);
            boolean wasBrokeByEndTime = false;
            for (int i = 0; i <= itemCount; i++)
            {
                Candle cdl = new Candle(priceByCandle.getOpen(),
                        priceByCandle.getHigh(),
                        priceByCandle.getLow(),
                        priceByCandle.getClose(),
                        priceByCandle.getOpenTime() + (i * deltaTime));
                if (startTime != null && cdl.getTime() < startTime.longValue())
                    continue;
                if (endTime != null && cdl.getTime() > endTime.longValue()) {
                    wasBrokeByEndTime = true;
                    continue;
                }
                tmp.add(cdl);
            }
            if (!wasBrokeByEndTime)
                tmp.add(candleClose);
            return tmp;

        } catch (Exception ex)
        {
            Logs.e(TAG, ex.toString());
            return null;
        }
    }
     **/

    public static List<Candle> transform(List<List<String>> sourceList)
    {
        if (sourceList == null)
            return null;
        if (sourceList.size() == 0)
            return null;

        ArrayList<Candle> retList = new ArrayList<>();

        for (List<String> item : sourceList)
        {
            Candle cdl = transformVector(item);
            if (cdl != null)
                retList.add(cdl);
        }

        return retList;
    }

    public static List<Candle> transformWithBorders(List<List<String>> sourceList, Long startTime, Long endTime)
    {
        if (sourceList == null)
            return null;
        if (sourceList.size() == 0)
            return null;

        ArrayList<Candle> retList = new ArrayList<>();

        for (List<String> item : sourceList)
        {
            Candle cdl = transformVector(item);
            if (cdl != null)
            {
                if (startTime != null && cdl.getTime() < startTime.longValue())
                    continue;
                if (endTime != null && cdl.getTime() > endTime.longValue())
                    continue;
                retList.add(cdl);
            }
        }

        return retList;
    }

    public static List<Candle> transformWithBordersAndInterval(List<List<String>> sourceList, Long startTime, Long endTime, String interval)
    {
        if (sourceList == null)
            return null;
        if (sourceList.size() == 0)
            return null;

        ArrayList<Candle> retList = new ArrayList<>();

        for (List<String> item : sourceList)
        {
            List<Candle> list = transformVectorWithInterval(item, interval, startTime, endTime);
            if (list != null)
            {
                retList.addAll(list);
            }
        }

        return retList;
    }

    //ver2
    public static List<Candle> transformWithBordersAndInterval(List<List<String>> sourceList, Long startTime, Long endTime, Interval interval)
    {
        if (sourceList == null)
            return null;
        if (sourceList.size() == 0)
            return null;

        ArrayList<Candle> retList = new ArrayList<>();

        for (List<String> item : sourceList)
        {
            List<Candle> list = transformVectorWithInterval(item, interval, startTime, endTime);
            if (list != null)
            {
                retList.addAll(list);
            }
        }

        return retList;
    }

    /**
     * StreamKline entity transformation to Candle
     */
    public static Candle transform(StreamKline source, long time)
    {
        if (source == null)
            return null;

        Candle cdl = new Candle(source.open, source.high, source.low, source.close, time);

        return cdl;
    }

    /**
     * StreamMarketTickerMini entity transformation to Candle
     */
    public static Candle transform(StreamMarketTickerMini source)
    {
        if (source == null)
            return null;

        Candle cdl = new Candle(source.open, source.high, source.low, source.close, source.eventTime);

        return cdl;
    }
}
