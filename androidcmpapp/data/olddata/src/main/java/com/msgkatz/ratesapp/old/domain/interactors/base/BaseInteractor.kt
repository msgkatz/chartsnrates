package com.msgkatz.ratesapp.old.domain.interactors.base

import com.msgkatz.ratesapp.old.utils.PreconditionsUtil
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.atomic.AtomicReference

/**
 * Base domain Use Case entity
 *
 * Created by msgkatz on 19/08/2018.
 */
abstract class BaseInteractor<T: Any?, ParameterType> {
    private val disposables = CompositeDisposable()

    /** Example: BehaviorRelay<Object> relay = BehaviorRelay.createDefault("default"); </Object> */
    protected abstract fun buildObservable(parameter: ParameterType): Observable<T>

    //protected abstract Type getResponseType();
    /**
     * Executes the current Interactor.
     *
     * @param observer [DisposableObserver] which will be listening to the observable build
     * by [.buildObservable] ()} method.
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    fun execute(observer: DisposableObserver<T>?, params: ParameterType) {
        val observable = this.buildObservable(params)

        if (observer != null) {
            val disposable: Disposable = observable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
            addDisposable(disposable)
        }
    }

    fun getAsFlow(params: ParameterType): Flow<T?> {
        val observable: ObservableSource<T> = this.buildObservable(params)

        return observable.asFlow()
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    private fun addDisposable(disposable: Disposable) {
        PreconditionsUtil.checkNotNull(disposable)
        PreconditionsUtil.checkNotNull(disposables)
        disposables.add(disposable)
    }
}

/**
 * Transforms given cold [ObservableSource] into cold [Flow].
 *
 * The resulting flow is _cold_, which means that [ObservableSource.subscribe] is called every time a terminal operator
 * is applied to the resulting flow.
 *
 * A channel with the [default][Channel.BUFFERED] buffer size is used. Use the [buffer] operator on the
 * resulting flow to specify a user-defined value and to control what happens when data is produced faster
 * than consumed, i.e. to control the back-pressure behavior. Check [callbackFlow] for more details.
 */
public fun <T: Any?> ObservableSource<T>.asFlow(): Flow<T?> = callbackFlow {
    val disposableRef = AtomicReference<Disposable>()
    val observer = object : Observer<T?> {
        override fun onComplete() { close() }
        override fun onSubscribe(d: Disposable) { if (!disposableRef.compareAndSet(null, d)) d.dispose() }
        override fun onNext(t: T & Any) {
            /*
             * Channel was closed by the downstream, so the exception (if any)
             * also was handled by the same downstream
             */
            try {
                trySendBlocking(t)
            } catch (e: InterruptedException) {
                // RxJava interrupts the source
            }
        }
        override fun onError(e: Throwable) { close(e) }
    }

        //.subscribeOn(Schedulers.computation())
//    withContext(Dispatchers.Default) {
//        subscribe(observer)
//    }
    subscribe(observer)
    awaitClose { disposableRef.getAndSet(Disposables.disposed())?.dispose() }
}
