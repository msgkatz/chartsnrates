package com.msgkatz.ratesapp.presentation.entities.events;

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
