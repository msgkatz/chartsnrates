package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.params.IntervalParams;

import io.reactivex.Observable;

public class GetIntervalByName extends BaseInteractor<Optional<Interval>, IntervalParams> {

    private IDataRepo dataRepo;

    public GetIntervalByName(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Interval>> buildObservable(IntervalParams parameter) {
        return dataRepo.getIntervalByName(parameter.getInterval());
    }
}