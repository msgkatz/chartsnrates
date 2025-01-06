package com.msgkatz.ratesapp.presentation.entities.events;

import com.msgkatz.ratesapp.domain.entities.IntervalJava;

public class NewIntervalEvent extends BaseEvent {

    private IntervalJava interval;

    public NewIntervalEvent(IntervalJava interval)
    {
        this.interval = interval;
    }

    public IntervalJava getInterval() {
        return interval;
    }
}
