package com.media.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static com.media.ui.Logger.logg;

public class bootComplete extends BroadcastReceiver {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "alprefs" ;
    public bootComplete() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm alarm = new Alarm();
        sharedpreferences =  context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("flag", 1);
            editor.commit();
            alarm.setUpAlarm(context);
            logg( "Alarm Set");
        }
        logg( "Broad Cast Received");
    }
}
