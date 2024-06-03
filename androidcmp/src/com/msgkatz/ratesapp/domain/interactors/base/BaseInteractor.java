package com.msgkatz.ratesapp.domain.interactors.base;

import com.msgkatz.ratesapp.utils.PreconditionsUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Base domain Use Case entity
 *
 * Created by msgkatz on 19/08/2018.
 */

public abstract class BaseInteractor<T, ParameterType> {

    private final CompositeDisposable disposables;

    public BaseInteractor()
    {
        this.disposables = new CompositeDisposable();
    }

    /** Example: BehaviorRelay<Object> relay = BehaviorRelay.createDefault("default"); **/
    protected abstract Observable<T> buildObservable(final ParameterType parameter);

    //protected abstract Type getResponseType();

    /**
     * Executes the current Interactor.
     *
     * @param observer {@link DisposableObserver} which will be listening to the observable build
     * by {@link #buildObservable(ParameterType)} ()} method.
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    public void execute(DisposableObserver<T> observer, ParameterType params) {


        final Observable<T> observable = this.buildObservable(params);

        if (observer != null)
        {
            Disposable disposable = observable
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            addDisposable(disposable);
        }


    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        PreconditionsUtil.checkNotNull(disposable);
        PreconditionsUtil.checkNotNull(disposables);
        disposables.add(disposable);
    }

}