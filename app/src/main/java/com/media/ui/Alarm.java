package com.media.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import static com.media.ui.Logger.logg;
import static com.media.ui.bootComplete.MyPREFERENCES;
import static com.media.ui.conf.AlarmDelay;

/**
 * Created by prabeer.kochar on 21-02-2017.
 */

public class Alarm {
    SharedPreferences sharedpreferences;
    public void setUpAlarm(Context context) {
       logg("setUpAlarm: Alarm1");
        sharedpreferences =  context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("flag", 1);
        editor.commit();
        Intent intent = new Intent(context, AlarmSet.class);
        PendingIntent pending_intent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarm_mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm_mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), AlarmDelay, pending_intent);
    }
}
