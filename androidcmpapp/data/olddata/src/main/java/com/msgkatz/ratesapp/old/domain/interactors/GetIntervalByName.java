package com.msgkatz.ratesapp.old.domain.interactors;

import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.entities.IntervalJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.old.domain.interactors.params.IntervalParams;

import io.reactivex.Observable;

public class GetIntervalByName extends BaseInteractor<Optional<IntervalJava>, IntervalParams> {

    private IDataRepo dataRepo;

    public GetIntervalByName(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<IntervalJava>> buildObservable(IntervalParams parameter) {
        return dataRepo.getIntervalByName(parameter.getInterval());
    }
}