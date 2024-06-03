package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.List;

import io.reactivex.Observable;

public class GetIntervals extends BaseInteractor<Optional<List<Interval>>, Void> {

    private IDataRepo dataRepo;

    public GetIntervals(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<List<Interval>>> buildObservable(Void parameter) {
        return dataRepo.getIntervalList();
    }
}
