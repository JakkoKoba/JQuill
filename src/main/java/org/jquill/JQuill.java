package org.jquill;

public class JQuill {
    // Print Error
    public void error(String message) {
        System.out.println("\u001B[31m⚠ ERROR: " + message + "\u001B[0m");
    }

    // Print Debug
    public void debug(String message) {
        System.out.println("\u001B[33m» DEBUG: " + message + "\u001B[0m");
    }
}