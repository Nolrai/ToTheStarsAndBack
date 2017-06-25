package com.garthskidstuff.shrines.Game;

import android.util.Log;

/**
 * Created by garthupshaw1 on 6/24/17.
 * Singleton wrapper on Log so that it can be mocked
 */
public class Logger {

    private static Logs logs = new AndroidLogs();

    public static void setLogs(Logs logs) {
        Logger.logs = logs;
    }

    public static void d(String tag, String msg) {
        logs.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        logs.i(tag, msg);
    }
}
