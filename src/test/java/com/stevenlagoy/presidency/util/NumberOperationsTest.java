package com.stevenlagoy.presidency.util;

import org.junit.Test;
import static org.junit.Assert.*;

public final class NumberOperationsTest {
    
    @Test
    public void testIsPrime() {
        assertFalse(NumberOperations.isPrime(-5));
        assertFalse(NumberOperations.isPrime(0));
        assertFalse(NumberOperations.isPrime(1));
        assertTrue(NumberOperations.isPrime(2));
        assertTrue(NumberOperations.isPrime(3));
        assertFalse(NumberOperations.isPrime(4));
        assertTrue(NumberOperations.isPrime(5));
        assertFalse(NumberOperations.isPrime(9));
        assertTrue(NumberOperations.isPrime(13));
        assertFalse(NumberOperations.isPrime(15));
        assertTrue(NumberOperations.isPrime(97));
    }

    @Test
    public void testNextPrime() {
        assertEquals(1, NumberOperations.nextPrime(-10));
        assertEquals(2, NumberOperations.nextPrime(1));
        assertEquals(3, NumberOperations.nextPrime(2));
        assertEquals(5, NumberOperations.nextPrime(4));
        assertEquals(7, NumberOperations.nextPrime(6));
        assertEquals(11, NumberOperations.nextPrime(10));
        assertEquals(13, NumberOperations.nextPrime(12));
        assertEquals(7919, NumberOperations.nextPrime(7907));
    }

    @Test
    public void testToOrdinal() {
        assertEquals("1st", NumberOperations.toOrdinal(1));
        assertEquals("2nd", NumberOperations.toOrdinal(2));
        assertEquals("3rd", NumberOperations.toOrdinal(3));
        assertEquals("4th", NumberOperations.toOrdinal(4));
        assertEquals("11th", NumberOperations.toOrdinal(11));
        assertEquals("12th", NumberOperations.toOrdinal(12));
        assertEquals("13th", NumberOperations.toOrdinal(13));
        assertEquals("21st", NumberOperations.toOrdinal(21));
        assertEquals("22nd", NumberOperations.toOrdinal(22));
        assertEquals("23rd", NumberOperations.toOrdinal(23));
        assertEquals("24th", NumberOperations.toOrdinal(24));
        assertEquals("100th", NumberOperations.toOrdinal(100));
        assertEquals("101st", NumberOperations.toOrdinal(101));
        assertEquals("negative 1st", NumberOperations.toOrdinal(-1));
        assertEquals("negative 12th", NumberOperations.toOrdinal(-12));
    }

}
