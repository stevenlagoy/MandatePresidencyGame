package com.stevenlagoy.presidency.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.text.SimpleDateFormat;

public final class Logger {

    private Logger() {
    }

    public static void log(@NotNull String logline) {
        try {
            File logFile = new File(FilePaths.LOG_FILE.toString());
            var ignored = logFile.createNewFile();
            PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

            logline = logline.replace("\n", " | ").replace("\r", "");
            logWriter.printf("%s : %s%n", getDate(), logline);
            IOUtils.stdout.printf("%s : %s%n", getDate(), logline);
            logWriter.close();
        } catch (IOException e) {
            IOUtils.stdout.println(e);
            System.exit(-1);
        }
    }

    public static void log(@NotNull String format, @Nullable Object... args) {
        log(String.format(format, args));
    }

    /**
     * Write the passed errorline to the standard error file.
     *
     * @param errorline String to be written, explaining information about the event logged
     * @see #error(Exception)
     * @see #error(String, String)
     * @see #error(String, String, Exception)
     */
    public static void error(@NotNull String errorline) {
        try {
            File errorFile = new File(FilePaths.ERROR_FILE.toString());
            var ignored = errorFile.createNewFile();
            PrintWriter errorWriter = new PrintWriter(new FileWriter(errorFile, true));

            errorline = errorline.replace("\n", " | ").replace("\r", "");
            errorWriter.printf("%s : %s%n", getDate(), errorline);
            IOUtils.stdout.printf("%s : %s%n", getDate(), errorline);
            errorWriter.close();
        } catch (IOException e) {
            IOUtils.stdout.println(e);
            System.exit(-1);
        }
    }

    public static void error(@NotNull String format, @Nullable Object... args) {
        error(String.format(format, args));
    }

    /**
     * Write the Exception to the standard error file.
     *
     * @param logE Exception to be written. The Exception.printStackTrace() method will be used.
     * @see #error(String, String, Exception)
     */
    public static void error(Exception logE) {
        try {
            File errorFile = new File(FilePaths.ERROR_FILE.toString());
            var ignored = errorFile.createNewFile();
            PrintWriter errorWriter = new PrintWriter(new FileWriter(errorFile, true));

            StringWriter sw = new StringWriter();
            logE.printStackTrace(new PrintWriter(sw));
            // Handle any carriage return characters
            String stackTrace = sw.toString().replace("\t", " -> ").replace("\n", "").replace("\r", "");
            errorWriter.printf("%s : %s %n", getDate(), stackTrace);
            IOUtils.stdout.printf("%s : %s %n", getDate(), stackTrace);
            errorWriter.close();
            return;
        } catch (IOException e) {
            IOUtils.stdout.println(e);
            System.exit(-1);
        }
    }

    /**
     * Write the passed errorline to the standard error file, with the context string as a label.
     *
     * @param context String for the label/context in which the log is being written. Will be put in full-capitals.
     * @param errorline String to be written, explaining information about the event logged.
     * @see #error(String, String, Exception)
     */
    public static void error(String context, String errorline) {
        try {
            File errorFile = new File(FilePaths.ERROR_FILE.toString());
            var ignored = errorFile.createNewFile();
            PrintWriter errorWriter = new PrintWriter(new FileWriter(errorFile, true));

            errorline = errorline.replace("\n", " | ").replace("\r", "");
            errorWriter.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), errorline);
            IOUtils.stdout.printf("%s : %s: %s%n", getDate(), context.toUpperCase(), errorline);
            errorWriter.close();
            return;
        } catch (IOException e) {
            IOUtils.stdout.println(e);
            System.exit(-1);
        }
    }

    /**
     * Write the passed errorline to the standard error file, with the context string
     * as a label and with the passed Exception's stack trace also being written.
     *
     * @param context String for the label/context in which the log is being written. Will be put in full-capitals.
     * @param errorline String to be written, explaining information about the event logged.
     * @param logE Exception to be written. The Exception.printStackTrace() method will be used.
     */
    public static void error(String context, String errorline, Exception logE) {
        try {
            File errorFile = new File(FilePaths.ERROR_FILE.toString());
            var ignored = errorFile.createNewFile();;
            PrintWriter logWriter = new PrintWriter(new FileWriter(errorFile, true));

            StringWriter sw = new StringWriter();
            logE.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString().replace("\t", " -> ").replace("\n", "").replace("\r", "");
            errorline = errorline.replace("\n", " | ").replace("\r", "");
            logWriter.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), errorline, stackTrace);
            IOUtils.stdout.printf("%s : %s: %s @ %s%n", getDate(), context.toUpperCase(), errorline, stackTrace);
            logWriter.close();
            return;
        } catch (IOException e) {
            IOUtils.stdout.println(e);
            System.exit(-1);
        }
    }

    /**
     * Gives the current date as a formatted string for logging purposes.
     *
     * @return The current date, formatted as {@code yyyy.MM.dd.HH.mm.ss.SSS}
     */
    private static String getDate() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
    }

    /**
     * Empties the Error file of all contents.
     */
    public static boolean clearErrorFile() {
        boolean successFlag = true;
        try {
            File errorFile = new File(FilePaths.ERROR_FILE.toString());
            var ignored = errorFile.createNewFile();
            FileOutputStream errorStream = new FileOutputStream(errorFile, false);
            errorStream.close();
        } catch (IOException e) {
            error("ERROR/LOG FILE NOT FOUND", "Somehow, the error file or log file was unable to be found.", e);
            successFlag = false;
        }
        return successFlag;
    }

    public static void logMemoryReport() {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();

        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        long usedMemory = totalMemory - freeMemory;
        double usagePercent = (usedMemory * 100.0) / totalMemory;
        double maxPercent = (usedMemory * 100.0) / maxMemory;

        sb.append(String.format("Free Memory: %,.2f KB%n", (freeMemory * 1.0) / 1024));
        sb.append(String.format("Used Memory: %,.2f KB%n", (usedMemory * 1.0) / 1024));
        sb.append(String.format("Total Memory: %,.2f KB%n", (totalMemory * 1.0) / 1024));
        sb.append(String.format("Max Memory: %,.2f KB%n", (maxMemory * 1.0) / 1024));
        sb.append(String.format("Usage Percent: %.2f%%%n", usagePercent));
        sb.append(String.format("Usage of Max Percent: %.2f%%%n", maxPercent));
        error("MEMORY REPORT", sb.toString().trim());
    }

}
