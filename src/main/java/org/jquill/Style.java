package org.jquill;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @param code the ANSI code string
 * Represents ANSI text styles and colors for terminal output, supporting both foreground and background colors,
 * text attributes (bold, italic, underline, etc.), and semantic aliases for common debug levels.
 * This class also provides a locking mechanism to protect segments of text from being restyled when multiple
 * styles are applied. Locked sections are preserved during processing, ensuring consistent rendering in
 * multithreaded environments.
 *
 * <h2>Core Features:</h2>
 * <ul>
 *   <li>Predefined text styles: {@link #BOLD}, {@link #DIM}, {@link #ITALIC}, {@link #UNDERLINE}, {@link #STRIKETHROUGH}, {@link #INVERT}</li>
 *   <li>Foreground and background colors with RGB, hex, or 256-color codes</li>
 *   <li>Semantic style aliases for logging: {@link #INFO}, {@link #SUCCESS}, {@link #WARNING}, {@link #ERROR}, {@link #LOG}, {@link #MUTED}</li>
 *   <li>Locking system: {@link #lock(String)}, {@link #unlock(String)}, {@link #isLocked(String)}</li>
 *   <li>Style combination: {@link #and(Style)}</li>
 *   <li>Safe application of multiple styles with {@link #apply(String, Style...)}</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * String styledMessage = Style.apply("Warning!", Style.WARNING, Style.BOLD);
 * Debug.println(styledMessage);
 *
 * String lockedText = Style.lock("Do not style me");
 * String combined = Style.apply("Prefix " + lockedText + " Suffix", Style.INFO);
 * Debug.println(combined);
 * }</pre>
 *
 * <h2>Thread Safety and Locking:</h2>
 * Locked segments are identified with special ANSI escape sequences to prevent re-styling.
 * Methods like {@link #processLocks(StringBuilder, String, Style...)} handle locked and unlocked text separately,
 * ensuring safe styling in multithreaded logging scenarios.
 *
 * @see Debug
 */
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
    public static final Style BG_CHARCOAL = bg(55, 70, 80);
    public static final Style BG_SILVER = bg(190, 190, 190);
    public static final Style BG_WHITE = bg(245, 245, 245);

    public static final Style BG_RED = bg(220, 55, 70);
    public static final Style BG_ORANGE = bg(255, 140, 0);
    public static final Style BG_AMBER = bg(255, 195, 5);
    public static final Style BG_YELLOW = bg(255, 215, 0);
    public static final Style BG_GOLD = bg(210, 175, 55);
    public static final Style BG_LIME = bg(190, 255, 0);
    public static final Style BG_GREEN = bg(40, 165, 70);
    public static final Style BG_MINT = bg(150, 250, 150);
    public static final Style BG_TEAL = bg(55, 160, 160);
    public static final Style BG_CYAN = bg(0, 190, 210);
    public static final Style BG_SKY = bg(135, 205, 250);
    public static final Style BG_BLUE = bg(0, 123, 255);
    public static final Style BG_INDIGO = bg(75, 0, 130);
    public static final Style BG_PURPLE = bg(110, 65, 195);
    public static final Style BG_VIOLET = bg(150, 0, 210);
    public static final Style BG_LAVENDER = bg(180, 125, 220);
    public static final Style BG_PINK = bg(255, 105, 180);
    public static final Style BG_ROSE = bg(255, 180, 195);
    public static final Style BG_CORAL = bg(255, 130, 80);
    public static final Style BG_BROWN = bg(140, 70, 20);
    public static final Style BG_SAND = bg(195, 180, 130);

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
     *
     * @param msg the text to lock
     * @return the locked text
     */
    public static String lock(String msg) {
        return LOCK_START + msg + LOCK_END;
    }

    /**
     * Removes lock boundaries from text.
     *
     * @param msg the locked text
     * @return the unlocked text
     */
    public static String unlock(String msg) {
        return msg.replace(LOCK_START, "").replace(LOCK_END, "");
    }

    /**
     * Checks if a message contains lock delimiters.
     *
     * @param msg the message to check
     * @return true if the text is locked
     */
    public static boolean isLocked(String msg) {
        return msg.contains(LOCK_START) && msg.contains(LOCK_END);
    }

    // ------------------ Core Style Logic ------------------

    /**
     * Combines two styles into one composite style.
     *
     * @param other the other style
     * @return a new style representing both
     */
    public Style and(Style other) {
        return new Style(this.code + other.code);
    }

    /**
     * Returns the ANSI code for this style.
     *
     * @return the ANSI code string
     */
    public String getCode() {
        return code;
    }

    // ------------------ Color Builders ------------------

    /**
     * Creates a foreground style from RGB values.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return the foreground style
     */
    public static Style fg(int r, int g, int b) {
        return new Style(String.format("\u001B[38;2;%d;%d;%dm", r, g, b));
    }

    /**
     * Creates a background style from RGB values.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return the background style
     */
    public static Style bg(int r, int g, int b) {
        return new Style(String.format("\u001B[48;2;%d;%d;%dm", r, g, b));
    }

    /**
     * Parses a hex color string as foreground.
     *
     * @param hex the hex color string
     * @return the foreground style
     */
    public static Style fg(String hex) {
        return parseHex(hex, true);
    }

    /**
     * Parses a hex color string as background.
     *
     * @param hex the hex color string
     * @return the background color
     */
    public static Style bg(String hex) {
        return parseHex(hex, false);
    }

    /**
     * Creates a foreground style from 256-color code.
     *
     * @param code the 256-color code (0-255)
     * @return the foreground style
     */
    public static Style fg256(int code) {
        return new Style(String.format("\u001B[38;5;%dm", code));
    }

    /**
     * Creates a background style from 256-color code.
     *
     * @param code the 256-color code (0-255)
     * @return the background style
     */
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
     * Applies one or more styles to a message. Locked sections are preserved.
     *
     * @param msg    the message to style
     * @param styles the styles to apply
     * @return styled text
     */
    public static String apply(String msg, Style... styles) {
        if (msg == null || msg.isEmpty()) return msg;
        StringBuilder sb = new StringBuilder();
        return processLocks(sb, msg, styles);
    }

    /**
     * Opens styles without closing — useful for inline styling.
     *
     * @param styles the styles to open
     * @return ANSI codes for styles
     */
    public static String open(Style... styles) {
        StringBuilder sb = new StringBuilder();
        for (Style s : styles) sb.append(s.code);
        return sb.toString();
    }

    /**
     * Returns the ANSI reset code.
     *
     * @return reset ANSI code
     */
    public static String close() {
        return RESET.code;
    }

    // ------------------ Lock-Aware Processing ------------------

    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[0-9;?]*[A-Za-z]");

    /**
     * Processes a message, preserving locked segments while applying styles elsewhere.
     *
     * @param sb     the string builder to append results to
     * @param msg    the message to process
     * @param styles styles to apply
     * @return the fully styled message
     */
    public static String processLocks(StringBuilder sb, String msg, Style... styles) {
        String[] segments = msg.split("(?=\\u001B\\[\\?200h)|(?<=\\u001B\\[\\?200l)");

        for (String segment : segments) {
            if (isLocked(segment)) {
                sb.append(segment);
            } else {
                Matcher matcher = ANSI_PATTERN.matcher(segment);
                int lastIdx = 0;
                while (matcher.find()) {
                    if (matcher.start() > lastIdx) {
                        String text = segment.substring(lastIdx, matcher.start());
                        for (Style s : styles) sb.append(s.code);
                        sb.append(text);
                        sb.append(RESET.code);
                    }
                    sb.append(matcher.group());
                    lastIdx = matcher.end();
                }
                if (lastIdx < segment.length()) {
                    String text = segment.substring(lastIdx);
                    for (Style s : styles) sb.append(s.code);
                    sb.append(RESET.code);
                }
            }
        }

        return sb.toString();
    }
}