package com.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * HelloWorld — compatible with Java 11+.
 *
 * Uses only features available in Java 11:
 *   • Regular classes instead of records
 *   • String concatenation instead of text blocks
 *   • Traditional switch/if-else instead of pattern matching
 */
public class HelloWorld {

    /** A simple class — compatible with Java 11+. */
    static class Greeting {
        private final String language;
        private final String message;

        public Greeting(String language, String message) {
            this.language = language;
            this.message = message;
        }

        public String getLanguage() {
            return language;
        }

        public String getMessage() {
            return message;
        }
    }

    public static void main(String[] args) {

        // String concatenation — Java 11 compatible
        String banner = "\n" +
                "╔══════════════════════════════════╗\n" +
                "║   Hello World — Java 11 Edition  ║\n" +
                "╚══════════════════════════════════╝\n";

        System.out.print(banner);

        // Traditional array and loop — Java 11 compatible
        Greeting[] greetings = new Greeting[]{
            new Greeting("English",    "Hello, World!"),
            new Greeting("Spanish",    "¡Hola, Mundo!"),
            new Greeting("Japanese",   "こんにちは、世界！"),
            new Greeting("Portuguese", "Olá, Mundo!")
        };

        for (Greeting g : greetings) {
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

        // NEW: print runtime context (host, working directory, timezone, date, time)
        printRuntimeContext();

        System.out.println("\nRunning on: " + Runtime.version());
    }

    /**
     * Prints the host runtime context: hostname, working directory, timezone, date, and time.
     *
     * Added as part of the cross-submodule "Runtime Context Output" feature.
     * Uses only standard-library APIs (java.net.InetAddress, java.time.*, System.getProperty).
     */
    private static void printRuntimeContext() {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            host = "unknown";
        }

        String workingDirectory = System.getProperty("user.dir");
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zoneId);
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.println("Host: " + host);
        System.out.println("Working directory: " + workingDirectory);
        System.out.println("Timezone: " + zoneId.getId());
        System.out.println("Date: " + formattedDate);
        System.out.println("Time: " + formattedTime);
    }
}
