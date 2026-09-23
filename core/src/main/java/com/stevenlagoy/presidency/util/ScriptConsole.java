package com.stevenlagoy.presidency.util;

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Wraps a lazily-initialized JShell instance so single statements can be evaluated
 * at runtime against the running game's classpath, mirroring an IDE's
 * "Evaluate Expression" feature.
 */
public final class ScriptConsole {

    private static JShell shell;
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static String initError = null;

    private ScriptConsole() {}

    private static synchronized void ensureInitialized() {
        if (initialized.get()) return;
        try {
            shell = JShell.builder().build();

            // Give the shell access to the game's own compiled classes.
            String classpath = System.getProperty("java.class.path");
            if (classpath != null) {
                for (String entry : classpath.split(File.pathSeparator)) {
                    shell.addToClasspath(entry);
                }
            }

            // Common imports so statements don't need full qualification.
            shell.eval("import com.stevenlagoy.presidency.core.*;");
            shell.eval("import com.stevenlagoy.presidency.characters.*;");
            shell.eval("import com.stevenlagoy.presidency.demographics.*;");
            shell.eval("import com.stevenlagoy.presidency.map.*;");
            shell.eval("import com.stevenlagoy.presidency.politics.*;");
        }
        catch (Exception e) {
            initError = "ScriptConsole failed to initialize: " + e.getMessage();
        }
        initialized.set(true);
    }

    /**
     * Evaluate a single Java statement or expression and return a human-readable
     * description of the result. Blocks the calling thread — call it from a click
     * handler, not from inside the render loop.
     */
    public static synchronized String evaluate(String statement) {
        ensureInitialized();
        if (initError != null) return initError;
        if (statement == null || statement.isBlank()) return "";

        StringBuilder result = new StringBuilder();
        try {
            List<SnippetEvent> events = shell.eval(statement);
            for (SnippetEvent event : events) {
                if (event.status() == Snippet.Status.REJECTED) {
                    result.append("Rejected: ").append(statement).append("\n");
                    shell.diagnostics(event.snippet())
                        .forEach(d -> result.append(d.getMessage(null)).append("\n"));
                }
                else if (event.exception() != null) {
                    result.append("Exception: ").append(event.exception().getMessage()).append("\n");
                }
                else if (event.value() != null) {
                    result.append(event.value()).append("\n");
                }
            }
        }
        catch (Exception e) {
            result.append("Error: ").append(e.getMessage());
        }
        return result.isEmpty() ? "(no value)" : result.toString().strip();
    }
}
