package com.msgkatz.ratesapp.old.domain.interactors;

import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 28/08/2018.
 */

public class GetAssets extends BaseInteractor<Optional<Map<String, AssetDT>>, Void> {

    private IDataRepo dataRepo;

    public GetAssets(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Map<String, AssetDT>>> buildObservable(Void parameter) {
        return dataRepo.getAssets();
    }
}
