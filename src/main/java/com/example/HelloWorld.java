package com.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * HelloWorld — compatible with Java 11+.
 *
 * <p>Uses only language features available in Java 11 so that the same
 * source compiles on JDK 11 and any later JDK. This is the deliberate
 * counterpart of the sibling {@code repo1-java21} submodule, which uses
 * features (records, text blocks, pattern-matching switch expressions) not
 * available below JDK 21.</p>
 *
 * <table>
 *   <caption>Java 11-compatible idioms used here</caption>
 *   <tr><th>Idiom</th><th>Rationale</th></tr>
 *   <tr><td>Traditional nested class with constructor and accessor methods</td>
 *       <td>Records are Java 16+, so a regular class is used instead.</td></tr>
 *   <tr><td>Explicit {@code String} concatenation for multi-line text</td>
 *       <td>Text blocks are Java 15+, so {@code "..." + "..." + ...} is used.</td></tr>
 *   <tr><td>Traditional {@code switch} statement with explicit {@code break}</td>
 *       <td>Switch expressions with arrow labels are Java 14+; pattern-matching
 *       for switch is Java 21. The traditional statement form remains valid
 *       and is the only Java 11-compatible choice.</td></tr>
 * </table>
 *
 * <p>The {@code maven-enforcer-plugin} configured in {@code pom.xml} also
 * hard-fails the build if the JDK is below 11, providing a build-time guard
 * in addition to the language-feature guard.</p>
 */
public class HelloWorld {

    /**
     * A simple immutable data carrier for the language and message pair
     * emitted by the multilingual greetings loop.
     *
     * <p>Implemented as a traditional nested {@code static} class with a
     * constructor and explicit accessor methods — the Java 11-compatible
     * counterpart of the {@code record Greeting(String language, String message) {}}
     * declaration used in the sibling {@code repo1-java21} submodule.</p>
     */
    static class Greeting {
        private final String language;
        private final String message;

        /**
         * Constructs a {@code Greeting} bound to the given language and
         * message values.
         *
         * @param language a human-readable language identifier (e.g.
         *        {@code "English"}) — used by {@link #main(String[])} to
         *        dispatch the flag-emoji prefix
         * @param message  the greeting text in the corresponding language
         *        (e.g. {@code "Hello, World!"})
         */
        public Greeting(String language, String message) {
            this.language = language;
            this.message = message;
        }

        /** @return the language identifier supplied at construction. */
        public String getLanguage() {
            return language;
        }

        /** @return the greeting message text supplied at construction. */
        public String getMessage() {
            return message;
        }
    }

    /**
     * Application entry point. Prints, in order:
     *
     * <ol>
     *   <li>A four-line ASCII banner identifying the submodule as the
     *       "Java 11 Edition".</li>
     *   <li>Four multilingual greetings (English, Spanish, Japanese,
     *       Portuguese) each prefixed by a flag emoji, dispatched by a
     *       traditional switch statement (Java 11-compatible).</li>
     *   <li>The host runtime context (hostname, working directory, timezone,
     *       date, time) emitted by {@link #printRuntimeContext()}.</li>
     *   <li>The current JVM runtime version on the final line, preserving
     *       the F-011 "Multilingual Console Output Parity" contract that the
     *       runtime-version line is the final non-empty line of output.</li>
     * </ol>
     *
     * @param args command-line arguments; ignored
     */
    public static void main(String[] args) {

        // String concatenation across multiple literal lines — Java 11
        // compatible. The sibling Java 21 submodule uses a text block here
        // (JEP 378, Java 15+), but text blocks are unavailable on Java 11.
        String banner = "\n" +
                "╔══════════════════════════════════╗\n" +
                "║   Hello World — Java 11 Edition  ║\n" +
                "╚══════════════════════════════════╝\n";

        System.out.print(banner);

        // Explicit array typing — Java 11 compatible. The sibling Java 21
        // submodule uses `var greetings = ...` here (Java 10+ local-type
        // inference). Although `var` is technically available since Java 10
        // and would compile on Java 11, the explicit form is kept to
        // emphasise the traditional Java 11-style aesthetic of this
        // submodule.
        Greeting[] greetings = new Greeting[]{
            new Greeting("English",    "Hello, World!"),
            new Greeting("Spanish",    "¡Hola, Mundo!"),
            new Greeting("Japanese",   "こんにちは、世界！"),
            new Greeting("Portuguese", "Olá, Mundo!")
        };

        for (Greeting g : greetings) {
            // Traditional switch statement with explicit `break` — the only
            // Java 11-compatible switch form. Switch expressions and
            // arrow-syntax case labels (Java 14+) and pattern-matching for
            // switch (Java 21) are intentionally NOT used here so the file
            // continues to compile cleanly on JDK 11.
            String line;
            switch (g.getLanguage()) {
                case "English":
                    line = "🇬🇧  " + g.getMessage();
                    break;
                case "Spanish":
                    line = "🇪🇸  " + g.getMessage();
                    break;
                case "Japanese":
                    line = "🇯🇵  " + g.getMessage();
                    break;
                case "Portuguese":
                    line = "🇧🇷  " + g.getMessage();
                    break;
                default:
                    line = "🌍  " + g.getMessage();
                    break;
            }
            System.out.println(line);
        }

        // NEW: print host, working directory, timezone, date, and time.
        // This block must precede the runtime-version line so that
        // "Running on: ..." remains the final output line (F-011 parity).
        printRuntimeContext();

        System.out.println("\nRunning on: " + Runtime.version());
    }

    /**
     * Prints the host machine's identifier (hostname), its filesystem
     * context (working directory), and the current timezone, date, and
     * time to standard output.
     *
     * <p>Output line prefixes are kept identical to the Java 21 and Python
     * sibling implementations so the captured screenshots are visually
     * comparable across submodules (AAP §0.6.2.1 — Submodule Parity Rule).</p>
     *
     * <p>The {@link InetAddress#getLocalHost()} call is wrapped in a
     * defensive {@code try/catch} because {@link UnknownHostException} is a
     * checked exception and can be raised on extremely minimal containers
     * without a populated {@code /etc/hostname}; in that case the host
     * falls back to the literal string {@code "unknown"} so the
     * {@code Host:} line is still emitted.</p>
     *
     * <p>Uses only Java 11-compatible APIs: {@code java.net.InetAddress}
     * (since Java 1.0), {@code java.time.LocalDate} / {@code LocalTime} /
     * {@code ZoneId} (since Java 8), and
     * {@code java.time.format.DateTimeFormatter} (since Java 8).
     * Local-type inference via {@code var} is intentionally avoided here
     * to keep the file aesthetically consistent with the rest of the
     * Java 11 submodule.</p>
     */
    private static void printRuntimeContext() {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "unknown";
        }

        // Explicit types instead of `var` — Java 11-compatible aesthetic.
        String cwd = System.getProperty("user.dir");
        ZoneId zone = ZoneId.systemDefault();
        LocalDate date = LocalDate.now(zone);
        LocalTime time = LocalTime.now(zone);

        System.out.println("Host: " + hostname);
        System.out.println("Working directory: " + cwd);
        System.out.println("Timezone: " + zone.getId());
        System.out.println("Date: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("Time: " + time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
