package com.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the location/date/time feature added to {@link HelloWorld#main(String[])}.
 *
 * <p>The test captures stdout via {@link System#setOut(PrintStream)} redirection,
 * invokes the application entry point, and asserts that all five expected
 * feature markers (Host, Working directory, Timezone, Date, Time) are emitted
 * in the documented format.</p>
 *
 * <h2>Feature markers verified</h2>
 * <ul>
 *   <li>{@code Host: <hostname>}                — hostname (or {@code "unknown"}
 *       fallback) from {@code InetAddress.getLocalHost().getHostName()}</li>
 *   <li>{@code Working directory: <cwd>}        — value of system property
 *       {@code user.dir}</li>
 *   <li>{@code Timezone: <zone-id>}             — id of
 *       {@code ZoneId.systemDefault()}</li>
 *   <li>{@code Date: YYYY-MM-DD}                — ISO-8601 calendar date</li>
 *   <li>{@code Time: HH:MM:SS}                  — wall-clock time</li>
 * </ul>
 *
 * <h2>Stdout capture strategy</h2>
 * <p>Each test installs a fresh {@link ByteArrayOutputStream}-backed
 * {@link PrintStream} as {@code System.out} in {@link #setUp()} and restores
 * the original stream in {@link #tearDown()}. The {@link PrintStream} is
 * created with {@code autoFlush=true} and {@link StandardCharsets#UTF_8} so
 * that the non-ASCII characters emitted by the production code (emoji flags,
 * Spanish accents, Japanese kanji) are captured and decoded accurately.</p>
 *
 * <p>Written using Java 11-compatible idioms only (no text blocks, no
 * {@code var}, no records, no pattern matching) — consistent with the
 * application code in this submodule (AAP §0.6.2.9).</p>
 */
public class HelloWorldFeatureTest {

    /**
     * The original {@link System#out} reference, captured once at instance
     * construction time so it can be restored in {@link #tearDown()} after
     * each test. Marked {@code final} because it is only assigned at the
     * field-initializer site and never reassigned.
     */
    private final PrintStream originalOut = System.out;

    /**
     * In-memory buffer that captures all bytes written to {@code System.out}
     * during a single test invocation. Re-initialized to a fresh instance
     * in {@link #setUp()} before every test method to guarantee per-test
     * isolation. Read back via
     * {@link ByteArrayOutputStream#toString(java.nio.charset.Charset)} with
     * UTF-8 after the entry point under test returns.
     */
    private ByteArrayOutputStream capturedStdout;

    /**
     * Replaces {@link System#out} with an in-memory UTF-8 print stream so
     * that the captured stdout can be inspected after each test invocation.
     *
     * <p>The replacement {@link PrintStream} is constructed with
     * {@code autoFlush=true} (the second positional argument) so that every
     * {@code println} call by the production code flushes its bytes into
     * the underlying {@link ByteArrayOutputStream} immediately — this
     * removes any need for an explicit flush before the assertions read
     * the buffer.</p>
     */
    @BeforeEach
    void setUp() {
        capturedStdout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedStdout, true, StandardCharsets.UTF_8));
    }

    /**
     * Restores the original {@link System#out} reference so that subsequent
     * tests, Surefire's own console logging, and any post-test cleanup
     * messages continue to reach the real console rather than the discarded
     * in-memory buffer from this test.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Asserts that {@link HelloWorld#main(String[])} prints the new
     * runtime-context block (Host, Working directory, Timezone, Date, Time).
     *
     * <p>The captured stdout is read once after {@code main} returns and is
     * validated against five independent assertions:</p>
     * <ol>
     *   <li>Three substring checks for the {@code "Host:"},
     *       {@code "Working directory:"}, and {@code "Timezone:"} markers.</li>
     *   <li>Two regular-expression checks that validate the YYYY-MM-DD and
     *       HH:MM:SS formats of the {@code Date:} and {@code Time:} lines
     *       respectively. The {@code (?s)} prefix enables DOTALL mode so the
     *       dot can match newline characters in the multi-line capture, and
     *       {@code String.matches()} performs a full-string anchored match.</li>
     * </ol>
     */
    @Test
    void printsHostDateAndTime() {
        HelloWorld.main(new String[]{});
        String output = capturedStdout.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Host:"), "Output should contain Host: marker");
        assertTrue(output.contains("Working directory:"), "Output should contain Working directory: marker");
        assertTrue(output.contains("Timezone:"), "Output should contain Timezone: marker");
        assertTrue(output.matches("(?s).*Date: \\d{4}-\\d{2}-\\d{2}.*"), "Output should contain Date: YYYY-MM-DD");
        assertTrue(output.matches("(?s).*Time: \\d{2}:\\d{2}:\\d{2}.*"), "Output should contain Time: HH:MM:SS");
    }
}
