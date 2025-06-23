package pl.filemover.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;


public class CopyTaskTest {

    private Path tempDir;

    @BeforeEach
    public void setup() throws IOException {
        tempDir = Files.createTempDirectory("copytasktest");
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.walk(tempDir)
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }

    @Test
    public void testFileIsCopied() throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path dest = tempDir.resolve("dest.txt");

        Files.writeString(source, "Hello CopyTask");

        CopyTask task = new CopyTask(source.toFile(), dest.toFile());
        boolean result = task.call();

        assertTrue(result);
        assertTrue(Files.exists(dest));
        assertEquals("Hello CopyTask", Files.readString(dest));
    }

    @Test
    public void testFileIsSkippedIfIdentical() throws Exception {
        Path source = tempDir.resolve("same.txt");
        Path dest = tempDir.resolve("same_copy.txt");

        Files.writeString(source, "SAME");
        Files.writeString(dest, "SAME");

        CopyTask task = new CopyTask(source.toFile(), dest.toFile());
        boolean result = task.call();

        assertFalse(result); 
    }

    @Test
    public void testDirectoryIsCreated() throws Exception {
        Path source = tempDir.resolve("folder");
        Path dest = tempDir.resolve("folder_copy");

        Files.createDirectory(source);

        CopyTask task = new CopyTask(source.toFile(), dest.toFile());
        boolean result = task.call();

        assertTrue(result);
        assertTrue(Files.exists(dest));
        assertTrue(Files.isDirectory(dest));
    }

    @Test
    public void testMissingSourceFileThrowsException() {
        Path source = tempDir.resolve("missing.txt");
        Path dest = tempDir.resolve("should_fail.txt");

        CopyTask task = new CopyTask(source.toFile(), dest.toFile());

        assertThrows(IOException.class, task::call);
    }
}
