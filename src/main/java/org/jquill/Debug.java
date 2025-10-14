package org.jquill;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Debug {

    public static boolean showTime = false;
    public static boolean showType = false;
    public static String timeFormat = "HH:mm:ss";

    private static final String RESET = "\u001B[0m";
    private static final String GRAY = "\u001B[38;2;108;117;125m";
    private static final String CYAN = "\u001B[38;2;91;192;222m";
    private static final String AMBER = "\u001B[38;2;224;166;58m";
    private static final String RED = "\u001B[38;2;220;53;69m";
    private static final String GREEN = "\u001B[38;2;92;184;92m";

    public static boolean isShowTime(boolean show) {
        return showTime = show;
    }
    private static String getTime() {
        if (!showTime) return ""; // if time is off, return empty
        return " [" + LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat)) + "] ";
    }

    public static boolean isShowType(boolean show) {
        return showType = show;
    }

    public static void info(String message) {
        info(message, showType);
    }
    public static void info(String message, boolean showType) {
        String timeStr = getTime();
        if (showType) System.out.println(CYAN + "[INFO] " + timeStr + RESET + message);
        else System.out.println(CYAN + "¡ " + timeStr + RESET + message);
    }

    public static void warn(String message) {
        warn(message, showType);
    }
    public static void warn(String message, boolean showType) {
        String timeStr = getTime();
        if (showType) System.out.println(AMBER + "[WARN] " + timeStr + message + RESET);
        else System.out.println(AMBER + "? " + timeStr + message + RESET);
    }

    public static void error(String message) {
        error(message, showType);
    }
    public static void error(String message, boolean showType) {
        String timeStr = getTime();
        if (showType) System.out.println(RED + "[ERROR] " + timeStr + message + RESET);
        else System.out.println(RED + "! " + timeStr + message + RESET);
    }

    public static void log(String message) {
        log(message, showType);
    }
    public static void log(String message, boolean showType) {
        String timeStr = getTime();
        if (showType) System.out.println(GRAY + "[LOG] " + timeStr + message + RESET);
        else System.out.println(GRAY + "» " + timeStr + message + RESET);
    }

    public static void success(String message) {
        success(message, showType);
    }
    public static void success(String message, boolean showType) {
        String timeStr = getTime();
        if (showType) System.out.println(GREEN + "[SUCCESS] " + timeStr + message + RESET);
        else System.out.println(GREEN + "✓ " + timeStr + message + RESET);
    }
}
