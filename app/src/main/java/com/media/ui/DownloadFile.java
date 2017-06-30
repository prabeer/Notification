package com.media.ui;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.media.ui.Logger.logg;
import static com.media.ui.conf.AppFolder;

public class DownloadFile {
    private String loc = AppFolder + "\\app.apk";
    private File dir = new File(AppFolder);

    public boolean DownloadFiles(String ur, String loc) {

        try {
            URL url = new URL(ur);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();
            logg("File Download Start");
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                logg("Check if folder exists");
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, loc);
                logg("Folder:" + loc);
                if (!file.exists()) {
                    logg("Create Folder");
                    String[] path = loc.split("\\\\");
                    int l = path.length;
                    String p = "";
                    for (int i = 0; i < l - 1; i++) {
                        p += path[i];
                    }
                    logg("Folder:" + p);
                    logg("apk:" + path[l - 1]);
                    File dr = new File(sdcard, p);
                    dir = dr;
                    for (int x = 0; x < 4; x++) {
                        if (dr.mkdirs()) {
                            logg("Dir Created Dl Start");
                            File app = new File(sdcard + "/" + p, "/" + path[l - 1]);
                            FileOutputStream fileOutput = new FileOutputStream(app);
                            InputStream inputStream = conn.getInputStream();
                            int totalSize = conn.getContentLength();
                            int downloadedSize = 0;
                            byte[] buffer = new byte[1024];
                            int bufferLength = 0;
                            while ((bufferLength = inputStream.read(buffer)) > 0) {
                                if (downloadedSize == 0) {
                                    logg("dl:Start");
                                }
                                //add the data in the buffer to the file in the file output stream (the file on the sd card
                                fileOutput.write(buffer, 0, bufferLength);
                                //add up the size so we know how much is downloaded
                                downloadedSize += bufferLength;
                                //this is where you would do something to report the prgress, like this maybe
                                //logg("dlSize:"+downloadedSize);
                            }
                            if (downloadedSize == totalSize) {
                                fileOutput.close();
                                return true;
                            }
                            logg("dlSize:" + downloadedSize);
                            fileOutput.close();
                        } else {
                            logg(new String("failed to create dir : " + responseCode));
                            deleteDir();
                            if (dr.mkdirs()) {
                                File app = new File(sdcard + "/" + p, "/" + path[l - 1]);
                                FileOutputStream fileOutput = new FileOutputStream(app);
                                InputStream inputStream = conn.getInputStream();
                                int totalSize = conn.getContentLength();
                                int downloadedSize = 0;
                                byte[] buffer = new byte[1024];
                                int bufferLength = 0;
                                while ((bufferLength = inputStream.read(buffer)) > 0) {
                                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                                    fileOutput.write(buffer, 0, bufferLength);
                                    //add up the size so we know how much is downloaded
                                    downloadedSize += bufferLength;
                                    //this is where you would do something to report the prgress, like this maybe
                                    logg("dlSize:" + downloadedSize);
                                }
                                if (downloadedSize == totalSize) {
                                    fileOutput.close();
                                    return true;
                                }
                                fileOutput.close();
                            } else {
                                logg(new String("failed to create dir complete :"));
                                return false;
                            }
                        }
                    }
                    logg(new String("Unable to Download"));
                    return false;
                }
            } else {
                return false;
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean DownloadFiles(String ur) {
        return DownloadFiles(ur, loc);
    }

    public boolean deleteDir() {
        return deleteDirectory(dir);
    }

    private boolean deleteDirectory(File dir) {

        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }

        // either file or an empty directory
        //System.out.println("removing file or directory : " + dir.getName());
        return dir.delete();
    }

}