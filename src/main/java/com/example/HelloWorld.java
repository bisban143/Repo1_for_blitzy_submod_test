package com.example;

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

        System.out.println("\nRunning on: " + Runtime.version());
    }
}
