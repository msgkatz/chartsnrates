package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 24/09/2018.
 */

public class GetTools extends BaseInteractor<Optional<Map<String, Tool>>, Void> {

    private IDataRepo dataRepo;

    public GetTools(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Map<String, Tool>>> buildObservable(Void parameter) {
        return dataRepo.getToolMap();
    }
}
