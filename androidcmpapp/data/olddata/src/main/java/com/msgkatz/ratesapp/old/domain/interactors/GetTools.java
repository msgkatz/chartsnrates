package com.msgkatz.ratesapp.old.domain.interactors;

import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.entities.ToolJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 24/09/2018.
 */

public class GetTools extends BaseInteractor<Optional<Map<String, ToolJava>>, Void> {

    private IDataRepo dataRepo;

    public GetTools(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Map<String, ToolJava>>> buildObservable(Void parameter) {
        return dataRepo.getToolMap();
    }
}
