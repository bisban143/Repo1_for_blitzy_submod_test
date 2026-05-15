# Hello World — Java 11

A Hello World project that is **compatible with Java 11** and later versions.

## Features

This version uses only features available in Java 11:

- Regular classes (instead of records)
- String concatenation (instead of text blocks)
- Traditional switch statements (instead of pattern matching)

## Requirements

- **Java 11+** (e.g. [Eclipse Temurin](https://adoptium.net/))
- **Maven 3.9+**

## Build & Run

```bash
# Build
mvn clean package

# Run
java -jar target/hello-world.jar
```

## Expected Output

```
╔══════════════════════════════════╗
║   Hello World — Java 11 Edition  ║
╚══════════════════════════════════╝
🇬🇧  Hello, World!
🇪🇸  ¡Hola, Mundo!
🇯🇵  こんにちは、世界！
🇧🇷  Olá, Mundo!

Running on: 11.0.x+...
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
