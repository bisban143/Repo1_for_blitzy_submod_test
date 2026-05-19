package com.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * HelloWorld — requires Java 21+.
 *
 * <p>Deliberately uses three language features unavailable in Java 11 so that
 * the binary will not compile or run under an older JDK. This makes the
 * submodule's identity (Java 21) self-evident at the source level and at the
 * bytecode level (class file major version 65).</p>
 *
 * <table>
 *   <caption>Java 21 features used</caption>
 *   <tr><th>Feature</th><th>Introduced</th></tr>
 *   <tr><td>Records</td><td>Java 16 (JEP 395)</td></tr>
 *   <tr><td>Text Blocks</td><td>Java 15 (JEP 378)</td></tr>
 *   <tr><td>Pattern Matching for Switch</td><td>Java 21 (JEP 441)</td></tr>
 * </table>
 *
 * <p>The {@code maven-enforcer-plugin} configured in {@code pom.xml} also
 * hard-fails the build if the JDK is below 21, providing a build-time guard
 * in addition to the language-feature guard.</p>
 */
public class HelloWorld {

    /**
     * A compact, immutable data carrier for the language and message pair
     * emitted by the multilingual greetings loop.
     *
     * <p>Implemented as a {@code record} (Java 16+, JEP 395) — this gives us
     * a final class, canonical constructor, accessor methods named after the
     * components ({@code language()}, {@code message()}), and {@code equals},
     * {@code hashCode}, and {@code toString} implementations for free. The
     * record form is intentionally chosen over a traditional class to assert
     * the submodule's Java 21 identity at the source level.</p>
     */
    record Greeting(String language, String message) {}

    /**
     * Application entry point. Prints, in order:
     *
     * <ol>
     *   <li>A four-line ASCII banner identifying the submodule as the
     *       "Java 21 Edition".</li>
     *   <li>Four multilingual greetings (English, Spanish, Japanese,
     *       Portuguese) each prefixed by a flag emoji, dispatched by a
     *       pattern-matching switch expression.</li>
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

        // Text block (Java 15+, JEP 378). Multi-line string literal preserves
        // the box-drawing characters verbatim without escape-laden
        // concatenation, and clearly identifies the submodule as Java 21.
        String banner = """

                ╔══════════════════════════════════╗
                ║   Hello World — Java 21 Edition  ║
                ╚══════════════════════════════════╝
                """;

        System.out.print(banner);

        // Java 10+ local-type inference via `var`. Using `var` here (instead
        // of `Greeting[]`) is a deliberate Java 21 idiom that the Java 11
        // sibling submodule cannot replicate.
        var greetings = new Greeting[]{
            new Greeting("English",    "Hello, World!"),
            new Greeting("Spanish",    "¡Hola, Mundo!"),
            new Greeting("Japanese",   "こんにちは、世界！"),
            new Greeting("Portuguese", "Olá, Mundo!")
        };

        for (Greeting g : greetings) {
            // Pattern-matching switch expression (Java 21, JEP 441). The
            // arrow-syntax form is an expression (not a statement) so the
            // result is assigned directly to `line`. The compiler enforces
            // exhaustiveness, and falls back to the default branch for any
            // unexpected language value.
            String line = switch (g.language()) {
                case "English"    -> "🇬🇧  " + g.message();
                case "Spanish"    -> "🇪🇸  " + g.message();
                case "Japanese"   -> "🇯🇵  " + g.message();
                case "Portuguese" -> "🇧🇷  " + g.message();
                default           -> "🌍  " + g.message();
            };
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
     * <p>Output line prefixes are kept identical to the Java 11 and Python
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
     * <p>Uses Java 21 {@code var} local-type inference for the timezone,
     * date, and time values to keep the source idiomatic for the Java 21
     * submodule. Date and time are split (rather than combined into a
     * single {@code LocalDateTime}) so that the output lines exactly mirror
     * the line breakdown of the Java 11 and Python sibling submodules.</p>
     */
    private static void printRuntimeContext() {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "unknown";
        }

        // Use Java 21 local-type inference (`var`), consistent with the
        // `var greetings = ...` declaration in main() above.
        var cwd  = System.getProperty("user.dir");
        var zone = ZoneId.systemDefault();
        var date = LocalDate.now(zone);
        var time = LocalTime.now(zone);

        System.out.println("Host: " + hostname);
        System.out.println("Working directory: " + cwd);
        System.out.println("Timezone: " + zone.getId());
        System.out.println("Date: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("Time: " + time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
