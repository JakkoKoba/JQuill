package org.jquill;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Debug.setShowThread(true);                // Show thread names
        Debug.setTimeMode(TimeMode.ABSOLUTE);     // Use absolute timestamp

        // Basic Logging
        Debug.info("Starting application...");
        Debug.log("Loading modules...");
        Debug.warn("Deprecated configuration detected.");
        Debug.error("Failed to load optional plugin!");
        Debug.success("Modules loaded successfully!");

        // Styled Output
        Debug.println("Custom styled message", Style.BOLD.and(Style.PURPLE));
        Debug.println("Background + foreground", Style.BG_WHITE.and(Style.BLACK).and(Style.UNDERLINE));

        // Inline Open/Close
        Debug.print(Style.open(Style.GREEN, Style.BOLD));
        Debug.print("Partial styled text ");
        Debug.println(Style.close() + "Normal text");

        // Chained Styles
        Debug.println("All styles demo", Style.BOLD.and(Style.RED).and(Style.UNDERLINE));

        // Lock + Apply Example
        String lockedMessage = Style.lock("DO NOT MODIFY");
        String styled = Style.apply("Message with " + lockedMessage + " inside", Style.AMBER);
        Debug.println(styled);

        // Finish
        Debug.info("Application finished!");
    }
}