package org.jquill;

public class Debug {

    public static final String RESET = "\u001B[0m";
    public static final String GRAY = "\u001B[38;2;108;117;125m";
    public static final String CYAN = "\u001B[38;2;91;192;222m";
    public static final String AMBER = "\u001B[38;2;224;166;58m";
    public static final String RED = "\u001B[38;2;220;53;69m";
    public static final String GREEN = "\u001B[38;2;92;184;92m";

    public static void info(String message) {
        info(message, false);
    }
    public static void info(String message, boolean showType) {
        if (showType) System.out.println(CYAN + "[INFO] " + RESET + message);
        else System.out.println(CYAN + "¡ " + RESET + message);
    }

    public static void warn(String message) {
        warn(message, false);
    }
    public static void warn(String message, boolean showType) {
        if (showType) System.out.println(AMBER + "[WARN] " + message + RESET);
        else System.out.println(AMBER + "? " + message + RESET);
    }

    public static void error(String message) {
        error(message, false);
    }
    public static void error(String message, boolean showType) {
        if (showType) System.out.println(RED + "[ERROR] " + message + RESET);
        else System.out.println(RED + "! " + message + RESET);
    }

    public static void log(String message) {
        log(message, false);
    }
    public static void log(String message, boolean showType) {
        if (showType) System.out.println(GRAY + "[LOG] " + message + RESET);
        else System.out.println(GRAY + "» " + message + RESET);
    }

    public static void success(String message) {
        success(message, false);
    }
    public static void success(String message, boolean showType) {
        if (showType) System.out.println(GREEN + "[SUCCESS] " + message + RESET);
        else System.out.println(GREEN + "✓ " + message + RESET);
    }
}
