package com.msgkatz.ratesapp.old.domain.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute the
 * {@link .BaseUseCase} out of the UI thread.
 */
public interface ThreadExecutor extends Executor
{
    public ScheduledFuture<?> schedule(Runnable command,
                                       long delay, TimeUnit unit);
}