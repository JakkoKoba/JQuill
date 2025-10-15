package org.jquill;

public class Style {
    // ANSI color codes for terminal output
    public static final Style RESET     = new Style("\u001B[0m");
    public static final Style BOLD      = new Style("\u001B[1m");
    public static final Style DIM       = new Style("\u001B[2m");
    public static final Style ITALICS   = new Style("\u001B[3m");
    public static final Style UNDERLINE = new Style("\u001B[4m");
    public static final Style GRAY      = new Style("\u001B[38;2;108;117;125m");
    public static final Style CYAN      = new Style("\u001B[38;2;91;192;222m");
    public static final Style AMBER     = new Style("\u001B[38;2;224;166;58m");
    public static final Style RED       = new Style("\u001B[38;2;220;53;69m");
    public static final Style GREEN     = new Style("\u001B[38;2;92;184;92m");

    private final String code;

    Style(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static String apply(String msg, Style... styles) {
        StringBuilder sb = new StringBuilder();
        for (Style s : styles) sb.append(s.getCode());
        sb.append(msg).append(RESET.getCode());
        return sb.toString();
    }
}
