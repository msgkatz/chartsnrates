package com.msgkatz.ratesapp.domain.interactors;


import com.msgkatz.ratesapp.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 29/08/2018.
 */

public class GetQuoteAssets extends BaseInteractor<Optional<Set<AssetDT>>, Void> {

    private IDataRepo dataRepo;

    public GetQuoteAssets(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Set<AssetDT>>> buildObservable(Void parameter) {
        return dataRepo.getQuoteAssets();
    }
}
