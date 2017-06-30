package com.media.ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import static com.media.ui.GetIMEI.imi;
import static com.media.ui.Logger.logg;
import static com.media.ui.conf.SERVER;

public class NotiOpt extends BroadcastReceiver {
    public NotiOpt() {
    }
    private static final String YES_ACTION = "YES_ACTION";
    private static final String NO_ACTION = "NO_ACTION";
    private static final String NOTI_ACTION = "NOTI_ACTION";
    private static String IMEI = "";
    private static String server = SERVER;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        String camp_id;
        String url;
        if(extras == null) {
            camp_id= null;
            url = null;
        } else {
            camp_id= extras.getString("CAMP_ID");
            url = extras.getString("URL");
        }

        IMEI = imi(context);

        if(YES_ACTION.equals(action)) {
              new CnfInstall(context).execute("InstReq",camp_id);
            logg("Pressed YES");
          } else if(NO_ACTION.equals(action)) {
                new AsyncPoll(context).execute("Cancel",camp_id);
            logg("Pressed No");
        }else if(NOTI_ACTION.equals(action)){

            Intent intents = new Intent(Intent.ACTION_VIEW);
            intents.setData(Uri.parse(url));
            PendingIntent pi = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intents, 0);
            try {
                pi.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            new AsyncPoll(context).execute("clicked",camp_id);
            logg("Pressed Noti"+"|url:"+url+"|campid:"+camp_id);
        }

    }
}
