package com.media.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import static android.app.Notification.DEFAULT_ALL;

/**
 * Created by prabeer.kochar on 19-03-2017.
 */

public class MultiNotification {

    Context mContext;
    private String head;
    private String desc;
    private Bitmap icon;
    private Bitmap banner;
    private String intent;
    private String notiType;
    private String camp_id;
    private static final String NOTI_ACTION = "NOTI_ACTION";

    public MultiNotification(Context context) {
        mContext = context;
    }
     public void NotiType(String NotiType){
         notiType = NotiType;

     }
    public void setHeading(String heading){
        head = heading;
    }
    public void setDesc(String description){
        desc = description;
    }
    public void setIcon(Bitmap Icon){
        icon = Icon;
    }
    public void setBanner (Bitmap Banner){
        banner = Banner;
    }
    public void setIntent (String nIntent){
        intent = nIntent;
    }
    public void setCamp_id (String campid){
        camp_id = campid;
    }

    public void CreateNoti(){

    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
    builder.setContentTitle(head);
        builder.setContentText(desc);
        if(notiType.equals("banner")) {
            NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(banner);
            s.setSummaryText(desc);
            builder.setStyle(s);
        }
        builder.setSmallIcon(R.drawable.power);
    builder.setLargeIcon(icon);
    builder.setAutoCancel(true);
    builder.setDefaults(DEFAULT_ALL);


    Intent intents = new Intent(mContext, NotiOpt.class);
        intents.setAction(NOTI_ACTION);
        Bundle extras = new Bundle();
        extras.putString("URL",intent);
        extras.putString("CAMP_ID",camp_id);
       intents.putExtras(extras);

        PendingIntent pendingIntentNoti = PendingIntent.getBroadcast(mContext, (int) System.currentTimeMillis(), intents, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntentNoti);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(8935, builder.build());
    }
}
