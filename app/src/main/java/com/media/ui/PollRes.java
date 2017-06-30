package com.media.ui;

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

import static com.media.ui.Logger.logg;

/**
 * Created by prabeer.kochar on 22-02-2017.
 */

public final class PollRes {

    public static boolean poll(String server, String op, String IMEI, String camp_id){

            try {
                int responseCode = 0;
                String status = "";
                JSONObject reader;
                URL url = new URL(server);

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

                logg( "ACk IMEI Complete");
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
                    if((str == null)||(str == "")) {
                        reader = new JSONObject(str);
                        status = reader.getString("status");
                        data = reader.getString("data");
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
    }

