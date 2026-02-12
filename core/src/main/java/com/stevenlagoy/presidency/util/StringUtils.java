package com.stevenlagoy.presidency.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Determines whether a given position in a line of text is currently inside a
     * string literal, accounting for
     * escaped quotes.
     *
     * <p>
     * This method scans the input line from the beginning up to (but not including)
     * the specified position.
     * It toggles the {@code inString} flag each time it encounters a non-escaped
     * double quote character.
     * This helps determine whether the character at the given position falls within
     * a string.
     * </p>
     *
     * @param line
     *                 The input string to analyze.
     * @param position
     *                 The index position in the string to check.
     *
     * @return {@code true} if the position is within a string literal,
     *         {@code false} otherwise.
     */
    public static boolean isInString(String line, int position) {
        boolean inString = false;
        for (int i = 0; i <= position; i++) { // Changed <= to < to exclude current position
            if (i == position && line.charAt(i) == '"' && (i == 0 || line.charAt(i - 1) != '\\')) {
                return false;
            }
            if (line.charAt(i) == '"' && (i == 0 || line.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
        }
        return inString;
    }

    /**
     * Determines whether the specified position in a line of text is currently
     * inside a JSON array.
     *
     * <p>
     * This method traverses the line of text up to (but not including) the given position,
     * tracking array nesting depth.
     * It increments depth when encountering a non-escaped '[' character outside
     * a string, and decrements it for ']'.
     * If the resulting depth is greater than 0 at the given position, the position
     * is considered to be inside an array.
     * </p>
     *
     * @param line
     *                 The input string to analyze.
     * @param position
     *                 The index position in the string to check.
     *
     * @return {@code true} if the position is within a JSON array, {@code false}
     *         otherwise.
     *
     * @see #isInString(String, int)
     */
    public static boolean isInArray(String line, int position) {
        int depth = 0;
        for (int i = 0; i < position; i++) {
            if (line.charAt(i) == '[' && !isInString(line, i - 1))
                depth++;
            else if (line.charAt(i) == ']' && !isInString(line, i - 1))
                depth--;
        }
        return depth != 0;
    }

    public static boolean isInObject(String line, int position) {
        int depth = 0;
        for (int i = 0; i < position; i++) {
            if (line.charAt(i) == '{' && !isInString(line, i))
                depth++;
            else if (line.charAt(i) == '}' && !isInString(line, i))
                depth--;
        }
        return depth != 0;
    }

    /**
     * Checks if the given line contains at least one unquoted occurrence of the
     * target character.
     *
     * <p>
     * This method ignores characters that appear inside string literals (delimited
     * by double quotes) and only considers
     * unescaped characters outside of strings.
     * </p>
     *
     * @param line
     *               The string to search.
     * @param target
     *               The character to look for.
     *
     * @return {@code true} if the character appears outside a string,
     *         {@code false} otherwise.
     *
     * @see #countUnquotedChar(String, char)
     * @see #isInString(String, int)
     */
    public static boolean containsUnquotedChar(String line, char target) {
        return countUnquotedChar(line, target) > 0;
    }

    /**
     * Counts how many times a target character appears outside of string literals
     * in the given line.
     *
     * <p>
     * This method scans the string and increments a counter for each instance of
     * the target character that is not
     * within a quoted string. Escaped quotes are correctly ignored.
     * </p>
     *
     * @param line
     *               The {@code string} to analyze.
     * @param target
     *               The {@code char} to count.
     *
     * @return The number of unquoted occurrences of the target character.
     *
     * @see #isInString(String, int)
     */
    public static int countUnquotedChar(String line, char target) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == target && !isInString(line, i))
                count++;
        }
        return count;
    }

    /**
     * Finds the first instance of the target character outside a string literal.
     *
     * @param line
     *               The {@code string} through which to search
     * @param target
     *               The {@code char} to search for
     *
     * @return The index of the first unquoted instance of the target character, or
     *         {@code -1} if not found
     *
     * @see #isInString(String, int)
     */
    public static int findFirstUnquotedChar(String line, char target) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == target && !isInString(line, i))
                return i;
        }
        return -1;
    }

    /**
     * Splits the input string around occurrences of the given separator string,
     * ignoring separators that appear inside
     * quoted strings.
     *
     * <p>
     * This method respects string literals delimited by double quotes, treating
     * escaped quotes correctly. Trims
     * whitespace around each resulting substring.
     * </p>
     *
     * @param string
     *                  The input string to split.
     * @param separator
     *                  The substring to split on, when not within quotes.
     *
     * @return An array of trimmed substrings resulting from the split.
     *
     * @see #isInString(String, int)
     */
    public static String[] splitByUnquotedString(String string, String separator) {
        List<String> parts = new ArrayList<>();
        int lastSplitIndex = 0;

        for (int i = 0; i <= string.length() - separator.length(); i++) {
            // Check if this position contains the separator
            if (string.startsWith(separator, i)) {
                // Verify the separator is not in quotes
                if (!isInString(string, i)) {
                    // Add the part before this separator
                    parts.add(string.substring(lastSplitIndex, i).trim());
                    lastSplitIndex = i + separator.length();
                    i += separator.length() - 1; // Skip the rest of the separator
                }
            }
        }

        // Add the remaining part after the last separator
        if (lastSplitIndex <= string.length()) {
            parts.add(string.substring(lastSplitIndex).trim());
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Splits the input string around occurrences of the given separator string,
     * ignoring separators that appear inside
     * quoted strings, and limits the number of resulting substrings.
     *
     * <p>
     * Trims whitespace around each substring. Stops splitting once {@code limit}
     * parts have been collected, placing the
     * remainder into the last entry.
     * </p>
     *
     * @param string
     *                  The input string to split.
     * @param separator
     *                  The substring to split on, when not within quotes.
     * @param limit
     *                  The maximum number of substrings to return. Must be at least
     *                  1.
     *
     * @return An array of trimmed substrings resulting from the split, with at most
     *         {@code limit} entries.
     *
     * @see #isInString(String, int)
     */
    public static String[] splitByUnquotedString(String string, String separator, int limit) {
        List<String> parts = new ArrayList<>();
        int lastSplitIndex = 0;
        int count = 1; // always returns at least one string

        for (int i = 0; i <= string.length() - separator.length() && count < limit; i++) {
            // Check if this position contains the separator
            if (string.startsWith(separator, i)) {
                // Verify the separator is not in quotes
                if (!isInString(string, i)) {
                    // Add the part before this separator
                    parts.add(string.substring(lastSplitIndex, i).trim());
                    count++;
                    lastSplitIndex = i + separator.length();
                    i += separator.length() - 1; // Skip the rest of the separator
                }
            }
        }

        // Add the remaining part after the last separator
        if (lastSplitIndex <= string.length()) {
            parts.add(string.substring(lastSplitIndex).trim());
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Splits the input string around occurrences of the given separator string,
     * ignoring separators that appear within
     * array brackets <code>[...]</code>.
     *
     * <p>
     * This method treats brackets as array delimiters and avoids splitting inside
     * them, even if the separator appears.
     * Trims whitespace around each resulting substring.
     * </p>
     *
     * @param string
     *                  The input string to split.
     * @param separator
     *                  The substring to split on, when not within array brackets.
     *
     * @return An array of trimmed substrings resulting from the split.
     *
     * @see #isInArray(String, int)
     */
    public static String[] splitByStringNotInArray(String string, String separator) {
        List<String> parts = new ArrayList<>();
        int lastSplitIndex = 0;

        for (int i = 0; i <= string.length() - separator.length(); i++) {
            // Check if this position contains the separator
            if (string.startsWith(separator, i)) {
                // Verify the separator is not in brackets
                if (!isInArray(string, i)) {
                    // Add the part before this separator
                    parts.add(string.substring(lastSplitIndex, i).trim());
                    lastSplitIndex = i + separator.length();
                    i += separator.length() - 1; // Skip the rest of the separator
                }
            }
        }

        // Add the remaining part after the last separator
        if (lastSplitIndex <= string.length()) {
            parts.add(string.substring(lastSplitIndex).trim());
        }

        return parts.toArray(new String[0]);
    }

    public static String[] splitByStringNotInObject(String string, String separator) {
        List<String> parts = new ArrayList<>();
        int lastSplitIndex = 0;

        for (int i = 0; i <= string.length() - separator.length(); i++) {
            // Check if this position contains the separator
            if (string.startsWith(separator, i)) {
                // Verify the separator is not in brackets
                if (!isInObject(string, i)) {
                    // Add the part before this separator
                    parts.add(string.substring(lastSplitIndex, i).trim());
                    lastSplitIndex = i + separator.length();
                    i += separator.length() - 1; // Skip the rest of the separator
                }
            }
        }

        // Add the remaining part after the last separator
        if (lastSplitIndex <= string.length()) {
            parts.add(string.substring(lastSplitIndex).trim());
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Splits a string by a separator only when it is not inside an object or an
     * array.
     *
     * @param string String to split.
     * @param separator String use as separator where it appears outside a nested object.
     *
     * @return Array of Strings split using the separator, which will not appear in the array
     *         unless it is in a nested object.
     */
    public static String[] splitByStringNotNested(String string, String separator) {
        List<String> parts = new ArrayList<>();
        int lastSplitIndex = 0;

        for (int i = 0; i <= string.length() - separator.length(); i++) {
            // Check if this position contains the separator
            if (string.startsWith(separator, i)) {
                // Verify the separator is not in brackets
                if (!isInObject(string, i) && !isInArray(string, i) && !isInString(string, i)) {
                    // Add the part before this separator
                    parts.add(string.substring(lastSplitIndex, i).trim());
                    lastSplitIndex = i + separator.length();
                    i += separator.length() - 1; // Skip the rest of the separator
                }
            }
        }

        // Add the remaining part after the last separator
        if (lastSplitIndex <= string.length()) {
            parts.add(string.substring(lastSplitIndex).trim());
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Replaces the last occurrence of a specified regular expression with a
     * replacement string in the input text.
     * This method uses the {@link #replaceFirst(String, String, String)} method by
     * reversing the string and performing
     * the replacement on the first occurrence in the reversed string.
     *
     * @param text
     *                    The input string in which the replacement will be made.
     * @param regex
     *                    The regular expression to match for the replacement.
     * @param replacement
     *                    The string to replace the matched regex.
     *
     * @return A new string with the last occurrence of the regex replaced by the
     *         replacement string.
     *
     * @see String#replaceFirst(String, String)
     */
    public static String replaceLast(String text, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int lastStart = -1;
        int lastEnd = -1;
        while (matcher.find()) {
            lastStart = matcher.start();
            lastEnd = matcher.end();
        }
        if (lastStart != -1) {
            return text.substring(0, lastStart) + replacement + text.substring(lastEnd);
        }
        return text;
    }

    /**
     * Replaces the first occurrence of a specified regular expression with a
     * replacement string in the input text.
     * This method reverses the input string, performs the replacement on the first
     * occurrence of the regex in the reversed string,
     * and then reverses the resulting string again to produce the final output.
     *
     * @param text
     *                    The input string in which the replacement will be made.
     * @param regex
     *                    The regular expression to match for the replacement
     * @param replacement
     *                    The string to replace the matched regex
     *
     * @return A new string with the first occurrence of the regex replaced by the
     *         replacement string.
     *
     * @see String#replaceFirst(String, String)
     */
    public static String replaceFirst(String text, String regex, String replacement) {
        return text.replaceFirst(regex, replacement);
    }

    /**
     * Replaces all occurrences of a specified regular expression in the input text
     * with a replacement string.
     *
     * @param text
     *                    The input string in which the replacements will be made.
     * @param regex
     *                    The regular expression to match for the replacement.
     * @param replacement
     *                    The string to replace the matched regex
     *
     * @return A new string with all occurrences of the regex replaced by the
     *         replacement string.
     *
     * @see String#replaceAll(String, String)
     */
    public static String replaceAll(String text, String regex, String replacement) {
        return text.replaceAll(regex, replacement);
    }

    /**
     * Replaces all occurrences of a specified regular expression where it appears
     * outside a quoted string in the input text with a replacement string.
     *
     * @param text
     *                    The input string in which the replacements will be made.
     * @param regex
     *                    The regular expression to match for the replacement.
     * @param replacement
     *                    The string to replace the matched regex
     *
     * @return A new string with all occurrences of the regex outside a string
     *         replaced by the replacement string
     *
     * @see #isInString(String, int)
     */
    public static String replaceAllNotInString(String text, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            // Only replace if the match is not inside a string literal
            if (!isInString(text, start)) {
                result.append(text, lastEnd, start);
                result.append(replacement);
                lastEnd = end;
            }
        }
        result.append(text.substring(lastEnd));
        return result.toString();
    }

    /**
     * Replaces the first {@code count} occurrences of the specified regular
     * expression with the replacement string in
     * the input text.
     *
     * @param text
     *                    The input string in which the replacements will be made.
     * @param regex
     *                    The regular expression to match for the replacement.
     * @param replacement
     *                    The string to replace the matched regex.
     * @param count
     *                    The number of occurrences to replace.
     *
     * @return A new string with the first {@code count} occurrences of the regex
     *         replaced by the replacement string.
     *
     * @see #replaceFirst(String, String, String)
     */
    public static String replaceFirstCount(String text, String regex, String replacement, int count) {
        StringBuilder result = new StringBuilder(text);
        int startPos = 0; // Start position for the next search

        for (int i = 0; i < count; i++) {
            // Find the next occurrence of the regex starting from the current position
            int index = result.indexOf(regex, startPos);
            if (index == -1) {
                break; // No more matches found, stop the loop
            }

            // Replace the match with the replacement string
            result.replace(index, index + regex.length(), replacement);

            // Move the start position just after the current replacement
            startPos = index + replacement.length();
        }

        return result.toString();
    }

    /**
     * Replaces the last {@code count} occurrences of the specified regular
     * expression with the replacement string in
     * the input text.
     *
     * @param text
     *                    The input string in which the replacements will be made.
     * @param regex
     *                    The regular expression to match for the replacement.
     * @param replacement
     *                    The string to replace the matched regex.
     * @param count
     *                    The number of occurrences to replace.
     *
     * @return A new string with the last {@code count} occurrences of the regex
     *         replaced by the replacement string.
     *
     * @see #replaceLast(String, String, String)
     */
    public static String replaceLastCount(String text, String regex, String replacement, int count) {
        StringBuilder result = new StringBuilder(text);
        int startPos = text.length(); // Start from the end of the string

        for (int i = 0; i < count; i++) {
            // Find the last occurrence of the regex before the current position
            int index = result.lastIndexOf(regex, startPos - 1);
            if (index == -1) {
                break; // No more matches found, stop the loop
            }

            // Replace the match with the replacement string
            result.replace(index, index + regex.length(), replacement);

            // Move the start position just before the current replacement
            startPos = index;
        }

        return result.toString();
    }

    /**
     * Capitalizes the first letter of each word in the given text. Other letters which are
     * already capitalized will keep their case.
     * @param text String text to turn to title case.
     * @return Passed text with at least the first letter of each word capitalized.
     * @see #toTitleCase(String, boolean)
     */
    public static String toTitleCase(String text) {
        return toTitleCase(text, false);
    }

    /**
     * Capitalizes the first letter of each word in the given text, optionally skipping articles
     * like 'the', 'and', 'of', 'in', etc. Other letters which are already capitalized will keep
     * their case.
     * @param text String text to turn to title case.
     * @param ignoreArticles Whether to ignore articles when capitalizing.
     * @return Passed text with at least the first letter of each word capitalized.
     */
    public static String toTitleCase(String text, boolean ignoreArticles) {
        Set<String> ignore = Set.of(
            "a", "an", "the", "for", "and", "nor", "but", "or", "yet", "so", "at", "by", "in", "of",
            "on", "to", "up", "with", "until", "while", "as", "off", "per", "via", "that"
        );
        return toTitleCase(text, ignoreArticles ? ignore : List.of());
    }

    /**
     * Capitalizes the first letter of each word in the given text, skipping any words present
     * in the ignore list. These ignored words will be treated as case-insensitive when matching
     * to words in the given text, but a complete word in the text must match a complete word in
     * the ignore list for it to be skipped. Additionally, the first and last words of each sentence
     * or clause in the text will always be title-cased. Other letters which are already capitalized
     * will keep their case.
     * @param text String text to turn to title case.
     * @param ignore Collection of String words to ignore title casing.
     * @return Passed text with at least the first letter of each word, except those present in the
     * ignore list, capitalized.
     */
    public static String toTitleCase(String text, Collection<String> ignore) {
        StringBuilder res = new StringBuilder();
        for (String word : text.split(" ")) {
            // Determine if is first or last word in clause
            boolean isFirstOrLast = false; // TODO

            // Determine whether to ignore
            if (ignore.contains(word) && !isFirstOrLast) continue;

            // Find the first letter in the word
            int firstLetterIdx = 0;
            while (!Character.isAlphabetic(word.charAt(firstLetterIdx))) firstLetterIdx++;
            char first = word.charAt(firstLetterIdx);

            // Build the title-cased word
            String titleCased = "";
            if (firstLetterIdx != 0) {
                titleCased += word.substring(0, firstLetterIdx);
            }
            titleCased += Character.toTitleCase(first);
            if (firstLetterIdx < word.length() - 1) {
                titleCased += word.substring(firstLetterIdx + 1);
            }

            // Add to res
            res.append(titleCased).append(" ");
        }
        return res.toString();
    }

    public static String[] splitClauses(String text) {
        /*
        Split by period, question mark, exclamation mark, colon, and semicolon which is proceeded
        by a non-whitespace character, followed immediately by some whitespace and then a capital
        letter or number OR a semicolon proceeded by a non-whitespace character, followed
        immediately by some whitespace and then any non-whitespace character.
        */
        /* Test Sentence:
        This is a sentence. This is another sentence. Do questions work? Wow, they do! Here is one example of a vexing sentence to split: Dr. Jim is my dentist. Things that do work are numbers, like 12.54 and 13.2. 11 is the first token in this sentence. Now I'm thinking about ellipses... The last sentence in this paragraph is not captured; this can be disregarded for the purpose of splitting clauses.
         */
        return text.split("(?<=\\S)[.?!:;]\\s+(?=[A-Z0-9])|(?<=.);\\s+(?=\\S)");
    }

}
