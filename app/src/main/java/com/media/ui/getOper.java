package com.media.ui;

import android.content.Context;
import android.os.Build;
import android.telephony.SubscriptionManager;

/**
 * Created by prabeer.kochar on 03-03-2017.
 */

public class getOper {

    public static String  opp(Context con) {
        int mccSlot1 = -1;
        int mccSlot2 = -1;
        int mncSlot1 = -1;
        int mncSlot2 = -1;

        SubscriptionManager subManager = (SubscriptionManager) con.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (subManager.getActiveSubscriptionInfoCount() >= 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    try {
                        mccSlot1 = subManager.getActiveSubscriptionInfoForSimSlotIndex(0).getMcc();
                    }catch(Exception e){
                        mccSlot1 = -1;
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    try {
                        mncSlot1 = subManager.getActiveSubscriptionInfoForSimSlotIndex(0).getMnc();
                    }catch(Exception e){
                        mncSlot1 = -1;
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (subManager.getActiveSubscriptionInfoCount() >= 2) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    try {
                        mccSlot2 = subManager.getActiveSubscriptionInfoForSimSlotIndex(1).getMcc();
                    }catch (Exception e)
                    {
                        mccSlot2 = -1;
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    try {
                        mncSlot2 = subManager.getActiveSubscriptionInfoForSimSlotIndex(1).getMnc();
                    }catch(Exception e){
                        mncSlot2 = -1;
                    }

                }
            }
        }
        return Integer.toString(mncSlot1)+"|"+Integer.toString(mccSlot1)+"|"+Integer.toString(mccSlot2)+"|"+Integer.toString(mncSlot2);
    }
}
