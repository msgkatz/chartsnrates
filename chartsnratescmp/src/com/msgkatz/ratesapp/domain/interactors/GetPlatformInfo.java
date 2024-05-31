package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import io.reactivex.Observable;


/**
 * Created by msgkatz on 19/08/2018.
 */

public class GetPlatformInfo extends BaseInteractor<Optional<PlatformInfo>, Void> {

    private IDataRepo dataRepo;

    public GetPlatformInfo(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<PlatformInfo>> buildObservable(Void parameter) {
        return dataRepo.getPlatformInfo();
    }
}
