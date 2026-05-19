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
 * <p>The test captures stdout, runs the entry point, and asserts that all
 * expected markers are emitted in the correct format. Backward-compatibility
 * assertions also confirm the existing greeting and runtime-version lines
 * are preserved.</p>
 *
 * <h2>Feature markers verified</h2>
 * <ul>
 *   <li>{@code Host: <hostname>}                — hostname from
 *       {@code InetAddress.getLocalHost().getHostName()}</li>
 *   <li>{@code Working directory: <cwd>}        — value of system property
 *       {@code user.dir}</li>
 *   <li>{@code Timezone: <zone-id>}             — id of
 *       {@code ZoneId.systemDefault()}</li>
 *   <li>{@code Date: YYYY-MM-DD}                — ISO-8601 calendar date</li>
 *   <li>{@code Time: HH:MM:SS}                  — wall-clock time</li>
 * </ul>
 *
 * <h2>Backward-compatibility markers verified</h2>
 * <ul>
 *   <li>{@code Hello, World!} — existing English greeting line must remain</li>
 *   <li>{@code Running on: }  — existing runtime-version line must remain
 *       (and per the spec, remain the final line of output)</li>
 * </ul>
 *
 * <h2>Stdout capture strategy</h2>
 * <p>Each test installs a fresh {@link ByteArrayOutputStream}-backed
 * {@link PrintStream} as {@code System.out} in {@link #setUp()} and restores
 * the original stream in {@link #tearDown()}. The {@link PrintStream} is
 * created with {@code autoFlush=true} and {@link StandardCharsets#UTF_8} so
 * that the non-ASCII characters emitted by the production code (emoji flags,
 * Spanish accents, Japanese kanji) are captured and decoded accurately.</p>
 */
public class HelloWorldFeatureTest {

    /**
     * The original {@link System#out} stream, preserved across each test so
     * it can be restored in {@link #tearDown()}. Saved before the override
     * happens in {@link #setUp()}.
     */
    private PrintStream originalOut;

    /**
     * In-memory buffer that captures all bytes written to {@code System.out}
     * during a single test invocation. Read via
     * {@link ByteArrayOutputStream#toString(java.nio.charset.Charset)} with
     * UTF-8 after the entry point under test returns.
     */
    private ByteArrayOutputStream byteStream;

    /**
     * Replaces {@link System#out} with an in-memory UTF-8 print stream so
     * that the captured stdout can be inspected after each test invocation.
     *
     * <p>The original {@code System.out} reference is preserved in
     * {@link #originalOut} so it can be restored in {@link #tearDown()}.
     * The replacement {@link PrintStream} is constructed with
     * {@code autoFlush=true} (the second positional argument) so that every
     * {@code println} call by the production code flushes its bytes into
     * the underlying {@link ByteArrayOutputStream} immediately — this
     * removes any need for an explicit flush before the assertions read
     * the buffer.</p>
     */
    @BeforeEach
    void setUp() {
        originalOut = System.out;
        byteStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteStream, true, StandardCharsets.UTF_8));
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
     * runtime-context block (Host, Working directory, Timezone, Date, Time)
     * AND preserves the existing greeting and runtime-version output.
     *
     * <p>The captured stdout is read once after {@code main} returns and is
     * validated against seven independent assertions:</p>
     * <ol>
     *   <li>Three substring checks for {@code "Host:"},
     *       {@code "Working directory:"}, and {@code "Timezone:"} markers.</li>
     *   <li>Two regular-expression checks that validate the YYYY-MM-DD and
     *       HH:MM:SS formats of the {@code Date:} and {@code Time:} lines
     *       respectively. The {@code (?s)} prefix enables DOTALL mode so
     *       the dot can match newline characters in the multi-line capture.</li>
     *   <li>Two backward-compatibility substring checks for the existing
     *       {@code "Hello, World!"} greeting and {@code "Running on:"}
     *       runtime-version line.</li>
     * </ol>
     *
     * <p>Each assertion attaches a contextual failure message that includes
     * the entire captured stdout, which makes CI debugging trivial when an
     * assertion fails.</p>
     */
    @Test
    void printsHostDateAndTime() {
        // Invoke the entry point under test. All stdout writes are routed
        // into byteStream via the System.setOut override installed in
        // setUp(). Passing an empty argument array mirrors the way the
        // program is launched from `java -jar target/hello-world.jar`.
        HelloWorld.main(new String[]{});

        // Decode the captured bytes back to a String using UTF-8 so that
        // any non-ASCII characters emitted by HelloWorld (emoji flags,
        // ¡, Á, Japanese kanji) are decoded faithfully.
        String output = byteStream.toString(StandardCharsets.UTF_8);

        // --- Feature markers: the five new lines produced by the
        // printRuntimeContext() helper added to HelloWorld.main(). ---
        assertTrue(output.contains("Host:"),
            "Output must contain 'Host:' marker. Actual:\n" + output);
        assertTrue(output.contains("Working directory:"),
            "Output must contain 'Working directory:' marker. Actual:\n" + output);
        assertTrue(output.contains("Timezone:"),
            "Output must contain 'Timezone:' marker. Actual:\n" + output);

        // --- Date / time format validation. The (?s) flag turns on
        // DOTALL mode so '.' matches newlines (the captured stdout
        // is multi-line). Backslashes are doubled because they live in a
        // Java String literal that is later re-interpreted as a regex. ---
        assertTrue(output.matches("(?s).*Date: \\d{4}-\\d{2}-\\d{2}.*"),
            "Output must contain a Date line in YYYY-MM-DD format. Actual:\n" + output);
        assertTrue(output.matches("(?s).*Time: \\d{2}:\\d{2}:\\d{2}.*"),
            "Output must contain a Time line in HH:MM:SS format. Actual:\n" + output);

        // --- Backward compatibility: the existing greeting line and
        // runtime-version line must remain present in the output so that
        // the new feature is purely additive. ---
        assertTrue(output.contains("Hello, World!"),
            "Existing English greeting must be preserved. Actual:\n" + output);
        assertTrue(output.contains("Running on:"),
            "Existing runtime-version line must be preserved. Actual:\n" + output);
    }
}
