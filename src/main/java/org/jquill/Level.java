package org.jquill;

/**
 * Defines the severity levels for debug output, controlling which messages are shown
 * based on their priority relative to the current logging level.
 * Two levels are provided:
 * <ul>
 *   <li>{@link #LOW} — lower priority messages; typically informational or standard logs</li>
 *   <li>{@link #HIGH} — higher priority messages; typically warnings, errors, or success notifications</li>
 * </ul>
 * Each level has an associated integer {@link #getPriority()} which is used internally
 * by {@link Debug} to determine whether a message should be printed depending on the
 * currently set logging level.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * Debug.setLevel(Level.LOW);   // Show all messages
 * if (messageLevel.getPriority() >= Debug.getLevel().getPriority()) {
 *     Debug.log("This message will be printed if its priority meets the current level");
 * }
 * }</pre>
 *
 * @see Debug
 */
public enum Level {

    /** Lower priority messages; typically informational or standard logs. */
    LOW(1),

    /** Higher priority messages; typically warnings, errors, or success notifications. */
    HIGH(2);

    private final int priority;

    /**
     * Constructs a Level with the given priority.
     *
     * @param priority the integer priority associated with this level
     */
    Level(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the priority of this level.
     * Higher priority values indicate more important messages.
     *
     * @return the integer priority
     */
    public int getPriority() {
        return priority;
    }
}