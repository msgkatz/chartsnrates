package com.msgkatz.ratesapp.old.domain.interactors;

import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.entities.PriceSimpleJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 24/08/2018.
 */

public class GetToolListPrices extends BaseInteractor<Optional<Map<String, Set<PriceSimpleJava>>>, Void> {

    private IDataRepo dataRepo;

    public GetToolListPrices(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Map<String, Set<PriceSimpleJava>>>> buildObservable(Void parameter) {
        return dataRepo.getCombinedToolListPrice();
    }
}
