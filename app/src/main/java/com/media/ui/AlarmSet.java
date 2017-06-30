package com.media.ui;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static com.media.ui.Logger.logg;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmSet extends IntentService {

    private static final String MyPREFERENCES = "alprefs";
    private SharedPreferences sharedPref;

    // Context mcontext;
    public AlarmSet() {
        super("AlarmSet");
    }
    public void onCreate() {
        super.onCreate();
        // this gets called properly
        sharedPref  = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        logg("Service onCreate()");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("flag", 1);
        editor.commit();
        logg("Service Reset!");

        Intent dialogIntent = new Intent(getBaseContext(), PingSource.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startService(dialogIntent);

        logg("Start New Service!");
        stopSelf();
    }

}
