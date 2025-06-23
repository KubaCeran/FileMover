package pl.filemover.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileMaskTest {

    @Test
    public void testPatternLiteralValues() {
        assertEquals("*.txt", FileMask.TXT.getPattern());
        assertEquals("*.jpg", FileMask.JPG.getPattern());
        assertEquals("*.*", FileMask.ALL.getPattern());
    }

    @Test
    public void testAllPatternsAreNonNull() {
        for (FileMask mask : FileMask.values()) {
            String pattern = mask.getPattern();
            assertNotNull(pattern, mask.name() + " pattern is null");
            assertFalse(pattern.trim().isEmpty(), mask.name() + " pattern is empty");
            assertTrue(pattern.startsWith("*") || pattern.startsWith("."), mask.name() + " pattern format?");
        }
    }

    @Test
    public void testEnumContainsExpectedTypes() {
        assertNotNull(FileMask.valueOf("PDF"));
        assertNotNull(FileMask.valueOf("DOCX"));
        assertNotNull(FileMask.valueOf("JSON"));
    }
}
