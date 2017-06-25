package com.garthskidstuff.shrines.Game;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by garthupshaw1 on 6/24/17.
 *
 */

class AndroidLogs implements Logs {
    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);

        // open a file on external storage and write this
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "GarthsKidStuff/Shrines");
        if (!dir.mkdirs()) {
            Log.e(tag, "Directory not created");
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(dir.getAbsolutePath() +"/log.txt", true));
            bw.write(tag + ": " + msg);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            Log.e(tag, e.getMessage());
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    Log.e(tag, e.getMessage());
                }
            }
        }

    }
}
