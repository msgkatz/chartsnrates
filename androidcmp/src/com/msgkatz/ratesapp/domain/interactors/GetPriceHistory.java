package com.msgkatz.ratesapp.domain.interactors;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.domain.IDataRepo;
import com.msgkatz.ratesapp.domain.interactors.base.BaseInteractor;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.params.PriceHistoryParams;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 23/08/2018.
 */

public class GetPriceHistory extends BaseInteractor<Optional<List<Candle>>, PriceHistoryParams> {

    private IDataRepo dataRepo;

    public GetPriceHistory(IDataRepo repo)
    {
        super();
        this.dataRepo = repo;
    }

    @Override
    protected Observable<Optional<List<Candle>>> buildObservable(PriceHistoryParams parameter) {
        return dataRepo.getPriceHistoryByTool(parameter.getSymbol(),parameter.getInterval(),
                parameter.getStartTime(), parameter.getEndTime(), parameter.getLimit());
    }
}
