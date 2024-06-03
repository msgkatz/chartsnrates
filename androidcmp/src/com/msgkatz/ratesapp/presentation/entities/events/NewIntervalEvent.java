package com.msgkatz.ratesapp.presentation.entities.events;

import com.msgkatz.ratesapp.domain.entities.Interval;

public class NewIntervalEvent extends BaseEvent {

    private Interval interval;

    public NewIntervalEvent(Interval interval)
    {
        this.interval = interval;
    }

    public Interval getInterval() {
        return interval;
    }
}
