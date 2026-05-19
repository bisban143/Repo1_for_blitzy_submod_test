# Hello World — Java 18

A Hello World project that deliberately uses **Java 18** language features, making it incompatible with Java 11 or Java 21+.

## Why it's Java 18 ONLY

Three Java 18-compatible features are used — all of which fail to compile on Java 11:

| Feature | Introduced |
|---|---|
| [Records](https://openjdk.org/jeps/395) | Java 16 (JEP 395) |
| [Text Blocks](https://openjdk.org/jeps/378) | Java 15 (JEP 378) |
| [Switch Expressions](https://openjdk.org/jeps/361) | Java 14 (JEP 361) |

The `maven-enforcer-plugin` enforces Java 18 ONLY (rejects both older and newer versions).

## Requirements

- **Java 18 ONLY** (e.g. [Eclipse Temurin 18](https://adoptium.net/))
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
║   Hello World — Java 18 Edition  ║
╚══════════════════════════════════╝
🇬🇧  Hello, World!
🇪🇸  ¡Hola, Mundo!
🇯🇵  こんにちは、世界！
🇧🇷  Olá, Mundo!

Running on: 18.0.x+...
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
