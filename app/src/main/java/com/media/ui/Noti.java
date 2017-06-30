package com.media.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import static android.app.Notification.DEFAULT_ALL;

/**
 * Created by prabeer.kochar on 25-02-2017.
 */

public class Noti {
    private static final String YES_ACTION = "YES_ACTION";
    private static final String NO_ACTION = "NO_ACTION";
    Context mContext;

    public Noti(Context context) {
        mContext = context;
    }

    public void addNotification(String Heading,String txt, String camp_id, Bitmap icon) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),R.layout.customnotification);

        // Set Notification Title
        String strtitle = Heading;
        // Set Notification Text
        String strtext = txt;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(Heading);
        builder.setContentText(txt);
        builder.setSmallIcon(R.drawable.power);
        builder.setLargeIcon(icon);
        builder.setAutoCancel(true);
        builder.setDefaults(DEFAULT_ALL);
        builder.setContent(remoteViews);
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.power);
        remoteViews.setImageViewBitmap (R.id.imagenotiright,icon);

        remoteViews.setTextViewText(R.id.title,strtitle);
        remoteViews.setTextViewText(R.id.text,strtext);

        Intent yesReceive = new Intent();
        yesReceive.putExtra("CAMP_ID", camp_id);
        yesReceive.setAction(YES_ACTION);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(mContext, (int) System.currentTimeMillis(), yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action InstAction = new  NotificationCompat.Action.Builder(android.R.color.transparent, "OK", pendingIntentYes).build();
        builder.addAction(InstAction);

        Intent noReceive = new Intent();
        noReceive.putExtra("CAMP_ID", camp_id);
        noReceive.setAction(NO_ACTION);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(mContext, (int) System.currentTimeMillis(), noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action CancAction = new  NotificationCompat.Action.Builder(android.R.color.transparent, "Cancel", pendingIntentNo).build();
        builder.addAction(CancAction);


        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(8935, builder.build());

    }
}
