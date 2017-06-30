package com.media.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.media.ui.Logger.logg;

public class networkAvailable extends BroadcastReceiver {
    public networkAvailable() {
    }
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "alprefs" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            int v = sharedpreferences.getInt("flag", 0);
            logg(Integer.toString(v));
            if (v == 1) {
                logg("request sent");
                if (isOnline(context)) {
                    intent = new Intent(context, AlarmSet.class);
                    context.startService(intent);
                } else {
                    logg("user offline");
                }
            } else {
                logg("request cancel");
            }
            logg("Network Received");
        }
    }
    private boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null &&
                networkInfo.isConnected());
    }

}
