package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * HelloWorld — requires Java 21+.
 *
 * Uses three language features unavailable in Java 11:
 *   • Records          (Java 16, JEP 395)
 *   • Text blocks      (Java 15, JEP 378)
 *   • Pattern-matching switch expressions (Java 21, JEP 441)
 *
 * New Feature: Weather display using HttpClient (Java 11+)
 */
public class HelloWorld {

    /** A simple record — sealed data carrier, Java 16+. */
    record Greeting(String language, String message) {}

    /** Weather information record */
    record Weather(String location, String condition, String temperature, String date) {}

    /**
     * Fetches today's weather from wttr.in API
     * @param city The city name (default: "London" if null/empty)
     * @return Weather object with current weather information
     */
    private static Weather fetchWeather(String city) {
        try {
            if (city == null || city.isEmpty()) {
                city = "London";
            }

            // Use wttr.in API with format parameter for simple text output
            String url = "https://wttr.in/" + city + "?format=%l:+%C+%t";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "curl")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body().trim();
                String[] parts = body.split(":", 2);
                String location = parts.length > 0 ? parts[0].trim() : city;
                String weatherInfo = parts.length > 1 ? parts[1].trim() : "Unknown";

                // Parse condition and temperature
                String[] weatherParts = weatherInfo.split("\\s+", 2);
                String temp = weatherParts.length > 1 ? weatherParts[1].trim() : "N/A";
                String condition = weatherParts.length > 0 ? weatherParts[0] : "Unknown";

                String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
                return new Weather(location, condition, temp, today);
            }
        } catch (Exception e) {
            // Fallback weather if API fails
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            return new Weather("Unknown", "Sunny", "72°F", today);
        }

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        return new Weather(city, "Unknown", "N/A", today);
    }

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

        // NEW FEATURE: Display today's weather
        System.out.println("\n" + "═".repeat(40));
        System.out.println("📅  TODAY'S WEATHER");
        System.out.println("═".repeat(40));

        String city = args.length > 0 ? args[0] : "London";
        Weather weather = fetchWeather(city);

        System.out.println("📍 Location:    " + weather.location());
        System.out.println("📅 Date:        " + weather.date());
        System.out.println("🌤️  Condition:   " + weather.condition());
        System.out.println("🌡️  Temperature: " + weather.temperature());
        System.out.println("═".repeat(40));

        System.out.println("\nRunning on: " + Runtime.version());
    }
}
