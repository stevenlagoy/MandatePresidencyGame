package com.stevenlagoy.presidency.util;

import org.junit.*;
import static org.junit.Assert.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public final class IOUtilTest {
    private static Path tempDir;

    @BeforeClass
    public static void setupClass() throws IOException {
        tempDir = Files.createTempDirectory("ioutiltest");
    }

    @AfterClass
    public static void cleanupClass() throws IOException {
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
    }

    @Test
    public void testWriteAndReadFile() {
        String filename = "testfile";
        IOUtil.writeFile(filename, IOUtil.Extension.TEXT, tempDir, "Hello, world!");
        Path filePath = tempDir.resolve(filename + IOUtil.Extension.TEXT.extension);
        List<String> lines = IOUtil.readFile(filePath);
        assertEquals(Collections.singletonList("Hello, world!"), lines);
    }

    @Test
    public void testListFiles() throws IOException {
        String filename = "listfile";
        IOUtil.writeFile(filename, IOUtil.Extension.JSON, tempDir, "{}");
        Set<Path> files = IOUtil.listFiles(tempDir, IOUtil.Extension.JSON);
        assertTrue(files.stream().anyMatch(p -> p.getFileName().toString().endsWith(".json")));
    }

    @Test
    public void testEmptyFiles() throws IOException {
        String filename = "deletefile";
        IOUtil.writeFile(filename, IOUtil.Extension.OUT, tempDir, "data");
        IOUtil.emptyFiles(tempDir, IOUtil.Extension.OUT);
        Set<Path> files = IOUtil.listFiles(tempDir, IOUtil.Extension.OUT);
        assertTrue(files.isEmpty());
    }
}