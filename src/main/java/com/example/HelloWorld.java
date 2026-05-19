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
 * Uses three language features unavailable in Java 11:
 *   • Records          (Java 16, JEP 395)
 *   • Text blocks      (Java 15, JEP 378)
 *   • Pattern-matching switch expressions (Java 21, JEP 441)
 */
public class HelloWorld {

    /** A simple record — sealed data carrier, Java 16+. */
    record Greeting(String language, String message) {}

    public static void main(String[] args) {

        // Text block — Java 15+
        String banner = """
                ╔══════════════════════════════════╗
                ║   Hello World — Java 21 Edition  ║
                ╚══════════════════════════════════╝
                """;

        System.out.print(banner);

        // Pattern-matching switch expression — Java 21+
        var greetings = new Greeting[]{
            new Greeting("English",    "Hello, World!"),
            new Greeting("Spanish",    "¡Hola, Mundo!"),
            new Greeting("Japanese",   "こんにちは、世界！"),
            new Greeting("Portuguese", "Olá, Mundo!")
        };

        for (Greeting g : greetings) {
            String line = switch (g.language()) {
                case "English"    -> "🇬🇧  " + g.message();
                case "Spanish"    -> "🇪🇸  " + g.message();
                case "Japanese"   -> "🇯🇵  " + g.message();
                case "Portuguese" -> "🇧🇷  " + g.message();
                default           -> "🌍  " + g.message();
            };
            System.out.println(line);
        }

        // NEW: print host, working directory, timezone, date, and time
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
     * comparable across submodules (AAP §0.6.2.1 — Submodule Parity Rule).
     *
     * <p>The InetAddress.getLocalHost() call is wrapped in a defensive
     * try/catch because UnknownHostException is a checked exception and
     * can be raised on extremely minimal containers without a populated
     * /etc/hostname; in that case the host falls back to the literal
     * string "unknown" so the Host: line is still emitted.
     */
    private static void printRuntimeContext() {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "unknown";
        }

        // Use Java 21 local-type inference (`var`), consistent with the
        // existing `var greetings = ...` line in main() above.
        var cwd = System.getProperty("user.dir");
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
