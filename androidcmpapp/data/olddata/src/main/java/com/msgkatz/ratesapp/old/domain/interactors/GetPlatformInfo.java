package com.msgkatz.ratesapp.old.domain.interactors;

import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.entities.PlatformInfoJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import io.reactivex.Observable;


/**
 * Created by msgkatz on 19/08/2018.
 */

public class GetPlatformInfo extends BaseInteractor<Optional<PlatformInfoJava>, Void> {

    private IDataRepo dataRepo;

    public GetPlatformInfo(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<PlatformInfoJava>> buildObservable(Void parameter) {
        return dataRepo.getPlatformInfo();
    }
}
