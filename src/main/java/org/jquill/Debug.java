package org.jquill;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatted, level-based, and optionally styled debug output.
 * Provides methods to log messages with different severity levels: {@link #info(String)},
 * {@link #log(String)}, {@link #warn(String)}, {@link #error(String)}, and {@link #success(String)}.
 * Each message can optionally include timestamps, thread names, and type labels.
 * Supports configuration of:
 * <ul>
 *   <li>Logging level via {@link #setLevel(Level)}</li>
 *   <li>Timestamp display mode via {@link #setTimeMode(TimeMode)} and format via {@link #setTimeFormat(String)}</li>
 *   <li>Whether to show message type prefixes via {@link #setShowType(boolean)}</li>
 *   <li>Whether to show thread names via {@link #setShowThread(boolean)}</li>
 * </ul>
 * Messages can also be styled using the {@link Style} class, with optional per-message style overrides.
 * All output is thread-safe and synchronized on the underlying {@link java.io.PrintStream}.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * Debug.setLevel(Level.LOW);
 * Debug.setShowType(true);
 * Debug.setTimeMode(TimeMode.ABSOLUTE);
 *
 * Debug.info("Application started");
 * Debug.warn("Potential issue detected");
 * Debug.error("Critical error occurred");
 * Debug.success("Task completed successfully");
 * }</pre>
 *
 * @see Level
 * @see TimeMode
 * @see Style
 */
public final class Debug {

    private static final PrintStream OUT = System.out;
    private static final long START_TIME = System.currentTimeMillis();

    private static volatile boolean showType = false;
    private static volatile boolean showThread = false;
    private static volatile TimeMode timeMode = TimeMode.ELAPSED;
    private static volatile Level currentLevel = Level.LOW;
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

    /**
     * Prints a styled message without a newline.
     *
     * @param msg    the message to print
     * @param styles optional styles to apply
     */
    public static void print(String msg, Style... styles) {
        StringBuilder sb = new StringBuilder();
        OUT.print(Style.processLocks(sb, msg, styles));
    }

    /**
     * Prints a styled message followed by a newline.
     *
     * @param msg    the message to print
     * @param styles optional styles to apply
     */
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

        synchronized (OUT) {
            OUT.println(Style.processLocks(sb, prefix.toString(), style));
        }
    }

    // ------------------ Level Shortcuts ------------------

    /**
     * Logs an informational message.
     *
     * @param msg      the message to log
     */
    public static void info(String msg) { info(msg, null); }

    /**
     * Logs an informational message with optional type display override.
     *
     * @param msg      the message to log
     * @param override overrides the global showType setting if non-null
     */
    public static void info(String msg, Boolean override) {
        print(Level.LOW, "[INFO]    ", msg, Style.INFO, override != null ? override : showType);
    }

    /** Logs a general log message.
     *
     * @param msg      the message to log
     */
    public static void log(String msg) { log(msg, null); }

    /** Logs a general log message with optional type display override.
     *
     * @param msg      the message to log
     * @param override overrides the global showType setting if non-null
     */
    public static void log(String msg, Boolean override) {
        print(Level.LOW, "[LOG]     ", msg, Style.LOG, override != null ? override : showType);
    }

    /** Logs a warning message.
     *
     * @param msg      the message to log
     */
    public static void warn(String msg) { warn(msg, null); }

    /** Logs a warning message with optional type display override.
     *
     * @param msg      the message to log
     * @param override overrides the global showType setting if non-null
     */
    public static void warn(String msg, Boolean override) {
        print(Level.HIGH, "[WARN]    ", msg, Style.WARNING, override != null ? override : showType);
    }

    /** Logs an error message.
     *
     * @param msg      the message to log
     */
    public static void error(String msg) { error(msg, null); }

    /** Logs an error message with optional type display override.
     *
     * @param msg      the message to log
     * @param override overrides the global showType setting if non-null
     */
    public static void error(String msg, Boolean override) {
        print(Level.HIGH, "[ERROR]   ", msg, Style.ERROR, override != null ? override : showType);
    }

    /** Logs a success message.
     *
     * @param msg      the message to log
     */
    public static void success(String msg) { success(msg, null); }

    /** Logs a success message with optional type display override.
     *
     * @param msg      the message to log
     * @param override overrides the global showType setting if non-null
     */
    public static void success(String msg, Boolean override) {
        print(Level.HIGH, "[SUCCESS] ", msg, Style.SUCCESS, override != null ? override : showType);
    }

    // ------------------ Sleep ------------------

    /**
     * Sleeps for a number of seconds and logs the sleep duration.
     *
     * @param seconds the number of seconds to sleep
     * @throws InterruptedException if interrupted during sleep
     */
    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
        info("Slept for: " + seconds + "s.");
    }

    // ------------------ Configuration ------------------

    /** Sets the global logging level.
     *
     * @param level the logging level to set
     */
    public static void setLevel(Level level) {
        currentLevel = level != null ? level : Level.LOW;
    }

    /** Returns the current logging level.
     *
     * @return the current logging level
     */
    public static Level getLevel() {
        return currentLevel;
    }

    /** Sets whether to show message type prefixes.
     *
     * @param value true to show type prefixes, false to hide them
     */
    public static void setShowType(boolean value) {
        showType = value;
    }

    /** Returns whether message type prefixes are shown.
     *
     * @return true if type prefixes are shown, false otherwise
     */
    public static boolean isShowType() {
        return showType;
    }

    /** Sets the global time mode for messages.
     *
     * @param mode the time mode to set
     */
    public static void setTimeMode(TimeMode mode) {
        timeMode = mode != null ? mode : TimeMode.ELAPSED;
    }

    /** Returns the current global time mode.
     *
     * @return the current time mode
     */
    public static TimeMode getTimeMode() {
        return timeMode;
    }

    /** Sets the time format used for absolute timestamps.
     *
     * @param format the time format string
     */
    public static void setTimeFormat(String format) {
        if (format != null && !format.isBlank()) {
            timeFormat = format;
        }
    }

    /** Returns the current time format string.
     *
     * @return the time format string
     */
    public static String getTimeFormat() {
        return timeFormat;
    }

    /** Sets whether thread names are displayed in messages.
     *
     * @param value true to show thread names, false to hide them
     */
    public static void setShowThread(boolean value) {
        showThread = value;
    }

    /** Returns whether thread names are shown in messages.
     *
     * @return true if thread names are shown, false otherwise
     */
    public static boolean isShowThread() {
        return showThread;
    }
}