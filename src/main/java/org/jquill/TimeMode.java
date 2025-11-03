package org.jquill;

/**
 * Defines the modes for displaying timestamps in debug output.
 * Controls how the {@link Debug} utility prefixes messages with time information.
 *
 * <ul>
 *   <li>{@link #NONE} — do not display any timestamp.</li>
 *   <li>{@link #ABSOLUTE} — display the current local time formatted according to {@link Debug#setTimeFormat(String)}.</li>
 *   <li>{@link #ELAPSED} — display the time elapsed since the {@link Debug} class was loaded, formatted as [MM:SS:ms].</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * Debug.setTimeMode(TimeMode.ABSOLUTE);
 * Debug.info("Application started");
 *
 * Debug.setTimeMode(TimeMode.ELAPSED);
 * Debug.log("Elapsed time logging");
 *
 * Debug.setTimeMode(TimeMode.NONE);
 * Debug.warn("No timestamp shown");
 * }</pre>
 *
 * @see Debug
 */
public enum TimeMode {
    /** No timestamp prefix. */
    NONE,

    /** Current local wall-clock time formatted according to {@link Debug#setTimeFormat(String)}. */
    ABSOLUTE,

    /** Time elapsed since {@link Debug} was first loaded, formatted as <code>[MM:SS:ms]</code>. */
    ELAPSED
}
