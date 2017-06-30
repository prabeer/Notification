package com.media.ui;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

/**
 * Created by prabeer.kochar on 21-02-2017.
 */

public class GetLocation {

    public static int GetLac(Context paramContext) throws Exception {
        {
            GsmCellLocation localGsmCellLocation = (GsmCellLocation) ((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getCellLocation();
            if (isUMTS(paramContext)) {
                if (localGsmCellLocation != null) {
                    return localGsmCellLocation.getLac() & 0xFFFF;
                }
            } else if (localGsmCellLocation != null) {
                int i = localGsmCellLocation.getLac();
                return i;
            }
            return -1;
        }
    }

    public static boolean isUMTS(Context paramContext) {
        switch (((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()) {
            default:
                return false;
            case 8:
                return true;
            case 10:
                return true;
            case 9:
                return true;
            case 3:
                return true;
        }

    }
}
