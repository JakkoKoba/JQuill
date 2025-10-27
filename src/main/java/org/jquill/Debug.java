package org.jquill;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Debug {
    private static final PrintStream OUT = System.out;
    private static final long START_TIME = System.currentTimeMillis();

    private static volatile boolean showType = false;
    private static volatile boolean showThread = false;
    private static volatile TimeMode timeMode = TimeMode.ELAPSED;
    private static volatile Level currentLevel = Level.LOW; // Default: show all
    private static volatile String timeFormat = "HH:mm:ss";

    private Debug() {
        // Utility class — prevent instantiation
    }

    // ------------------ Time & Thread Helpers ------------------

    private static String formatTime() {
        if (timeMode == TimeMode.ABSOLUTE) {
            return "[" + LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat)) + "] ";
        } else if (timeMode == TimeMode.ELAPSED) {
            long elapsed = System.currentTimeMillis() - START_TIME;
            long ms = elapsed % 1000;
            long s = (elapsed / 1000) % 60;
            long m = (elapsed / (1000 * 60)) % 60;
            return String.format("[%02d:%02d:%03d] ", m, s, ms);
        }
        return "";
    }

    private static String formatThread() {
        return "[" + Thread.currentThread().getName() + "] ";
    }

    // ------------------ Core Print ------------------

    public static void print(String msg, Style... styles) {
        StringBuilder sb = new StringBuilder();
        OUT.print(Style.processLocks(sb, msg, styles));
    }

    public static void println(String msg, Style... styles) {
        StringBuilder sb = new StringBuilder();
        OUT.println(Style.processLocks(sb, msg, styles));
    }

    // ------------------ Unified Output ------------------

    private static void print(Level level, String label, String message, Style style, boolean showTypePrefix) {
        if (level.getPriority() < currentLevel.getPriority()) return;

        StringBuilder sb = new StringBuilder();
        StringBuilder prefix = new StringBuilder();

        if (showThread) {
            prefix.append(Style.lock(formatThread()));
        }

        if (showTypePrefix) {
            prefix.append(label);
        } else {
            prefix.append(switch (label.trim()) {
                case "[INFO]" -> "i ";
                case "[LOG]" -> "* ";
                case "[WARN]" -> "? ";
                case "[ERROR]" -> "x ";
                case "[SUCCESS]" -> "+ ";
                default -> "";
            });
        }

        prefix.append(formatTime());
        prefix.append(message);

        synchronized (OUT) { // synchronize output, not class-level
            OUT.println(Style.processLocks(sb, prefix.toString(), style));
        }
    }

    // ------------------ Level Shortcuts ------------------

    public static void info(String msg) { info(msg, null); }
    public static void info(String msg, Boolean override) {
        print(Level.LOW, "[INFO]    ", msg, Style.INFO, override != null ? override : showType);
    }

    public static void log(String msg) { log(msg, null); }
    public static void log(String msg, Boolean override) {
        print(Level.LOW, "[LOG]     ", msg, Style.LOG, override != null ? override : showType);
    }

    public static void warn(String msg) { warn(msg, null); }
    public static void warn(String msg, Boolean override) {
        print(Level.HIGH, "[WARN]    ", msg, Style.WARNING, override != null ? override : showType);
    }

    public static void error(String msg) { error(msg, null); }
    public static void error(String msg, Boolean override) {
        print(Level.HIGH, "[ERROR]   ", msg, Style.ERROR, override != null ? override : showType);
    }

    public static void success(String msg) { success(msg, null); }
    public static void success(String msg, Boolean override) {
        print(Level.HIGH, "[SUCCESS] ", msg, Style.SUCCESS, override != null ? override : showType);
    }

    // ------------------ Sleep ------------------

    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
        info("Slept for: " + seconds + "s.");
    }

    // ------------------ Configuration ------------------

    public static void setLevel(Level level) {
        currentLevel = level != null ? level : Level.LOW;
    }

    public static Level getLevel() {
        return currentLevel;
    }

    public static void setShowType(boolean value) {
        showType = value;
    }

    public static boolean isShowType() {
        return showType;
    }

    public static void setTimeMode(TimeMode mode) {
        timeMode = mode != null ? mode : TimeMode.ELAPSED;
    }

    public static TimeMode getTimeMode() {
        return timeMode;
    }

    public static void setTimeFormat(String format) {
        if (format != null && !format.isBlank()) {
            timeFormat = format;
        }
    }

    public static String getTimeFormat() {
        return timeFormat;
    }

    public static void setShowThread(boolean value) {
        showThread = value;
    }

    public static boolean isShowThread() {
        return showThread;
    }
}
