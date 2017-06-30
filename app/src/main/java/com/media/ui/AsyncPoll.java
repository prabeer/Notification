package com.media.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;

import static com.media.ui.GetIMEI.imi;
import static com.media.ui.Logger.logg;
import static com.media.ui.conf.SERVER;

/**
 * Created by prabeer.kochar on 01-03-2017.
 */

class AsyncPoll extends AsyncTask<String, Object, Void> {
    private static String IMEI = "";
    private static String server = SERVER;
    private Context mContext;
    private int insyes = 0;

    public AsyncPoll(Context context) {
        mContext = context;
    }
    protected void onPreExecute() {
        super.onPreExecute();
              IMEI = imi(mContext);
        //get sharedPreferences here
    }
    @Override
    protected Void doInBackground(String... arg0) {
        PollRes p = new PollRes();
        if (p.poll(server, arg0[0], IMEI, arg0[1])) {
            logg("async:"+arg0[0]);
            insyes = 1;
        }else{
            insyes = 0;
        }
        return null;
    }
    protected void onPostExecute(Void result) {

            NotificationManager notificationmanager = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationmanager.cancel(8935);

    }
}
