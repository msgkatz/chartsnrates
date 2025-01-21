package com.msgkatz.ratesapp.old.domain.interactors;

import com.msgkatz.ratesapp.old.data.entities.Candle;
import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.old.domain.interactors.params.PriceRealtimeParams;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 18/09/2018.
 */

public class GetCurrentPricesInterim extends BaseInteractor<Optional<List<Candle>>, PriceRealtimeParams> {

    private IDataRepo dataRepo;

    public GetCurrentPricesInterim(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<List<Candle>>> buildObservable(PriceRealtimeParams parameter) {
        return dataRepo.getCurrentPricesInterimByTool(parameter.getSymbol(),parameter.getInterval(),
                parameter.getStartTime());
    }
}
