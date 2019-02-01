package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.data.entities.rest.PriceSimpleDT;
import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 24/08/2018.
 */

public class GetToolListPrices extends BaseInteractor<Optional<Map<String, Set<PriceSimple>>>, Void> {

    private IDataRepo dataRepo;

    public GetToolListPrices(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Map<String, Set<PriceSimple>>>> buildObservable(Void parameter) {
        return dataRepo.getCombinedToolListPrice();
    }
}
