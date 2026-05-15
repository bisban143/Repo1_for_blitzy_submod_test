# Hello World — Java 21

A Hello World project that deliberately uses **Java 21** language features, making it incompatible with Java 11.

## Why it won't run on Java 11

Three Java 21 features are used — all of which fail to compile on Java 11:

| Feature | Introduced |
|---|---|
| [Records](https://openjdk.org/jeps/395) | Java 16 (JEP 395) |
| [Text Blocks](https://openjdk.org/jeps/378) | Java 15 (JEP 378) |
| [Pattern Matching for Switch](https://openjdk.org/jeps/441) | Java 21 (JEP 441) |

The `maven-enforcer-plugin` also hard-fails the build if the JDK is below 21.

## Requirements

- **Java 21+** (e.g. [Eclipse Temurin](https://adoptium.net/))
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
║   Hello World — Java 21 Edition  ║
╚══════════════════════════════════╝
🇬🇧  Hello, World!
🇪🇸  ¡Hola, Mundo!
🇯🇵  こんにちは、世界！
🇧🇷  Olá, Mundo!

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
