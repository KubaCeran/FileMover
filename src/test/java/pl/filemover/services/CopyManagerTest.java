package pl.filemover.services;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;

import pl.filemover.models.CopyStats;

public class CopyManagerTest {

    private Path tempSrc;
    private Path tempDest;

    @BeforeEach
    public void setup() throws IOException {
        tempSrc = Files.createTempDirectory("src");
        tempDest = Files.createTempDirectory("dest");

        // Tworzymy pliki testowe
        Files.writeString(tempSrc.resolve("test1.txt"), "Zawartość pliku 1");
        Files.writeString(tempSrc.resolve("test2.log"), "Zawartość pliku 2");
        Files.createDirectory(tempSrc.resolve("subdir"));
        Files.writeString(tempSrc.resolve("subdir/test3.txt"), "Zawartość pliku 3");
    }

    @AfterEach
    public void cleanup() throws IOException {
        deleteRecursively(tempSrc);
        deleteRecursively(tempDest);
    }

    private void deleteRecursively(Path path) throws IOException {
        Files.walk(path)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
    }

    @Test
    public void testCopyTxtFilesOnly() throws Exception {
        try (CopyManager manager = new CopyManager(2)) {
            CopyStats stats = manager.copyDirectory(tempSrc.toFile(), tempDest.toFile(), "*.txt");
            assertEquals(3, stats.getCopied());
            assertEquals(1, stats.getSkipped());
        }
    }

    @Test
    public void testCopyAllFiles() throws Exception {
        try (CopyManager manager = new CopyManager(2)) {
            CopyStats stats = manager.copyDirectory(tempSrc.toFile(), tempDest.toFile(), "*.*");
            assertEquals(4, stats.getCopied());
            assertEquals(1, stats.getSkipped());
        }
    }
    
    @Test
    public void testInvalidPatternThrowsException() throws Exception {
        try (CopyManager manager = new CopyManager(2)) {
            Exception exception = assertThrows(Exception.class, () ->
                manager.copyDirectory(tempSrc.toFile(), tempDest.toFile(), "[a-z")
            );
            assertTrue(exception.getMessage().contains("Unclosed character class"));
        }
    }
    
    @Test
    public void testEmptySourceDirectory() throws Exception {
        // Usuwamy wszystkie pliki z tempSrc, by był pusty
        Files.walk(tempSrc)
            .filter(path -> !path.equals(tempSrc))
            .map(Path::toFile)
            .forEach(File::delete);

        try (CopyManager manager = new CopyManager(2)) {
            CopyStats stats = manager.copyDirectory(tempSrc.toFile(), tempDest.toFile(), "*.*");
            assertEquals(1, stats.getCopied());  
            assertEquals(1, stats.getSkipped());
        }
    }


}
