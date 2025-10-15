package org.jquill;

public enum Level {
    LOW(1),
    HIGH(2);

    private final int priority;

    Level(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}