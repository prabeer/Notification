package com.media.ui;

import android.os.Build;

/**
 * Created by prabeer.kochar on 07-07-2017.
 */

public class  GetDeviceName {
        public void getDeviceName() {

    }

    public  static String DeviceDetails(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product =  Build.PRODUCT;
        return product+'|'+model+'|'+manufacturer;
    }
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
