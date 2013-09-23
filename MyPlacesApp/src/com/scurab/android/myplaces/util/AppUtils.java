package com.scurab.android.myplaces.util;

import com.google.android.maps.GeoPoint;

import java.util.Random;

public class AppUtils {
    private final static Random sRandom = new Random();

    /**
     * Returns ranodm string minlenth = 5, maxlength = 10
     * Calls {@link #generateRandomString(int, int)}
     *
     * @return
     */
    public static String generateRandomString() {
        return generateRandomString(5, 10);
    }

    /**
     * generate random string with standard chars
     * using {@link #generateRandomChar()}
     *
     * @param min
     * @param max
     * @return
     */
    public static String generateRandomString(int min, int max) {
        StringBuilder sb = new StringBuilder();
        int len = min + sRandom.nextInt(max - min + 1);
        for (int i = 0; i < len; i++)
            sb.append(generateRandomChar());
        return sb.toString();
    }

    /**
     * Generate random  numeric string
     *
     * @param prefix value for every number, can be null
     * @param min    min lenght of generated number => min will be prefix.lenght + min
     * @param max    max length of generated number => max well be prefix.length + max
     * @return
     */
    public static String generateRandomNumericString(String prefix, int min, int max) {
        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        int len = min + sRandom.nextInt(max - min + 1);
        for (int i = 0; i < len; i++)
            sb.append(sRandom.nextInt(10));
        return sb.toString();
    }

    /**
     * Generate random chars from 'a' to 'z'
     *
     * @return
     */
    public static char generateRandomChar() {
        return (char) ('a' + sRandom.nextInt(25));
    }

    /**
     * Fix number to 2 decimal places
     *
     * @param dbl
     * @return
     */
    public static float fixNumber(double dbl) {
        int ix = (int) (Math.round(dbl * 100.0));
        return ix / 100.0f;
    }

    public static double round(double dbl, int decimals) {
        if (decimals > 8) {
            decimals = 8;
        }
        double base = Math.pow(10, decimals);
        int ix = (int) (Math.round(dbl * base));
        return ix / base;
    }

    public static double getDistance(GeoPoint gp1, GeoPoint gp2) {
        double x = Math.pow((gp1.getLatitudeE6() - gp2.getLatitudeE6()), 2);
        double y = Math.pow((gp1.getLongitudeE6() - gp2.getLongitudeE6()), 2);
        return Math.sqrt(x + y);
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null || "".equals(s));
    }

    public static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }
}