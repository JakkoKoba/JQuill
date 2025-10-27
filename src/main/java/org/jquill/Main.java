package org.jquill;

public class Main {
    public static void main(String[] args) {
        Debug.setLevel(Level.LOW);
        Debug.setShowThread(true);
        Debug.setShowType(false);

        Debug.setTimeMode(TimeMode.ELAPSED);

        Debug.info("Info");
        Debug.warn("Warn");
        Debug.error(Style.apply("Error"));
        Debug.success("Success");
        Debug.log("Log");

        String part1 = Style.apply("This is part 1: ", Style.BLUE);
        Debug.println(Style.lock(part1) + "x", Style.RED);
        System.out.println(Style.apply(Style.lock("x ") + "+ y = z", Style.PURPLE));
    }
}