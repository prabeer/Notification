package com.media.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.media.ui.GetIMEI.imi;
import static com.media.ui.Logger.logg;
import static com.media.ui.PollRes.poll;
import static com.media.ui.conf.AppFolder;
import static com.media.ui.conf.SERVER;

/**
 * Created by prabeer.kochar on 07-03-2017.
 */

class CnfInstall extends AsyncTask<Object, Object, Boolean> {

    private static String IMEI = "";
    private static String server = SERVER;
    private Context mContext;
    private int insyes = 0;
    private String camp_id;
    private String op = "Inst";
    String inte = "";
    InstallApp ins;
    SharedPreferences sharedpreferences;
    public static final String pakage = "MyPrefs";

    public CnfInstall(Context context) {
        mContext = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        IMEI = imi(mContext);
        ins = new InstallApp(mContext);
        //get sharedPreferences here
        // showNotification("App Install started","Please Wait");
        NotificationManager notificationmanager = (NotificationManager) mContext
                .getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(8935);
    }

    @Override
    protected Boolean doInBackground(Object... arg0) {
        try {
            logg("inst start");
            int responseCode = 0;
            String status = "";

            JSONObject reader;
            URL url = new URL(server);
            camp_id = (String) arg0[1];
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("IM", IMEI);
            postDataParams.put("st", op);
            postDataParams.put("ci", camp_id);
            logg(postDataParams.toString());
            String data = postDataParams.toString();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);

            writer.flush();
            writer.close();

            logg("Inst IMEI Complete");
            responseCode = conn.getResponseCode();
            logg(String.valueOf(responseCode));

            if (responseCode == HttpsURLConnection.HTTP_OK) {


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    // Log.d("cmds", String.valueOf(line));
                    sb.append(line);
                    // break;
                }

                in.close();
                logg("Response:" + sb.toString());
                String str = sb.toString();
                reader = new JSONObject(str);
                status = reader.getString("status");
                data = reader.getString("data");
                inte = reader.getString("int");
                if (status.equals("AskIns")) {
                    String ur = data;
                    DownloadFile dl = new DownloadFile();
                    if (dl.DownloadFiles(ur)) {

                        String dir = Environment.getExternalStorageDirectory().toString();
                        String loc = dir + ("/" + AppFolder + "/app.apk");
                        String pkg = inte;
                        if (ins.install(loc, pkg)) {
                            logg("Install Complete");
                            dl.deleteDir();
                            insyes = 1;
                            sharedpreferences = mContext.getSharedPreferences(pakage, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("pkg", pkg);
                            editor.putString("camp_id", camp_id);
                            editor.putString("ins_type", "askins");
                            editor.commit();
                        } else {
                            logg("install failed");
                            dl.deleteDir();
                            //  showNotification("Install Failed","Try Later");
                            if (poll(SERVER, "AskInf", imi(mContext), camp_id)) {
                                insyes = 0;
                                logg("Ack Complete");
                            }
                        }
                    } else {
                        logg("download failed");
                        dl.deleteDir();
                        // showNotification("Check Internet Connection","Try Later");
                        if (poll(SERVER, "AskInd", imi(mContext), camp_id)) {
                            insyes = 0;
                            logg("Ack Complete");
                        }
                    }
                }
                logg("Stat:" + status);
                return true;

            } else {
                logg("doInBackground:" + responseCode);

                return false;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        if (insyes == 1) {
            logg("post pass");


            //showNotification("Install Complete", "Try the new app");
        } else {
            logg("post fail");
            // showNotification("Install Failed", "Try Next time");
        }
    }

    private void showNotification(String h, String d) {
        try {
            NotificationManager notificationmanager = (NotificationManager) mContext
                    .getSystemService(NOTIFICATION_SERVICE);
            notificationmanager.cancel(8935);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
            builder.setContentTitle(h);
            builder.setContentText(d);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setAutoCancel(true);
            //builder.setDefaults(DEFAULT_ALL);
            NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(8935, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
