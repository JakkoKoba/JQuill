package org.jquill;

public record Style(String code) {

    // ------------------ Core ANSI Reset ------------------
    public static final Style RESET = new Style("\u001B[0m");

    // ------------------ Text Styles ------------------
    public static final Style BOLD = new Style("\u001B[1m");
    public static final Style DIM = new Style("\u001B[2m");
    public static final Style ITALIC = new Style("\u001B[3m");
    public static final Style UNDERLINE = new Style("\u001B[4m");
    public static final Style INVERT = new Style("\u001B[7m");
    public static final Style STRIKETHROUGH = new Style("\u001B[9m");

    // ------------------ Foreground Colors ------------------
    public static final Style BLACK = fg(35, 35, 40);
    public static final Style GRAY = fg(110, 115, 125);
    public static final Style LIGHT_GRAY = fg(175, 180, 190);
    public static final Style CHARCOAL = fg(55, 70, 80);
    public static final Style SILVER = fg(190, 190, 190);
    public static final Style WHITE = fg(245, 245, 245);

    public static final Style RED = fg(220, 55, 70);
    public static final Style ORANGE = fg(255, 140, 0);
    public static final Style AMBER = fg(255, 195, 5);
    public static final Style YELLOW = fg(255, 215, 0);
    public static final Style GOLD = fg(210, 175, 55);
    public static final Style LIME = fg(190, 255, 0);
    public static final Style GREEN = fg(40, 165, 70);
    public static final Style MINT = fg(150, 250, 150);
    public static final Style TEAL = fg(55, 160, 160);
    public static final Style CYAN = fg(0, 190, 210);
    public static final Style SKY = fg(135, 205, 250);
    public static final Style BLUE = fg(0, 123, 255);
    public static final Style INDIGO = fg(75, 0, 130);
    public static final Style PURPLE = fg(110, 65, 195);
    public static final Style VIOLET = fg(150, 0, 210);
    public static final Style LAVENDER = fg(180, 125, 220);
    public static final Style PINK = fg(255, 105, 180);
    public static final Style ROSE = fg(255, 180, 195);
    public static final Style CORAL = fg(255, 130, 80);
    public static final Style BROWN = fg(140, 70, 20);
    public static final Style SAND = fg(195, 180, 130);

    // ------------------ Background Colors ------------------
    public static final Style BG_BLACK = bg(35, 35, 40);
    public static final Style BG_GRAY = bg(110, 115, 125);
    public static final Style BG_LIGHT_GRAY = bg(175, 180, 190);
    public static final Style BG_WHITE = bg(245, 245, 245);
    public static final Style BG_RED = bg(220, 55, 70);
    public static final Style BG_ORANGE = bg(255, 140, 0);
    public static final Style BG_AMBER = bg(255, 195, 5);
    public static final Style BG_YELLOW = bg(255, 215, 0);
    public static final Style BG_GOLD = bg(210, 175, 55);
    public static final Style BG_GREEN = bg(40, 165, 70);
    public static final Style BG_BLUE = bg(0, 123, 255);
    public static final Style BG_PURPLE = bg(110, 65, 195);
    public static final Style BG_PINK = bg(255, 105, 180);

    // ------------------ Semantic Aliases ------------------
    public static final Style INFO = CYAN.and(ITALIC);
    public static final Style SUCCESS = GREEN;
    public static final Style WARNING = AMBER;
    public static final Style ERROR = RED;
    public static final Style LOG = SILVER.and(ITALIC);
    public static final Style MUTED = GRAY.and(DIM);

    // ------------------ Lock System ------------------
    private static final String LOCK_START = "\u001B[?200h";
    private static final String LOCK_END = "\u001B[?200l";

    /**
     * Marks text as locked — prevents re-styling during processing.
     */
    public static String lock(String msg) {
        return LOCK_START + msg + LOCK_END;
    }

    /**
     * Removes lock boundaries from text.
     */
    public static String unlock(String msg) {
        return msg.replace(LOCK_START, "").replace(LOCK_END, "");
    }

    /**
     * Returns true if the text contains lock delimiters.
     */
    public static boolean isLocked(String msg) {
        return msg.contains(LOCK_START) && msg.contains(LOCK_END);
    }

    // ------------------ Core Style Logic ------------------

    /**
     * Combines two styles into one composite style.
     */
    public Style and(Style other) {
        return new Style(this.code + other.code);
    }

    public String getCode() {
        return code;
    }

    // ------------------ Color Builders ------------------

    public static Style fg(int r, int g, int b) {
        return new Style(String.format("\u001B[38;2;%d;%d;%dm", r, g, b));
    }

    public static Style bg(int r, int g, int b) {
        return new Style(String.format("\u001B[48;2;%d;%d;%dm", r, g, b));
    }

    public static Style fg(String hex) {
        return parseHex(hex, true);
    }

    public static Style bg(String hex) {
        return parseHex(hex, false);
    }

    public static Style fg256(int code) {
        return new Style(String.format("\u001B[38;5;%dm", code));
    }

    public static Style bg256(int code) {
        return new Style(String.format("\u001B[48;5;%dm", code));
    }

    private static Style parseHex(String hex, boolean foreground) {
        if (hex == null || !hex.matches("^#?[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Invalid hex color: " + hex);
        }
        hex = hex.startsWith("#") ? hex.substring(1) : hex;
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return foreground ? fg(r, g, b) : bg(r, g, b);
    }

    // ------------------ Application ------------------

    /**
     * Applies one or more styles to a message.
     * Locked sections are preserved.
     */
    public static String apply(String msg, Style... styles) {
        if (msg == null || msg.isEmpty()) return msg;
        StringBuilder sb = new StringBuilder();
        // Delegate to processLocks which preserves locked segments and styles the rest.
        return processLocks(sb, msg, styles);
    }


    /**
     * Opens styles without closing — useful for inline styling.
     */
    public static String open(Style... styles) {
        StringBuilder sb = new StringBuilder();
        for (Style s : styles) sb.append(s.code);
        return sb.toString();
    }

    /**
     * Returns the ANSI reset code.
     */
    public static String close() {
        return RESET.code;
    }

    // ------------------ Lock-Aware Processing ------------------

    /**
     * Processes a message, preserving locked segments while applying styles elsewhere.
     */
    private static final String ANSI_REGEX = "\u001B\\[[;\\d]*m";

    public static String processLocks(StringBuilder sb, String msg, Style... styles) {
        // Split by lock boundaries
        String[] segments = msg.split("(?=\\u001B\\[\\?200h)|(?<=\\u001B\\[\\?200l)");

        for (String segment : segments) {
            if (isLocked(segment)) {
                sb.append(segment); // locked text untouched
            } else {
                int idx = 0;
                while (idx < segment.length()) {
                    // Check if the next part starts with an ANSI code
                    if (segment.charAt(idx) == '\u001B') {
                        int end = idx + 2;
                        while (end < segment.length() && !Character.isLetter(segment.charAt(end))) end++;
                        end++; // include the letter
                        sb.append(segment, idx, end);
                        idx = end;
                    } else {
                        // Unstyled text: apply styles until next ANSI or end
                        int end = idx;
                        while (end < segment.length() && segment.charAt(end) != '\u001B') end++;
                        for (Style s : styles) sb.append(s.getCode());
                        sb.append(segment, idx, end);
                        sb.append(Style.close());
                        idx = end;
                    }
                }
            }
        }

        return sb.toString();
    }


}