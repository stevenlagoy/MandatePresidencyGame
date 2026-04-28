package com.stevenlagoy.presidency.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StringUtilsTest {

    @Test
    fun `WHEN position inside quotes THEN returns true`() {
        val s = """abc "hello" def"""
        val pos = s.indexOf("e")
        assertTrue(s.isInString(pos))
    }

    @Test
    fun `WHEN position outside quotes THEN returns false`() {
        val s = """abc "hello" def"""
        val pos = s.indexOf("a")
        assertFalse(s.isInString(pos))
    }

    @Test
    fun `WHEN inside array THEN returns true`() {
        val s = "a [1, 2, 3] b"
        val pos = s.indexOf("2")
        assertTrue(s.isInArray(pos))
    }

    @Test
    fun `WHEN outside array THEN returns false`() {
        val s = "a [1, 2, 3] b"
        val pos = s.indexOf("b")
        assertFalse(s.isInArray(pos))
    }

    @Test
    fun `WHEN nested objects THEN depth handled correctly`() {
        val s = "{ a: { b: 1 } }"
        val pos = s.indexOf("b")
        assertTrue(s.isInObject(pos))
    }

    @Test
    fun `WHEN contains unquoted char THEN true`() {
        val s = """a,b,"c,d""""
        assertTrue(s.containsUnquotedChar(','))
    }

    @Test
    fun `WHEN char only in quotes THEN containsUnquotedChar false`() {
        val s = """"a,b,c""""
        assertFalse(s.containsUnquotedChar(','))
    }

    @Test
    fun `WHEN counting unquoted chars THEN correct count`() {
        val s = """a,b,"c,d",e"""
        assertEquals(3, s.countUnquotedChar(','))
    }

    @Test
    fun `WHEN find first unquoted char THEN correct index`() {
        val s = """a,"b,c",d"""
        val idx = s.findFirstUnquotedChar(',')
        assertEquals(s.indexOf(','), idx)
    }

    @Test
    fun `WHEN splitByUnquotedString THEN ignores quoted separators`() {
        val s = """a,b,"c,d",e"""
        val parts = s.splitByUnquotedString(",")
        assertEquals(listOf("a", "b", "\"c,d\"", "e"), parts)
    }

    @Test
    fun `WHEN splitByStringNotInArray THEN ignores array separators`() {
        val s = "a,[b,c],d"
        val parts = s.splitByStringNotInArray(",")
        assertEquals(listOf("a", "[b,c]", "d"), parts)
    }

    @Test
    fun `WHEN splitByStringNotInObject THEN ignores object separators`() {
        val s = "a,{b:c,d:e},f"
        val parts = s.splitByStringNotInObject(",")
        assertEquals(listOf("a", "{b:c,d:e}", "f"), parts)
    }

    @Test
    fun `WHEN split with limit THEN respects limit`() {
        val s = "a,b,c,d"
        val parts = s.splitByUnquotedString(",", limit = 2)
        assertEquals(listOf("a", "b,c,d"), parts)
    }

    @Test
    fun `WHEN replaceLast THEN only last match replaced`() {
        val s = "a1b1c1"
        val result = s.replaceLast("\\d", "X")
        assertEquals("a1b1cX", result)
    }

    @Test
    fun `WHEN replaceAllRegex THEN replaces all`() {
        val s = "a1b2c3"
        val result = s.replaceAllRegex("\\d", "X")
        assertEquals("aXbXcX", result)
    }

    @Test
    fun `WHEN replaceAllNotInString THEN ignores quoted matches`() {
        val s = """a1 "b2" c3"""
        val result = s.replaceAllNotInString("\\d", "X")
        assertEquals("""aX "b2" cX""", result)
    }

    @Test
    fun `WHEN titlecase simple THEN capitalizes words`() {
        val s = "hello world"
        assertEquals("Hello World", s.titlecase())
    }

    @Test
    fun `WHEN titlecase ignoring articles THEN skips inner articles`() {
        val s = "the lord of the rings"
        val result = s.titlecase(true)
        assertEquals("The Lord of the Rings", result)
    }

    @Test
    fun `WHEN titlecase preserves punctuation prefix THEN correct`() {
        val s = "\"hello world\""
        val result = s.titlecase()
        assertEquals("\"Hello World\"", result)
    }

    @Test
    fun `WHEN splitClauses THEN splits on punctuation`() {
        val s = "Hello world. This is a test! Another sentence?"
        val parts = s.splitClauses()
        assertEquals(3, parts.size)
    }

    @Test
    fun `WHEN splitClauses avoids decimals THEN does not split`() {
        val s = "The value is 3.14 and rising."
        val parts = s.splitClauses()
        assertEquals(1, parts.size)
    }

    @Test
    fun `WHEN empty string THEN functions behave safely`() {
        val s = ""
        assertFalse(s.containsUnquotedChar(','))
        assertEquals(0, s.countUnquotedChar(','))
        assertEquals(-1, s.findFirstUnquotedChar(','))
        assertEquals(listOf(""), s.splitByUnquotedString(","))
    }

    @Test
    fun `WHEN no matches in replaceLast THEN returns original`() {
        val s = "abc"
        assertEquals(s, s.replaceLast("\\d", "X"))
    }
}
