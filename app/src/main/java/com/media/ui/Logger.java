package com.media.ui;

import android.util.Log;

import static com.media.ui.conf.LogEnable;

/**
 * Created by prabeer.kochar on 22-02-2017.
 */

public class Logger {
    public  Logger(){

    }
    private static String  TAG = "cmds";
    private static boolean enable = LogEnable;
    public static void logg(String val){
        if(enable) {
            Log.d(TAG, "btt: " + val);
        }
    }
}
