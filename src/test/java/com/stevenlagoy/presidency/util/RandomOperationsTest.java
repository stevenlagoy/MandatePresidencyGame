package com.stevenlagoy.presidency.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public final class RandomOperationsTest {

    @Test
    public void testRandomIntRange() {
        int min = 1, max = 10;
        for (int i = 0; i < 100; i++) {
            int val = RandomOperations.randInt(min, max);
            assertTrue(val >= min && val <= max);
        }
    }

    @Test
    public void testRandomFloatRange() {
        float min = 0.5f, max = 2.5f;
        for (int i = 0; i < 100; i++) {
            float val = RandomOperations.randPercent(min, max);
            assertTrue(val >= min && val <= max);
        }
    }

    @Test
    public void testRandomBoolean() {
        boolean foundTrue = false, foundFalse = false;
        for (int i = 0; i < 100; i++) {
            boolean val = RandomOperations.randBool();
            if (val) foundTrue = true;
            else foundFalse = true;
        }
        assertTrue(foundTrue && foundFalse);
    }

    @Test
    public void testRandomElementFromList() {
        List<String> list = Arrays.asList("a", "b", "c");
        Set<String> found = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String val = RandomOperations.randSelect(list);
            assertTrue(list.contains(val));
            found.add(val);
        }
        assertEquals(new HashSet<>(list), found);
    }

    @Test
    public void testRandomElementFromArray() {
        String[] arr = {"x", "y", "z"};
        Set<String> found = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String val = RandomOperations.randSelect(arr);
            assertTrue(Arrays.asList(arr).contains(val));
            found.add(val);
        }
        assertEquals(new HashSet<>(Arrays.asList(arr)), found);
    }

    @Test
    public void testRandomChance() {
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            if (RandomOperations.randChance(0.2f)) count++;
        }
        // Should be roughly 20% (allowing some variance)
        assertTrue(count > 100 && count < 300);
    }

    @Test
    public void testRollDieAndDice() {
        for (int i = 0; i < 100; i++) {
            int val = RandomOperations.rollDie(6);
            assertTrue(val >= 1 && val <= 6);

            int[] dice = RandomOperations.rollDice(3, 6);
            assertEquals(3, dice.length);
            for (int d : dice) {
                assertTrue(d >= 1 && d <= 6);
            }

            int[] customDice = RandomOperations.rollDice(new int[]{4, 8, 10});
            assertEquals(3, customDice.length);
            assertTrue(customDice[0] >= 1 && customDice[0] <= 4);
            assertTrue(customDice[1] >= 1 && customDice[1] <= 8);
            assertTrue(customDice[2] >= 1 && customDice[2] <= 10);
        }
    }

    @Test
    public void testRollDiceSum() {
        int sum = RandomOperations.rollDiceSum(2, 6);
        assertTrue(sum >= 2 && sum <= 12);

        int sum2 = RandomOperations.rollDiceSum(new int[]{4, 4, 4});
        assertTrue(sum2 >= 3 && sum2 <= 12);
    }

    @Test
    public void testRandDouble() {
        for (int i = 0; i < 100; i++) {
            double val = RandomOperations.randDouble(1.0, 2.0);
            assertTrue(val >= 1.0 && val < 2.0);
        }
    }

    @Test
    public void testWeightedRandSelectArray() {
        String[] arr = {"a", "b", "c"};
        double[] weights = {0.1, 0.2, 0.7};
        Set<String> found = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String val = RandomOperations.weightedRandSelect(arr, weights);
            assertTrue(Arrays.asList(arr).contains(val));
            found.add(val);
        }
        assertTrue(found.contains("a") && found.contains("b") && found.contains("c"));
    }

    @Test
    public void testWeightedRandSelectCollection() {
        List<String> list = Arrays.asList("x", "y", "z");
        List<Number> weights = Arrays.asList(0.5, 0.3, 0.2);
        Set<String> found = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String val = RandomOperations.weightedRandSelect(list, weights);
            assertTrue(list.contains(val));
            found.add(val);
        }
        assertTrue(found.contains("x") && found.contains("y") && found.contains("z"));
    }

    @Test
    public void testWeightedRandSelectMap() {
        Map<String, Number> map = new HashMap<>();
        map.put("foo", 0.1);
        map.put("bar", 0.2);
        map.put("baz", 0.7);
        Set<String> found = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String val = RandomOperations.weightedRandSelect(map);
            assertTrue(map.containsKey(val));
            found.add(val);
        }
        assertTrue(found.contains("foo") && found.contains("bar") && found.contains("baz"));
    }

    @Test
    public void testProbabilisticCount() {
        int total = 0;
        for (int i = 0; i < 100; i++) {
            int count = RandomOperations.probabilisticCount(0.2f);
            assertTrue(count >= 0);
            total += count;
        }
        // Should not always be zero or always be huge
        assertTrue(total > 0 && total < 1000);
    }

    @Test
    public void testFlipACoin() {
        boolean foundHeads = false, foundTails = false;
        for (int i = 0; i < 100; i++) {
            boolean val = RandomOperations.flipACoin();
            if (val) foundHeads = true;
            else foundTails = true;
        }
        assertTrue(foundHeads && foundTails);
    }
}