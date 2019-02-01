package com.msgkatz.ratesapp.domain.interactors;


import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 29/08/2018.
 */

public class GetQuoteAssets extends BaseInteractor<Optional<Set<Asset>>, Void> {

    private IDataRepo dataRepo;

    public GetQuoteAssets(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Set<Asset>>> buildObservable(Void parameter) {
        return dataRepo.getQuoteAssets();
    }
}
