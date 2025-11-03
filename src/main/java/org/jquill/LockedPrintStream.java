package org.jquill;

import java.io.PrintStream;

/**
 * A {@link PrintStream} wrapper that ensures thread-safe, style-aware output using
 * {@link Style#processLocks(StringBuilder, String, Style...)}.
 * Calls to {@link #print(String)} and {@link #println(String)} are processed through the
 * {@link Style} locking mechanism, preserving nested style sequences and preventing
 * interleaving when multiple threads emit output concurrently.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * LockedPrintStream lockedOut = new LockedPrintStream(System.out);
 * lockedOut.println("styled and synchronized");
 * }</pre>
 *
 * @see Style
 * @see Debug
 */
public final class LockedPrintStream extends PrintStream {

    private final StringBuilder sb = new StringBuilder();

    /**
     * Constructs a new style-aware print stream that delegates to the given stream.
     *
     * @param original the underlying stream to write to
     */
    public LockedPrintStream(PrintStream original) {
        super(original);
    }

    @Override
    public void print(String x) {
        super.print(Style.processLocks(sb, x));
    }

    @Override
    public void println(String x) {
        super.println(Style.processLocks(sb, x));
    }
}
