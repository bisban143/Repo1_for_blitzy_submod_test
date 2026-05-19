# Hello World — Java 21 (with Weather Feature)

A Hello World project that deliberately uses **Java 21** language features, making it incompatible with Java 11.

**New Feature**: Displays today's weather using HttpClient and the wttr.in API.

## Why it won't run on Java 11

Three Java 21 features are used — all of which fail to compile on Java 11:

| Feature | Introduced |
|---|---|
| [Records](https://openjdk.org/jeps/395) | Java 16 (JEP 395) |
| [Text Blocks](https://openjdk.org/jeps/378) | Java 15 (JEP 378) |
| [Pattern Matching for Switch](https://openjdk.org/jeps/441) | Java 21 (JEP 441) |

The `maven-enforcer-plugin` also hard-fails the build if the JDK is below 21.

## New Feature: Weather Display

This branch includes a weather display feature that:
- Fetches real-time weather data from [wttr.in](https://wttr.in) API
- Uses Java's HttpClient (available since Java 11)
- Displays location, date, condition, and temperature
- Accepts an optional city name as a command-line argument
- Falls back to "London" if no city is specified
- Handles API failures gracefully with fallback data

## Requirements

- **Java 21+** (e.g. [Eclipse Temurin](https://adoptium.net/))
- **Maven 3.9+**

## Build & Run

```bash
# Build
mvn clean package

# Run with default city (London)
java -jar target/hello-world.jar

# Run with custom city
java -jar target/hello-world.jar "New York"
java -jar target/hello-world.jar "Tokyo"
java -jar target/hello-world.jar "Paris"
```

**Note**: The weather feature requires an internet connection to fetch data from wttr.in API.

## Expected Output

```
╔══════════════════════════════════╗
║   Hello World — Java 21 Edition  ║
╚══════════════════════════════════╝
🇬🇧  Hello, World!
🇪🇸  ¡Hola, Mundo!
🇯🇵  こんにちは、世界！
🇧🇷  Olá, Mundo!

════════════════════════════════════════
📅  TODAY'S WEATHER
════════════════════════════════════════
📍 Location:    London, United Kingdom
📅 Date:        May 19, 2026
🌤️  Condition:   Sunny
🌡️  Temperature: +15°C
════════════════════════════════════════

Running on: 21.0.x+...
```

## Project Structure

```
hello-world-java/
├── .github/
│   └── workflows/
│       └── build.yml       # GitHub Actions CI
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── HelloWorld.java
├── .gitignore
├── pom.xml
└── README.md
```
