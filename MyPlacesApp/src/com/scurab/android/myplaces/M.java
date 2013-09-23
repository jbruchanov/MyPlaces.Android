package com.scurab.android.myplaces;

import android.content.Context;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class M {
    public final static double COORD_HELP_MAPPER = 1E6;

    public static final class Defaults {

        public static final String PROPERTY_SERVER_URL = "http://myplaces.scurab.com:8182";
//		public static final String PROPERTY_SERVER_URL = "http://192.168.100.11:8182";

    }

    public static final class Constants {
        public static final int DIALOG_EDITTEXT_ID = 0x91237489;
        public static final String MAP_ITEM = "MAP_ITEM";

        public static final int RESULT_ADD = 0xDD;
        public static final int RESULT_DELETE = 0xDE;
        public static final int RESULT_UPDATE = 0xDF;
        public static final String NEW_MAP_ITEM = "NEW_MAP_ITEM";
        public static final int REQUEST_EDIT_MAP_ITEM = 0xEAE;
    }

    private M() {
    }

    public static String handleUncoughtError(Thread t, Throwable ex, Context context) {
        MyPrintWriter pw = null;
        try {
            File f = context.getFileStreamPath(String.format("/sdcard/ERR_MyMplaces.%s.txt", System.currentTimeMillis()));
//				File f = new File(LOCAL_ERROR_FILE);
            if (f.exists()) {
                f.delete();
            }
            pw = new MyPrintWriter(f.getAbsolutePath());
            int[] display = new int[2];
            int rotation = context.getResources().getConfiguration().orientation;
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            display[0] = dm.widthPixels;
            display[1] = dm.heightPixels;

            pw.println("Time:" + Calendar.getInstance().getTime().toLocaleString());
            pw.println(String.format("Display X:%s, Y:%S, Orientation:%s", display[0], display[1], rotation));
            pw.println(String.format("API-LEVEL:%s", android.os.Build.VERSION.SDK_INT));
            pw.println(String.format("Model:%s", android.os.Build.MODEL));
            pw.println("ExceptionClass:" + ex.getClass().getName());
            pw.println("Message:" + ex.getMessage());
            pw.println("Stack:");
            ex.printStackTrace(pw);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            return "handleUncoughtError ERROR\n" + e.getMessage();
        }

        return pw.getWrittenData();
    }

    public static class MyPrintWriter extends PrintWriter {
        StringBuilder sb = new StringBuilder();
        private String file;

        @Override
        public void println(String str) {
            if (file != null) {
                super.println(str);
            }
            sb.append(str + "\n");
        }

        @Override
        public void write(char[] buf, int offset, int count) {
            if (file != null) {
                super.write(buf, offset, count);
            }
            sb.append(String.copyValueOf(buf, offset, count));

        }

        public MyPrintWriter() throws FileNotFoundException {
            super(new java.io.ByteArrayOutputStream());
        }

        public MyPrintWriter(String file) throws FileNotFoundException {
            super(file);
            this.file = file;
        }

        public String getWrittenData() {
            return sb.toString();
        }
    }
}
