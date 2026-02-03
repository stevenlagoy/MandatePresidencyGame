package com.stevenlagoy.presidency.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public final class FilePaths {

    public static final Path ROOT = FileSystems.getDefault().getPath("MandatePresidencyGame");
    public static final Path PROJECT = ROOT.resolve("core").resolve("src").resolve("main").resolve("java")
            .resolve("com").resolve("stevenlagoy").resolve("presidency");

    public static final Path LOGS_DIR = ROOT.resolve("logs");
    public static final Path LOG_FILE = LOGS_DIR.resolve("log.txt");
    public static final Path ERROR_FILE = LOGS_DIR.resolve("error.txt");
    public static final Path OUTPUT_FILE = LOGS_DIR.resolve("output.txt");

    public static final List<Path> IGNORED_PATHS = List.of();

}
