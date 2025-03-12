package com.msgkatz.ratesapp.feature.common.utils;

import android.util.Log;

/**
 * Created by msgkatz on 07/08/2018.
 */

public class Logs {

    private final static Boolean USE_ONLY_TRACE_LOGS = false;
    private final static String CALLER_TAG = ":::Caller:::";

    public static void d(Object caller, String message)
    {
        String TAG = CALLER_TAG;
        try {
            TAG = caller.getClass().getSimpleName();
        } catch (Exception ex)
        {
            //do nothing
        }
        d(TAG, message);
    }

    public static void d(String TAG, String message)
    {
        if (Parameters.DEBUG)
        {
            Log.d(TAG, message);
        }
    }

    public static void e(Object caller, String message)
    {
        String TAG = CALLER_TAG;
        try {
            TAG = caller.getClass().getSimpleName();
        } catch (Exception ex)
        {
            //do nothing
        }
        e(TAG, message);
    }

    public static void e(String TAG, String message)
    {
        if (Parameters.DEBUG && !USE_ONLY_TRACE_LOGS)
        {
            Log.e(TAG, message);
        }
    }

//    public static void e2(Object caller, String message)
//    {
//        String TAG = CALLER_TAG;
//        try {
//            TAG = caller.getClass().getSimpleName();
//        } catch (Exception ex)
//        {
//            //do nothing
//        }
//        e2(TAG, message);
//    }
//
//    public static void e2(String TAG, String message)
//    {
//        if (Parameters.DEBUG && !USE_ONLY_TRACE_LOGS && !Parameters.DEBUG_LOGS_E2_DISABLE)
//        {
//            Log.e(TAG, message);
//        }
//    }

    public static void s(Object caller, String message)
    {
        if (Parameters.DEBUG)
        {
            String TAG = CALLER_TAG;
            try {
                TAG = caller.getClass().getSimpleName();

            } catch (Exception ex)
            {
                //do nothing
            }

            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

            s(TAG, message, stackTraceElements);
        }
    }

    public static void s(String TAG, String message, StackTraceElement[] stackTraceElements)
    {
        if (Parameters.DEBUG)
        {
            String stackMessage = toString(stackTraceElements);
            Log.e(TAG, message + "\n" + stackMessage);
        }
    }

    private static String toString(StackTraceElement[] stackTraceElements)
    {
        if (stackTraceElements == null)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : stackTraceElements)
            stringBuilder.append(element.toString()).append("\n");
        return stringBuilder.toString();
    }
}

