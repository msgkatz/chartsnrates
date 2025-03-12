package com.msgkatz.ratesapp.old.domain.interactors;

import com.msgkatz.ratesapp.old.data.entities.Candle;
import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.old.domain.interactors.params.PriceParams;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 23/08/2018.
 */

public class GetCurrentPrice extends BaseInteractor<Optional<Candle>, PriceParams> {

    private IDataRepo dataRepo;

    public GetCurrentPrice(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<Candle>> buildObservable(PriceParams parameter) {
//        return dataRepo.getCurrentPriceByToolCombo(parameter.getSymbol(),parameter.getInterval(),
//                parameter.getStartTime());
        return dataRepo.getCurrentPriceByTool(parameter.getSymbol(),parameter.getInterval(),
                parameter.getStartTime());
    }
}