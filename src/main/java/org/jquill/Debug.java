package org.jquill;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Debug {
    private static final PrintStream out = System.out;

    private static volatile boolean showType = false;
    private static volatile TimeMode timeMode = TimeMode.ELAPSED;
    private static volatile Level currentLevel = Level.LOW; // default: show all
    private static volatile String timeFormat = "HH:mm:ss";

    private static final long START_TIME = System.currentTimeMillis();

    // ------------------ Helper Commands ------------------
    private static String getTime() {
        switch (timeMode) {
            case ABSOLUTE:
                return "[" + LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat)) + "] ";
            case ELAPSED:
                long elapsed = System.currentTimeMillis() - START_TIME;
                long milliseconds = elapsed % 1000;
                long seconds = (elapsed / 1000) % 60;
                long minutes = (elapsed / (1000 * 60)) % 60;
                return String.format("[%02d:%02d:%03d] ", minutes, seconds, milliseconds);
            default:
                return "";
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

    public static void newLine() {
        newLine(1);
    }
    public static void newLine(int number) {
        for (int i = 0; i < number; i++) out.println();
    }

    // ------------------ Unified Formatted Print ------------------
    private static void printFormatted(Level level, String message, Style style, String typeLabel, boolean showTypePrefix) {
        synchronized (Debug.class) {
            if (level.getPriority() < currentLevel.getPriority()) {
                return; // skip messages below current level
            }

            String timeStr = getTime();

            // Determine prefix (type label or symbol)
            String prefix;
            if (showTypePrefix) {
                prefix = timeStr + typeLabel;
            } else {
                prefix = switch (typeLabel.trim()) {
                    case "[INFO]" -> "¡ ";
                    case "[LOG]" -> "• ";
                    case "[WARN]" -> "? ";
                    case "[ERROR]" -> "✖ ";
                    case "[SUCCESS]" -> "✔ ";
                    default -> "";
                };
                prefix += timeStr;
            }

            String line;

            // Only color prefix + time for INFO messages
            if (Objects.equals(typeLabel.trim(), "[INFO]")) {
                String coloredPrefix = style.getCode() + prefix + Style.RESET.getCode();
                line = coloredPrefix + message;
            } else {
                // All other levels: no coloring at all
                line = style.getCode() + prefix  + message + Style.RESET.getCode();
            }

            out.println(line);
        }
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

    // ------------------ Wait Method ------------------
    public static void sleep(int seconds) throws InterruptedException {
        int s = seconds * 1000;
        Thread.sleep(s);
        Debug.info("Slept for: " + seconds + "s.");
    }

    // ------------------ Get and Set Methods ------------------
    public static void setLevel(Level level) {
        currentLevel = level;
    }
    public static Level getLevel() {
        return currentLevel;
    }

    public static boolean getShowType() {
        return showType;
    }

    public static void setShowType(boolean input) {
        showType = input;
    }

    public static void setTimeMode(TimeMode input) {
        timeMode = input;
    }

    public static TimeMode getTimeMode() {
        return timeMode;
    }

    public static String getTimeFormat() {
        return timeFormat;
    }

    public static void setTimeFormat(String input) {
        timeFormat = input;
    }
}
