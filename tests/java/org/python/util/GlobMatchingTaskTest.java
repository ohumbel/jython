package org.python.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.python.expose.generate.ExposeTask;

class GlobMatchingTaskTest {

    static class DummyGlobMatchingTask extends GlobMatchingTask {
        boolean processed = false;
        Set<File> receivedMatches;

        @Override
        protected void process(Set<File> matches) {
            this.processed = true;
            this.receivedMatches = matches;
        }

        @Override
        protected String getFrom() {
            return "*.txt";
        }

        @Override
        protected String getTo() {
            return "*.txt";
        }
    }

    @Test
    void testCommandLineOptions(@TempDir Path tempDir) throws IOException {
        Path src = tempDir.resolve("src");
        Path dest = tempDir.resolve("dest");
        Files.createDirectories(src);

        File f1 = Files.createFile(src.resolve("a.txt")).toFile();
        File f2 = Files.createFile(src.resolve("b.txt")).toFile();

        DummyGlobMatchingTask task = new DummyGlobMatchingTask();
        task.runFromCommandLine(new String[] {
            "--srcdir", src.toString(),
            "--destdir", dest.toString()
        });

        assertTrue(task.processed);
        assertNotNull(task.receivedMatches);
        assertEquals(2, task.receivedMatches.size());
        assertTrue(task.receivedMatches.contains(f1));
        assertTrue(task.receivedMatches.contains(f2));
        assertTrue(Files.exists(dest));
    }

    @Test
    void testCommandLineEqualsOptions(@TempDir Path tempDir) throws IOException {
        Path src = tempDir.resolve("src");
        Path dest = tempDir.resolve("dest");
        Files.createDirectories(src);

        File f1 = Files.createFile(src.resolve("a.txt")).toFile();
        File f2 = Files.createFile(src.resolve("b.txt")).toFile();

        DummyGlobMatchingTask task = new DummyGlobMatchingTask();
        task.runFromCommandLine(new String[] {
            "--srcdir=" + src.toString(),
            "--destdir=" + dest.toString(),
            "--excludes=b.txt"
        });

        assertTrue(task.processed);
        assertNotNull(task.receivedMatches);
        assertEquals(1, task.receivedMatches.size());
        assertTrue(task.receivedMatches.contains(f1));
        assertFalse(task.receivedMatches.contains(f2));
    }

    @Test
    void testUnknownArgument() {
        DummyGlobMatchingTask task = new DummyGlobMatchingTask();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            task.runFromCommandLine(new String[] {"--unknown"});
        });
        assertTrue(ex.getMessage().contains("Unknown argument"));
    }

    @Test
    void testMissingArgumentValue() {
        DummyGlobMatchingTask task = new DummyGlobMatchingTask();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            task.runFromCommandLine(new String[] {"--srcdir"});
        });
        assertTrue(ex.getMessage().contains("Missing value"));
    }

    @Test
    void testMissingRequiredParameters() {
        DummyGlobMatchingTask task = new DummyGlobMatchingTask();
        BuildException ex = assertThrows(BuildException.class, () -> {
            task.runFromCommandLine(new String[] {});
        });
        assertTrue(ex.getMessage().contains("srcdir attribute must be set"));
    }

    @Test
    void testExposeTaskMainMissingArgs() {
        assertThrows(BuildException.class, () -> {
            ExposeTask.main(new String[] {});
        });
    }

    @Test
    void testJycompileAntTaskMainMissingArgs() {
        assertThrows(BuildException.class, () -> {
            JycompileAntTask.main(new String[] {});
        });
    }
}
