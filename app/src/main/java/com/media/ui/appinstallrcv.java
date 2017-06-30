package com.media.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.NotificationCompat;

import static android.app.Notification.DEFAULT_ALL;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.media.ui.CnfInstall.pakage;
import static com.media.ui.Logger.logg;

public class appinstallrcv extends BroadcastReceiver {
    public appinstallrcv() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionStr = intent.getAction();
        try {
            if (Intent.ACTION_PACKAGE_ADDED.equals(actionStr)) {
                String packageName = intent.getData().getEncodedSchemeSpecificPart();
                SharedPreferences sharedpreferences = context.getSharedPreferences(pakage, Context.MODE_PRIVATE);
                if (sharedpreferences.contains("pkg")) {
                    String pak = sharedpreferences.getString("pkg", null);
                    String camp_id = sharedpreferences.getString("camp_id", null);
                    String ins_type = sharedpreferences.getString("ins_type", null);

                    logg("Shared-pkg:" + pak);
                    if (ins_type.equals("askins")) {
                        if (pak.equals(packageName)) {
                            logg("Package Found");
                            createMyNotification("Install Complete", "Try the new app", packageName, context);
                            AsyncPoll a = new AsyncPoll(context);
                            a.execute("AskInc", camp_id);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove("pkg");
                            editor.remove("camp_id");
                            editor.remove("ins_type");
                            editor.commit();
                        }
                        logg("pkg:" + packageName);
                    } else if (ins_type.equals("frcins")) {
                        if (pak.equals(packageName)) {
                            logg("Package Found");
                            AsyncPoll a = new AsyncPoll(context);
                            a.execute("Inc", camp_id);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove("pkg");
                            editor.remove("camp_id");
                            editor.remove("ins_type");
                            editor.commit();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createMyNotification(String titl, String titlcont, String apppack, Context mContext) {

        PackageManager pm = mContext.getPackageManager();
        Intent LaunchIntent = null;
        String name = "";
        String stat = "";
        logg("creating Notification");
        try {
            if (pm != null) {
                logg("launch Noti:" + apppack);
                ApplicationInfo app = mContext.getPackageManager().getApplicationInfo(apppack, 0);
                name = (String) pm.getApplicationLabel(app);
                LaunchIntent = pm.getLaunchIntentForPackage(apppack);
            } else {
                logg("fail Noti");
                stat = "fl";

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            stat = "exc";
            logg("Launch Excep");
        }
        if (stat.equals("")) {
            logg("stats:" + stat);
            try {
                // NotificationManager notificationmanager = (NotificationManager) mContext
                //        .getSystemService(NOTIFICATION_SERVICE);
                // notificationmanager.cancel(8935);

                Intent intent = LaunchIntent; // new Intent();
                PendingIntent pIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
                builder.setContentTitle(titl);
                builder.setContentText(titlcont);
                builder.setSmallIcon(R.drawable.power);
                builder.setAutoCancel(true);
                builder.setDefaults(DEFAULT_ALL);
                builder.setContentIntent(pIntent);

                NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                manager.notify(8945, builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
            logg("stats:" + stat);
        } else {
            logg("stats:" + stat);
        }
    }

}
