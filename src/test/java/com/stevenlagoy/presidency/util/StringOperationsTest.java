package com.stevenlagoy.presidency.util;

import org.junit.Test;
import static org.junit.Assert.*;

public final class StringOperationsTest {
    
    @Test
    public void testIsInString() {
        String line = "foo \"bar \\\"baz\\\" qux\" zot";
        assertFalse(StringOperations.isInString(line, 0)); // 'f'
        assertTrue(StringOperations.isInString(line, 7));  // inside "bar ..."
        assertFalse(StringOperations.isInString(line, line.length() - 1)); // after string
    }

    @Test
    public void testIsInArray() {
        String line = "[1, 2, [3, 4], 5]";
        assertTrue(StringOperations.isInArray(line, 5)); // inside first array
        assertTrue(StringOperations.isInArray(line, 10)); // inside nested array
        assertFalse(StringOperations.isInArray(line, line.length())); // after array
    }

    @Test
    public void testIsInObject() {
        String line = "{\"a\": 1, \"b\": {\"c\": 2}}";
        assertTrue(StringOperations.isInObject(line, 5)); // inside outer object
        assertTrue(StringOperations.isInObject(line, 18)); // inside nested object
        assertFalse(StringOperations.isInObject(line, line.length())); // after object
    }

    @Test
    public void testContainsUnquotedChar() {
        String line = "foo, \"bar, baz\", qux";
        assertTrue(StringOperations.containsUnquotedChar(line, ','));
        assertEquals(2, StringOperations.countUnquotedChar(line, ','));
    }

    @Test
    public void testFindFirstUnquotedChar() {
        String line = "foo, \"bar, baz\", qux";
        assertEquals(3, StringOperations.findFirstUnquotedChar(line, ','));
    }

    @Test
    public void testSplitByUnquotedString() {
        String line = "foo, \"bar, baz\", qux";
        String[] parts = StringOperations.splitByUnquotedString(line, ",");
        assertEquals(3, parts.length);
        assertEquals("foo", parts[0]);
        assertEquals("\"bar, baz\"", parts[1]);
        assertEquals("qux", parts[2]);
    }

    @Test
    public void testSplitByUnquotedStringWithLimit() {
        String line = "foo, \"bar, baz\", qux, zot";
        String[] parts = StringOperations.splitByUnquotedString(line, ",", 2);
        assertEquals(2, parts.length);
        assertEquals("foo", parts[0]);
        assertEquals("\"bar, baz\", qux, zot", parts[1]);
    }

    @Test
    public void testSplitByStringNotInArray() {
        String line = "foo, [bar, baz], qux";
        String[] parts = StringOperations.splitByStringNotInArray(line, ",");
        assertEquals(3, parts.length);
        assertEquals("foo", parts[0]);
        assertEquals("[bar, baz]", parts[1]);
        assertEquals("qux", parts[2]);
    }

    @Test
    public void testSplitByStringNotInObject() {
        String line = "foo, {bar: baz, qux: zot}, end";
        String[] parts = StringOperations.splitByStringNotInObject(line, ",");
        assertEquals(3, parts.length);
        assertEquals("foo", parts[0]);
        assertEquals("{bar: baz, qux: zot}", parts[1]);
        assertEquals("end", parts[2]);
    }

    @Test
    public void testSplitByStringNotNested() {
        String line = "foo, [bar, {baz, qux}], end";
        String[] parts = StringOperations.splitByStringNotNested(line, ",");
        assertEquals(3, parts.length);
        assertEquals("foo", parts[0]);
        assertEquals("[bar, {baz, qux}]", parts[1]);
        assertEquals("end", parts[2]);
    }

    @Test
    public void testReplaceLast() {
        String text = "foo bar baz bar";
        String result = StringOperations.replaceLast(text, "bar", "qux");
        assertEquals("foo bar baz qux", result);
    }

    @Test
    public void testReplaceFirst() {
        String text = "foo bar baz bar";
        String result = StringOperations.replaceFirst(text, "bar", "qux");
        assertEquals("foo qux baz bar", result);
    }

    @Test
    public void testReplaceAll() {
        String text = "foo bar baz bar";
        String result = StringOperations.replaceAll(text, "bar", "qux");
        assertEquals("foo qux baz qux", result);
    }

    @Test
    public void testReplaceAllNotInString() {
        String text = "foo bar \"bar\" bar";
        String result = StringOperations.replaceAllNotInString(text, "bar", "qux");
        assertEquals("foo qux \"bar\" qux", result);
    }

    @Test
    public void testReplaceFirstCount() {
        String text = "foo bar bar bar";
        String result = StringOperations.replaceFirstCount(text, "bar", "qux", 2);
        assertEquals("foo qux qux bar", result);
    }

    @Test
    public void testReplaceLastCount() {
        String text = "foo bar bar bar";
        String result = StringOperations.replaceLastCount(text, "bar", "qux", 2);
        assertEquals("foo bar qux qux", result);
    }
}