package org.jquill;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Debug {
    private static final PrintStream out = System.out;

    public static boolean showTime = false;
    public static boolean showType = false;
    public static boolean useRunTime = false;
    public static String timeFormat = "HH:mm:ss";

    private static final long START_TIME = System.currentTimeMillis();

    // ------------------ Helper Commands ------------------
    private static String getTime(boolean runtime) {
        if (!showTime) return "";

        if (runtime) {
            long elapsed = System.currentTimeMillis() - START_TIME;
            long milliseconds = elapsed % 1000;
            long seconds = (elapsed / 1000) % 60;
            long minutes = (elapsed / (1000 * 60)) % 60;
            return String.format("[%02d:%02d:%03d] ", minutes, seconds, milliseconds);
        } else {
            return "[" + LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat)) + "] ";
        }
    }

    // ------------------ Core Print ------------------
    public static void print(String msg) {
        out.print(msg);
    }

    public static void print(String msg, Style... styles) {
        out.print(Style.apply(msg, styles));
    }

    public static void println(String msg) {
        out.println(msg);
    }

    public static void println(String msg, Style... styles) {
        out.println(Style.apply(msg, styles));
    }

    // ------------------ Unified Formatted Print ------------------
    // ------------------ Unified Formatted Print ------------------
    private static void printFormatted(Level level, String message, Style style, String typeLabel, boolean showTypePrefix) {
        if (level.getPriority() < currentLevel.getPriority()) {
            return; // skip messages below current level
        }

        String timeStr = getTime(useRunTime);
        String line;

        String prefix;
        if (showTypePrefix) {
            // Use full type label
            prefix = typeLabel;
        } else {
            // Use symbol based on typeLabel
            prefix = switch (typeLabel.trim()) {
                case "[INFO]" -> "¡ ";
                case "[LOG]" -> "• ";
                case "[WARN]" -> "? ";
                case "[ERROR]" -> "✖ ";
                case "[SUCCESS]" -> "✔ ";
                default -> "";
            };
        }

        line = String.format("%s%s%s", style.getCode(), prefix + timeStr, message);

        out.println(line + Style.RESET.getCode());
    }

    // ------------------ Custom Print Methods ------------------
    public static void info(String message) {
        info(message, showType);
    }

    public static void info(String message, boolean showTypePrefix) {
        String label = "[INFO]    ";
        printFormatted(Level.LOW, message, Style.CYAN, label, showTypePrefix);
    }

    public static void warn(String message) {
        warn(message, showType);
    }

    public static void warn(String message, boolean showTypePrefix) {
        String label = "[WARN]    ";
        printFormatted(Level.HIGH, message, Style.AMBER, label, showTypePrefix);
    }

    public static void error(String message) {
        error(message, showType);
    }

    public static void error(String message, boolean showTypePrefix) {
        String label = "[ERROR]   ";
        printFormatted(Level.HIGH, message, Style.RED, label, showTypePrefix);
    }

    public static void log(String message) {
        log(message, showType);
    }

    public static void log(String message, boolean showTypePrefix) {
        String label = "[LOG]     ";
        printFormatted(Level.LOW, message, Style.GRAY, label, showTypePrefix);
    }

    public static void success(String message) {
        success(message, showType);
    }

    public static void success(String message, boolean showTypePrefix) {
        String label = "[SUCCESS] ";
        printFormatted(Level.HIGH, message, Style.GREEN, label, showTypePrefix);
    }

    // ------------------ Priority Levels ------------------
    private static Level currentLevel = Level.LOW; // default: show all

    public static void setLevel(Level level) {
        currentLevel = level;
    }

    public static Level getLevel() {
        return currentLevel;
    }
}
