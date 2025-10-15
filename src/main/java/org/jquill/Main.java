package org.jquill;

public class Main {
    public static void main(String[] args) {

        Debug.showType = false;
        Debug.showTime = true;
        Debug.useRunTime = true;

        Debug.info("Starting application!");
        Debug.log("Test warning below:");
        Debug.warn("This is a warning!");
        Debug.success("Success in running warn!");
        Debug.error("Well this doesn't work!");
    }
}
