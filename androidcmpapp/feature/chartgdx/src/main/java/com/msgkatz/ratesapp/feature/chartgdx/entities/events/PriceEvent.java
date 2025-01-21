package com.msgkatz.ratesapp.feature.chartgdx.entities.events;

public class PriceEvent extends BaseEvent {

    private double price;

    public PriceEvent(double price)
    {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
