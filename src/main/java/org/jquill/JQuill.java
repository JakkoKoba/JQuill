package org.jquill;

public class JQuill {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[90m";

    public static void info(String message) {
        System.out.println(CYAN + "ⓘ INFO: " + message + RESET);
    }

    public static void warn(String message) {
        System.out.println(YELLOW + "⚠ WARN: " + message + RESET);
    }

    public static void error(String message) {
        System.out.println(RED + "⚠ ERROR: " + message + RESET);
    }

    public static void debug(String message) {
        System.out.println(GRAY + "» DEBUG: " + message + RESET);
    }

    public static void success(String message) {
        System.out.println(GREEN + "✓ SUCCESS: " + message + RESET);
    }
}