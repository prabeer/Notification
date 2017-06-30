package com.media.ui;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by prabeer.kochar on 21-02-2017.
 */

public final class GetIMEI {
    public static String imi(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return String.valueOf(tm.getDeviceId());
        }catch(Exception e){
            return "99999999";
        }
    }
}
