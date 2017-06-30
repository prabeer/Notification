package com.media.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.media.ui.Logger.logg;

/**
 * Created by prabeer.kochar on 22-02-2017.
 */

public class InstallApp {

    private Context mcontext;
    public static final String ACTION_INSTALL_COMPLETE = "com.media.ui.ACTION_INSTALL_COMMIT";

    public InstallApp(Context context) {
        mcontext = context;
    }

    public boolean install(String loc, String pkg) {
        //logg("Package:- " + pkg);
        if (!pkg.isEmpty()) {
            InputStream i = null;
            File f = new File(loc);
            try {
                i = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (f.exists()) {

                try {
                    installPackage(mcontext, i, pkg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logg("Cmd: Ins From p");

                    return true;

            } else {
                return false;
            }
        } else {
            if (isPackageExisted(pkg)) {
                logg("pkg exists");
                return false;
            } else {
                InputStream i = null;
                File f = new File(loc);
                try {
                    i = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (f.exists()) {

                    logg("Cmd: Ins From p");
                    try {
                        installPackage(mcontext, i, pkg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isPackageExisted(pkg)) {
                        return true;
                    }
                } else {
                    return false;
                }

            }
        }
        return false;
    }

    public boolean isPackageExisted(String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = mcontext.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
           // logg(packageInfo.packageName + "\n");
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    public static boolean installPackage(Context context, InputStream in, String packageName)
            throws IOException {


        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        params.setAppPackageName(packageName);
      //  logg("extra: " + Intent.EXTRA_REFERRER);
        params.setReferrerUri(Uri.parse(Intent.EXTRA_REFERRER));
        params.setOriginatingUri(Uri.parse(Intent.EXTRA_ORIGINATING_URI));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            params.setOriginatingUid(1000);
        }
        // set params
        int sessionId = packageInstaller.createSession(params);
        PackageInstaller.Session session = packageInstaller.openSession(sessionId);
        OutputStream out = session.openWrite("COSU", 0, -1);
        byte[] buffer = new byte[65536];
        int c;
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
        session.fsync(out);
        in.close();
        out.close();

        session.commit(createIntentSender(context, sessionId));
        return true;
    }

    private static IntentSender createIntentSender(Context context, int sessionId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                new Intent(ACTION_INSTALL_COMPLETE),
                0);
        return pendingIntent.getIntentSender();
    }
}
