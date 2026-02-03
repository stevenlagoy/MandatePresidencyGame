package com.stevenlagoy.presidency.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public final class IOUtils {

    private IOUtils() {
    }

    public static enum Extension {

        ALL(""),
        AVI(".avi"),
        BASH(".sh"),
        BATCH(".bat"),
        BINARY(".bin"),
        BITMAP(".bmp"),
        BLENDER(".blend"),
        CLASS(".class"),
        CMD(".cmd"),
        CSV(".csv"),
        DAE(".dae"),
        DATABASE(".db"),
        DDS(".dds"),
        EXECUTABLE(".exe"),
        FBX(".fbx"),
        FLAC(".flac"),
        FONT(".fnt"),
        GIF(".gif"),
        GNUZIP(".gz"),
        HEIC(".heic"),
        HTML(".html"),
        ICON(".ico"),
        IN(".in"),
        INITIALIZATION(".ini"),
        JAR(".jar"),
        JAVA(".java"),
        JPEG(".jpg"),
        JSON(".json"),
        LOG(".log"),
        M4A(".m4a"),
        MIDI(".mid"),
        MP3(".mp3"),
        MP4(".mp4"),
        MPEG(".mpg"),
        OBJ(".obj"),
        OGG(".ogg"),
        OTF(".otf"),
        OUT(".out"),
        PDF(".pdf"),
        PNG(".png"),
        SETTINGS(".set"),
        SQL(".sql"),
        SVG(".svg"),
        SYSTEM(".sys"),
        TAR(".tar"),
        TARGA(".tga"),
        TEXT(".txt"),
        TIF(".tif"),
        TTF(".ttf"),
        WAVE(".wav"),
        WINRAR(".rar"),
        XML(".xml"),
        ZIP(".zip");

        public final String extension;

        Extension(String extension) {
            this.extension = extension;
        }
    }

    /** Standard input from System.in. @see IOUtil#createScanner(InputStream) */
    public static final Scanner stdin = IOUtils.createScanner(System.in);
    /** Standard output to System.out. @see IOUtil#createWriter(OutputStream) */
    public static final PrintWriter stdout = IOUtils.createWriter(System.out);
    /** Standard output to System.err. @see IOUtil#createWriter(OutputStream) */
    public static final PrintWriter stderr = IOUtils.createWriter(System.err);
    /** Output to logs/output.txt */
    public static final PrintWriter logout;
    static {
        PrintWriter pw;
        try {
            pw = IOUtils.createWriter(FilePaths.OUTPUT_FILE.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            pw = new PrintWriter(System.out, true); // fallback to stdout
        }
        logout = pw;
    }

    public static Scanner createScanner(InputStream inputStream) {
        return new Scanner(inputStream, StandardCharsets.UTF_8.name());
    }

    public static Scanner createScanner(File file) throws FileNotFoundException {
        return new Scanner(file, StandardCharsets.UTF_8.name());
    }

    public static PrintWriter createWriter(OutputStream outputStream) {
        try {
            return new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8.name()), true);
        } catch (UnsupportedEncodingException e) {
            return new PrintWriter(new OutputStreamWriter(outputStream), true);
        }
    }

    public static PrintWriter createWriter(File file) throws IOException {
        return createWriter(file, false);
    }

    public static PrintWriter createWriter(File file, boolean append) throws IOException {
        return new PrintWriter(new FileWriter(file, append), true);
    }

    public static void closeQuietly(Closeable c) {
        if (c != null)
            try {
                c.close();
            } catch (IOException ignored) {
            }
    }

    /**
     * Returns a Set of Paths for all the files in the specified directory.
     * <p>
     * Equivalent to {@link IOUtil#listFiles(Path, FileExtension) listFiles(dir,
     * FileExtension.ALL)}
     *
     * @param dir
     *            The path to the directory to list the files within
     *
     * @return A Set of Paths to each file within the directory
     *
     * @throws IOException
     *                     If the directory path is invalid or unable to be located
     *
     * @see FileExtension#ALL
     */
    public static Set<Path> listFiles(Path dir) throws IOException {
        return listFiles(dir, Extension.ALL);
    }

    /**
     * Returns a Set of Paths for all the files in the specificed directory with the
     * given extension.
     *
     * @param dir
     *                  The path to the directory to list the files within.
     * @param extension
     *                  A FileOperations.FileExtension to filter the Path results
     *                  by.
     *
     * @return A Set of Paths to each file within the directory with the extension.
     *
     * @throws IOException
     *                     If the directory path is invalid or unable to be located.
     */
    public static Set<Path> listFiles(Path dir, Extension extension) throws IOException {
        if (dir == null)
            throw new IllegalArgumentException("Path may not be null");

        Set<Path> pathSet = new HashSet<>();
        dir = dir.normalize();
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
        for (Path path : stream) {
            if (path == null || path.getFileName() == null)
                continue;
            Path filename = path.getFileName();
            if (!Files.isDirectory(path) && !FilePaths.IGNORED_PATHS.contains(path)
                    && (filename.toString().endsWith(extension.extension)
                            || filename.toString().endsWith(extension.extension.toUpperCase()))) {
                pathSet.add(dir.resolve(filename));
            }
        }
        stream.close();
        return pathSet;
    }

    /**
     * Empties a directory of all files. Searches only the directory itself, not any
     * subdirectories (non-recursive).
     * 
     * @param dir The directory to empty.
     * @throws IOException When the directory is invalid or inaccessable, or lack
     *                     permissions to delete a file.
     */
    public static void emptyFiles(Path dir) throws IOException {
        emptyFiles(dir, Extension.ALL);
    }

    /**
     * Empties a directory of all files with the given extension. Searches only the
     * directory itself, not any subdirectories (non-recursive).
     * 
     * @param dir       The directory to empty.
     * @param extension The extension to target.
     * @throws IOException When the directory is invalid or inaccessable, or lack
     *                     permissions to delete a file.
     */
    public static void emptyFiles(Path dir, Extension extension) throws IOException {
        Set<Path> paths = listFiles(dir, extension);
        IOException ex = null;
        for (Path path : paths) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                ex = e;
            }
        }
        if (ex != null)
            throw ex;
    }

    /**
     * Reads a file and returns its contents as a List of String.
     * 
     * @param path Path to the file to be read
     * @return List of Strings containing the lines in the file
     * @throws IOException When the file cannot be found or read
     */
    public static List<String> readFile(Path path) throws IOException {
        Scanner scanner = IOUtils.createScanner(path.toFile());
        List<String> result = new ArrayList<>();
        while (scanner.hasNextLine())
            result.add(scanner.nextLine());
        return result;
    }

    /**
     * Writes a String line to a file with the given directory, name, and extension.
     * 
     * @param filename    String name of the file to create / write to
     * @param extension   Extension of the file.
     * @param destination Path to the Directory which will contain the created /
     *                    written file.
     * @param content     String to write into the file.
     * @throws IOException When the file cannot be found or created
     * @see #writeFile(String, Extension, Path, List)
     */
    public static void writeFile(String filename, Extension extension, Path destination, String content)
            throws IOException {
        writeFile(filename, extension, destination, Collections.singletonList(content));
    }

    /**
     * Writes String lines to a file with the given directory, name, and extension.
     * 
     * @param filename    String name of the file to create / write to
     * @param extension   Extension of the file
     * @param destination Path to the Directory which will contain the created or
     *                    written file
     * @param content     List of Strings to write into the file, each String on a
     *                    new line
     * @throws IOException When the file cannot be found or created
     * @see #writeFile(File, List)
     */
    public static void writeFile(String filename, Extension extension, Path destination, List<String> content)
            throws IOException {
        Path filePath = destination.resolve(filename.split("\\.")[0] + extension.extension);
        File file = filePath.toFile();
        writeFile(file, content);
    }

    /**
     * Writes String lines to a file.
     * 
     * @param file    File to be written into
     * @param content List of Strings to write into the file, each String on a
     *                new line
     * @throws IOException When the file is invalid or cannot be found or created
     */
    public static void writeFile(File file, List<String> content) throws IOException {
        Files.createDirectories(file.getParentFile().toPath());
        PrintWriter writer = IOUtils.createWriter(file, false);
        for (String line : content)
            writer.println(line);
        writer.close();
    }

}
