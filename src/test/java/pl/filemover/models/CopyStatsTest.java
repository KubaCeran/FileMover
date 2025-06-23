package pl.filemover.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CopyStatsTest {

 @Test
 public void testInitialization() {
     CopyStats stats = new CopyStats(5, 2);
     assertEquals(5, stats.getCopied());
     assertEquals(2, stats.getSkipped());
 }

 @Test
 public void testSetters() {
     CopyStats stats = new CopyStats(0, 0);
     stats.setCopied(10);
     stats.setSkipped(3);
     assertEquals(10, stats.getCopied());
     assertEquals(3, stats.getSkipped());
 }
}

