package com.msgkatz.ratesapp.old.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by msgkatz on 15/08/2018.
 */

public class WorkerThread extends HandlerThread implements Handler.Callback
{
    private Handler handler;
    private static int workerId = 0;

    public WorkerThread() {
        super("ws" + (workerId++));
    }

    public void doRunnable(Runnable runnable) {
        if (handler == null) {
            handler = new Handler(getLooper(), this);
        }
        Message msg = handler.obtainMessage(0, runnable);
        handler.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Runnable runnable = (Runnable) msg.obj;
        runnable.run();
        return true;
    }
}

