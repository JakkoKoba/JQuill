package org.jquill;

public class Main {
    public static void main(String[] args) {

        Debug.setLevel(Level.LOW);

        Debug.setShowType(true);
        Debug.setTimeMode(TimeMode.ELAPSED);

        Debug.info("Starting application!");
        Debug.log("Test warning below:");
        Debug.warn("This is a warning!");
        try {
            Debug.sleep(2);
        } catch (InterruptedException e) {
            Debug.error("Error when running Debug.sleep: " + e.getMessage());
        }
        Debug.success("Success in running warn!");
        Debug.error("Well this doesn't work!");

        Debug.newLine(3);
        Debug.println("This is an example print message", Style.ITALICS, Style.CYAN);

    }
}
