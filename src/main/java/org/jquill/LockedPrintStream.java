package org.jquill;

import java.io.PrintStream;

public class LockedPrintStream extends PrintStream {
    private final StringBuilder sb = new StringBuilder();

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