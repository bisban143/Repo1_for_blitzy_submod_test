package com.example;

/**
 * HelloWorld — requires Java 18 ONLY.
 *
 * Uses language features available in Java 18 but not in Java 11:
 *   • Records          (Java 16, JEP 395)
 *   • Text blocks      (Java 15, JEP 378)
 *   • Switch expressions (Java 14, JEP 361)
 *
 * Does NOT use Java 21 features like pattern-matching switch.
 */
public class HelloWorld {

    /** A simple record — available since Java 16. */
    record Greeting(String language, String message) {}

    public static void main(String[] args) {

        // Text block — Java 15+
        String banner = """
                ╔══════════════════════════════════╗
                ║   Hello World — Java 18 Edition  ║
                ╚══════════════════════════════════╝
                """;

        System.out.print(banner);

        // Switch expression (non-pattern-matching) — Java 14+
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

        System.out.println("\nRunning on: " + Runtime.version());
    }
}
