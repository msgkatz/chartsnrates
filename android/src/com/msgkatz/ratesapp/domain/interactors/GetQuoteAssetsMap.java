package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 10/09/2018.
 */

public class GetQuoteAssetsMap extends BaseInteractor<Optional<Map<String, Asset>>, Void> {

    private IDataRepo dataRepo;

    public GetQuoteAssetsMap(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Map<String, Asset>>> buildObservable(Void parameter) {
        return dataRepo.getQuoteAssetsAsMap();
    }
}
