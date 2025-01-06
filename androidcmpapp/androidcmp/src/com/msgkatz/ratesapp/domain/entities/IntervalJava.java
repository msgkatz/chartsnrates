package com.msgkatz.ratesapp.domain.entities;

/**
 *
 * m -> minutes; h -> hours; d -> days; w -> weeks; M -> months

     1m
     3m
     5m
     15m
     30m
     1h
     2h
     4h
     6h
     8h
     12h
     1d
     3d
     1w
     1M

 *
 * Created by msgkatz on 15/08/2018.
 */

public class IntervalJava {

    private int id;
    private int type;
    private String symbol;
    private String symbolApi;

    private long perItemDefaultMs;
    private long perBlockDefaultMs;

    private long perItemMinMs;
    private long perBlockMinMs;

    private long perItemMaxMs;
    private long perBlockMaxMs;

    private int inUse;

    private boolean selected = false;

    public IntervalJava(int id, int type,
                        String symbol, String symbolApi,
                        long perItemDefaultMs, long perBlockDefaultMs,
                        long perItemMinMs, long perBlockMinMs,
                        long perItemMaxMs, long perBlockMaxMs,
                        int inUse)
    {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.symbolApi = symbolApi;
        this.perItemDefaultMs = perItemDefaultMs;
        this.perBlockDefaultMs = perBlockDefaultMs;
        this.perItemMinMs = perItemMinMs;
        this.perBlockMinMs = perBlockMinMs;
        this.perItemMaxMs = perItemMaxMs;
        this.perBlockMaxMs = perBlockMaxMs;
        this.inUse = inUse;

    }

    public IntervalJava(String value) {
        this.symbol = value;
    }

    public int getType() {
        return type;
    }

    public String getValue() {

        return symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSymbolApi() {
        return symbolApi;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public static IntervalJava fromString(String s)
    {
        if (s == null)
            return null;

        return new IntervalJava(s);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPerItemDefaultMs() {
        return perItemDefaultMs;
    }

    public void setPerItemDefaultMs(long perItemDefaultMs) {
        this.perItemDefaultMs = perItemDefaultMs;
    }

    public long getPerBlockDefaultMs() {
        return perBlockDefaultMs;
    }

    public void setPerBlockDefaultMs(long perBlockDefaultMs) {
        this.perBlockDefaultMs = perBlockDefaultMs;
    }

    public int getInUse() {
        return inUse;
    }

    public void setInUse(int inUse) {
        this.inUse = inUse;
    }
}
