package com.media.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by prabeer.kochar on 22-02-2017.
 */


public class ShellExecuter {
    public String Executer(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);

            // DataOutputStream os = new DataOutputStream(p.getOutputStream());

            //os.writeBytes("mount -o remount,rw /system\n");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";

            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = output.toString();
        return response;

    }
}
