package com.media.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.media.ui.Logger.logg;

/**
 * Created by prabeer.kochar on 10-04-2017.
 */

public class PackageList {
Context mcontext;
    public PackageList(Context context){
mcontext = context;
    }
    public String getPkglist() throws PackageManager.NameNotFoundException, JSONException {
        String str = "";
        final PackageManager pm = mcontext.getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
String ver = "0";
        String verno = "0";
        int i = 0;
JSONObject fin = new JSONObject();
        for (ApplicationInfo packageInfo : packages) {
            try {
                PackageInfo pInfo = mcontext.getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_META_DATA);
                ver = pInfo.versionName;
                verno = String.valueOf(pInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            JSONObject jo = new JSONObject();
            jo.put("pkg",packageInfo.packageName);
            jo.put("vrn",ver);
            jo.put("vno", verno);
            String is = String.valueOf(i);
            fin.put("a"+is, jo);
            i++;

           // logg("Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
        logg("finalstr:"+fin.toString());

return fin.toString();
    }
}
