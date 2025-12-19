package com.stevenlagoy.presidency.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CollectionOperationsTest {
    
    @Test
    public void testCombinationsEmpty() {
        Set<Set<String>> result = CollectionOperations.combinations(Collections.emptySet());
        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.emptySet()));
    }

    @Test
    public void testCombinationsSingleElement() {
        Set<String> input = new HashSet<>(Arrays.asList("A"));
        Set<Set<String>> result = CollectionOperations.combinations(input);
        assertEquals(2, result.size());
        assertTrue(result.contains(Collections.emptySet()));
        assertTrue(result.contains(new HashSet<>(Arrays.asList("A"))));
    }

    @Test
    public void testCombinationsTwoElements() {
        Set<String> input = new HashSet<>(Arrays.asList("A", "B"));
        Set<Set<String>> result = CollectionOperations.combinations(input);
        assertEquals(4, result.size());
        assertTrue(result.contains(Collections.emptySet()));
        assertTrue(result.contains(new HashSet<>(Arrays.asList("A"))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList("B"))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList("A", "B"))));
    }

    @Test
    public void testCombinationsThreeElements() {
        Set<Integer> input = new HashSet<>(Arrays.asList(1, 2, 3));
        Set<Set<Integer>> result = CollectionOperations.combinations(input);
        assertEquals(8, result.size());
        assertTrue(result.contains(Collections.emptySet()));
        assertTrue(result.contains(new HashSet<>(Arrays.asList(1))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList(2))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList(3))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList(1, 2))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList(1, 3))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList(2, 3))));
        assertTrue(result.contains(new HashSet<>(Arrays.asList(1, 2, 3))));
    }

    @Test(expected = NullPointerException.class)
    public void testCombinationsNullInput() {
        CollectionOperations.combinations(null);
    }

    @Test
    public void testCombinationsWithNullElement() {
        Set<String> input = new HashSet<>(Arrays.asList("A", null));
        CollectionOperations.combinations(input);
    }

}
