package com.media.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.media.ui.GetLoc.Gloc;
import static com.media.ui.GetLocation.GetLac;
import static com.media.ui.Logger.logg;
import static com.media.ui.getOper.opp;


/**
 * Created by prabeer.kochar on 21-02-2017.
 */

public class PingServer {
    private Context mContext;
    private static String URLVal = "";
    private static String op = "";
    private static String IMEI = "";
    private static int v = 0;
    private static final String MyPREFERENCES = "alprefs";
    private Map<String, String> res = new HashMap<String, String>();
    private SharedPreferences sharedPref;
    private String Mcc;
    private String loc;
    private int cel = 0;


    public PingServer(Context context) {
        mContext = context;
    }


    public Map StartPoll(String URL, String Req) throws Exception {
        sharedPref = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        URLVal = URL;
        op = Req;
        GetIMEI i = new GetIMEI();
        IMEI = i.imi(mContext);
        Mcc = opp(mContext);
        loc = Gloc(mContext);
        cel = GetLac(mContext);
        v = sharedPref.getInt("flag", 0);
        res.put("stat", "pollstart");
        res.put("data", "");
        return Poll();
    }

    private Map Poll() {
        res.put("stat", "");
        res.put("data", "");
        if (v > 0) {
            if (isOnline(mContext)) {
                HttpURLConnection conn = null;
                BufferedReader in = null;
                try {
                    int responseCode = 0;
                    String status = "";
                    String ic = "";
                    JSONObject reader;
                    URL url = new URL(URLVal);

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("IM", IMEI);
                    postDataParams.put("st", op);
                    postDataParams.put("mcc", Mcc);
                    postDataParams.put("loc", loc);
                    postDataParams.put("cel", Integer.toString(cel));
                    logg(postDataParams.toString());
                    String data = postDataParams.toString();

                    conn = (HttpURLConnection) url.openConnection();
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

                    logg("send IMEI Complete");
                    responseCode = conn.getResponseCode();
                    logg(String.valueOf(responseCode));

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        in = new BufferedReader(new
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
                        String pkg = "";
                        reader = new JSONObject(str);
                        status = reader.getString("status");
                        data = reader.getString("data");
                        if (reader.has("camp_id")) {
                            ic = reader.getString("camp_id");
                        }
                        if (reader.has("pkg")) {
                            pkg = reader.getString("pkg");
                        }
                        logg("Stat:" + status);
                        res.put("stat", status);
                        res.put("data", data);
                        res.put("ic", ic);
                        res.put("pkg", pkg);

                    } else {
                        Log.d("cmds", "doInBackground:" + responseCode);

                        res.put("stat", new String("false : " + responseCode));
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } else {
                res.put("stat", "NoNetwork");
                res.put("data", "");
            }
            return res;
        } else {
            res.put("stat", "PollCancel");
            res.put("data", "");
        }
        return res;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)
                        context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null &&
                networkInfo.isConnected());
    }
}
