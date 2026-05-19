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
Host: <hostname>
Working directory: <cwd>
Timezone: <zone-id>
Date: YYYY-MM-DD
Time: HH:MM:SS

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
├── test/                   # User-mandated test root (per AAP §0.6.2.2)
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── HelloWorldFeatureTest.java
│   └── screenshot/         # CI-generated test evidence
│       ├── .gitkeep
│       ├── run-output.txt  # CI-populated
│       └── test-output.txt # CI-populated
├── .gitignore
├── pom.xml
└── README.md
```

## Testing

Automated tests are organized under a literal `test/` directory at the submodule root (per the user-mandated convention — this overrides Maven's default `src/test/java/` location).

### Running the tests locally

```bash
mvn test
```

The test suite (`test/java/com/example/HelloWorldFeatureTest.java`) is a JUnit 5 test that:

- Swaps `System.out` with a `ByteArrayOutputStream`-backed `PrintStream` in `@BeforeEach`
- Invokes `HelloWorld.main(new String[]{})`
- Restores `System.out` in `@AfterEach`
- Asserts the captured output contains the markers `Host:`, `Working directory:`, and `Timezone:`
- Asserts the captured output matches the regexes `Date: \d{4}-\d{2}-\d{2}` and `Time: \d{2}:\d{2}:\d{2}`

Maven discovers the tests because `pom.xml` declares `<testSourceDirectory>test/java</testSourceDirectory>` inside `<build>`. The `maven-surefire-plugin` (version 3.2.5) executes the tests during the `test` lifecycle phase.

The test class itself is written in Java 11-compatible style (no text blocks, no records, no `var`, no pattern matching) — consistent with the application code in this submodule.

### Dependencies

The following dev-tier dependencies are declared in `pom.xml` at scope `test`:

| Artifact | Version | Purpose |
|---|---|---|
| `org.junit.jupiter:junit-jupiter-api` | 5.10.2 | JUnit 5 assertion and lifecycle API |
| `org.junit.jupiter:junit-jupiter-engine` | 5.10.2 | JUnit 5 platform engine for Surefire |

The application itself (the `HelloWorld` class) continues to declare zero runtime dependencies.

### Test Evidence (Screenshots)

Test and run output captures are persisted under `test/screenshot/`:

- `test/screenshot/run-output.txt` — captured stdout from `java -jar target/hello-world.jar`
- `test/screenshot/test-output.txt` — captured stdout from `mvn test`

These files are automatically populated and committed back by the GitHub Actions workflow (`.github/workflows/build.yml`) on each green CI run, and are also published as a downloadable workflow artifact (`screenshot-evidence-java11`).

### CI Runtime vs. Source Language Level (per ADR-003)

This submodule's GitHub Actions workflow (`.github/workflows/build.yml`) **intentionally uses JDK 21** via `actions/setup-java@v4` with `java-version: '21'`, while the POM compiles the source code at the **Java 11 language level** via `<release>${java.version}</release>` (where `java.version` is `11`). This is a deliberate architectural choice (ADR-003 — byte-identical Java workflows across the `repo1-java21` and `repo1-java11` submodules) and has two practical consequences:

1. **Source compatibility is enforced at the compiler level, not the JVM level.** The compiler emits Java 11 bytecode (`javac --release 11`), and the `maven-enforcer-plugin` rule `requireJavaVersion: [11,)` permits any JDK 11 or later. The Java 11 language constraints (no records, no text blocks, no `var`, no pattern-matching switch) are honored by the source code itself, not by the build environment.
2. **CI-captured evidence (`test/screenshot/run-output.txt`) shows a Java 21 runtime version on the final `Running on:` line** (e.g., `Running on: 21.0.x+...`) even though the binary is Java 11-compatible. The banner (`Hello World — Java 11 Edition`), working directory (under `…/Repo1_for_blitzy_submod_test`), and bytecode major version (55, not 65) all unambiguously identify this submodule as Java 11. To produce evidence with a Java 11 `Running on:` line, run `mvn package && java -jar target/hello-world.jar` locally under a JDK 11 installation.
