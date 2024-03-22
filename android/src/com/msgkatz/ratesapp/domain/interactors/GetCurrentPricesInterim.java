package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.params.PriceRealtimeParams;

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
