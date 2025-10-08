package org.jquill;

public class JQuill {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[90m";

    public static void info(String message) {
        info(message, false);
    }
    public static void info(String message, boolean showType) {
        if (showType) System.out.println(CYAN + "ⓘ INFO: " + message + RESET);
        else System.out.println(CYAN + "ⓘ " + message + RESET);
    }

    public static void warn(String message) {
        warn(message, false);
    }
    public static void warn(String message, boolean showType) {
        if (showType) System.out.println(YELLOW + "⚠ WARN: " + message + RESET);
        else System.out.println(YELLOW + "⚠ " + message + RESET);
    }

    public static void error(String message) {
        error(message, false);
    }
    public static void error(String message, boolean showType) {
        if (showType) System.out.println(RED + "⚠ ERROR: " + message + RESET);
        else System.out.println(RED + "⚠ " + message + RESET);
    }

    public static void debug(String message) {
        debug(message, false);
    }
    public static void debug(String message, boolean showType) {
        if (showType) System.out.println(GRAY + "» DEBUG: " + message + RESET);
        else System.out.println(GRAY + "» " + message + RESET);
    }

    public static void success(String message) {
        success(message, false);
    }
    public static void success(String message, boolean showType) {
        if (showType) System.out.println(GREEN + "✓ SUCCESS: " + message + RESET);
        else System.out.println(GREEN + "✓ " + message + RESET);
    }
}
