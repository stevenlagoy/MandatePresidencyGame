package com.stevenlagoy.presidency.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class RandomOperations {

    private RandomOperations() {}

    /**
     * Returns {@code true} or {@code false} with equal liklihood.
     * @return {@code true} 50% of the time and {@code false} 50% of the time.
     * @see #randChance(float)
     */
    public static boolean randBool() {
        return randChance(0.5f);
    }

    /** Flips a coin and returns {@code true} if heads, {@code false} if tails. */
    public static boolean flipACoin() {
        return randBool();
    }

    /** Rolls a die with the given number of sides and returns the result. */
    public static int rollDie(int numSides) {
        return randInt(1, numSides);
    }

    /** Rolls the given number of dice with the given number of sides on each and returns an array with the results. */
    public static int[] rollDice(int numDice, int numSidesPer) {
        int[] dice = new int[numDice];
        Arrays.fill(dice, numSidesPer);
        return rollDice(dice);
    }

    /** Rolls dice with the given numbers of sides and returns an array with the results. */
    public static int[] rollDice(int[] diceSides) {
        int[] res = new int[diceSides.length];
        for (int i = 0; i < diceSides.length; i++) {
            res[i] = rollDie(diceSides[i]);
        }
        return res;
    }

    /** Rolls the given number of dice with the given number of sides and returns the sum. */
    public static int rollDiceSum(int numDice, int numSidesPer) {
        return Arrays.stream(rollDice(numDice, numSidesPer)).sum();
    }

    /** Rolls dice with the given numbers of sides and returns the sum. */
    public static int rollDiceSum(int[] diceSides) {
        return Arrays.stream(rollDice(diceSides)).sum();
    }

    /**
     * Selects a float between 0.0 and 1.0 which can be used as a percentage.
     * 
     * @return A pseudorandomly selected float in the range [0.0, 1.0)
     * @see #randPercent(float, float)
     */
    public static float randPercent() {
        return randFloat(0.0f, 1.0f);
    }

    /**
     * Selects a float between 0.0 and 1.0.
     * 
     * @return A pseudorandomly selected float in the range [0.0, 1.0)
     * @see #randFloat(float, float)
     */
    public static float randFloat() {
        return randFloat(0.0f, 1.0f);
    }

    /**
     * Selects a float between the min and the max.
     * <p>
     * <i>If min is a larger value than max, their values will be swapped.</i>
     * 
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected float in the range [min, max)
     */
    public static float randPercent(float min, float max) {
        return randFloat(min, max);
    }

    /**
     * Selects a float between the min and the max.
     * <p>
     * <i>If min is a larger value than max, their values will be swapped.</i>
     * 
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected float in the range [min, max)
     */
    public static float randFloat(float min, float max) {
        // will perform the same if min and max are flipped
        Random rand = new Random();
        // return a float between min and max (exclusive), equally distributed
        return (max - min) * rand.nextFloat() + min;
    }

    public static boolean randChance(float chance) {
        if (chance <= 0.0f) return false;
        else if (chance >= 1.0f) return true;
        else return randPercent() <= chance;
    }

    /**
     * Selects a double between the min and the max.
     * 
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly selected double in the range [min, max)
     */
    public static double randDouble(double min, double max) {
        // will perform the same if min and max are flipped
        Random rand = new Random();
        return (max - min) * rand.nextDouble() + min; // return a double between min and max (exclusive), equally
                                                      // distributed
    }

    /**
     * Selects an integer between zero and the max, evenly distributed.
     * 
     * @param max The maximum value which can be randomly selected (exclusive)
     * @return A pseudorandomly generated integer in the range [0, max)
     */
    public static int randInt(int max) {
        return randInt(0, max);
    }

    /**
     * Selects an integer between the min and the max, evenly distributed.
     * 
     * @param min The minimum value which can be selected (inclusive)
     * @param max The maximum value which can be selected (exclusive)
     * @return A pseudorandomly generated integer in the range [min, max)
     */
    public static int randInt(int min, int max) {
        if (max < min)
            throw new IllegalArgumentException(
                    String.format("The minimum is less than the maximum: %d < %d.%n", max, min));
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min; // return an integer between min and max (inclusive), equally
                                                  // distributed
    }

    /**
     * Selects one value from an array.
     * 
     * @param items The array to select from.
     * @return One randomly selected value.
     */
    public static <T> T randSelect(T[] items) {
        if (items.length == 0)
            return null;
        Random rand = new Random();
        int randomNumber = rand.nextInt(items.length);
        return items[randomNumber];
    }

    /**
     * Selects one value from a collection.
     * 
     * @param items The collection to select from.
     * @return One randomly selected value.
     */
    public static <T> T randSelect(Collection<T> items) {
        if (items == null || items.size() == 0)
            return null;

        Random rand = new Random();
        int randomNumber = rand.nextInt(items.size());
        int i = 0;
        for (T item : items) {
            if (i == randomNumber) return item;
            i++;
        }
        return null; // Never reached when items.size() > 0
    }

    /**
     * Selects a value from the items array, with the weight for selection given by
     * the weights array. The arrays must have the same length.
     * 
     * @param <T>     Object
     * @param items   Array of selectable values. Must have same length as weights
     *                array.
     * @param weights Array of weights for each value. Must have same length as
     *                items array.
     *                For any index n within the items of either array,
     *                {@code items[n]} corresponds to {@code weights[n]}.
     *                The probability that any item i will be selected is given by
     *                {@code weights[i] / sum(weights)}
     * @return One value selected from the items array, or {@code null} if
     *         unsuccessful.
     */
    public static <T> T weightedRandSelect(T[] items, double[] weights) {
        if (items.length < 1 || weights.length < 1) {
            Logger.log("INVALID SELECTION FROM EMPTY ARRAY",
                    String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        if (items.length != weights.length) {
            Logger.log("WEIGHTED SELECTION FROM MISMATCHED ARRAYS",
                    String.format("Provided arrays for a weighted selection have mismatched lengths."),
                    new Exception());
            return null;
        }

        double totalWeight = 0;
        for (double weight : weights)
            totalWeight += weight;

        Random rand = new Random();
        double randomNumber = rand.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (int i = 0; i < items.length; i++) {
            cumulativeWeight += weights[i];
            if (randomNumber < cumulativeWeight) {
                return items[i];
            }
        }
        Logger.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."),
                new Exception());
        return null; // Edge-case failure to select
    }

    /**
     * Selects a value from the items array, with the weight for selection given by
     * the weights array. The arrays must have the same length.
     * 
     * @param <T>     Object
     * @param items   Ordered collection of selectable values. Must have same size as weights array.
     * @param weights Ordered collection of weights for each value. Must have same size as items array.
     *                For any index n within the items of either collection,
     *                {@code items[n]} corresponds to {@code weights[n]}.
     *                The probability that any item i will be selected is given by
     *                {@code weights[i] / sum(weights)}
     * @return One value selected from the items collection, or {@code null} if
     *         unsuccessful.
     */
    public static <T> T weightedRandSelect(List<T> items, List<Number> weights) {
        if (items.size() < 1 || weights.size() < 1) {
            Logger.log("INVALID SELECTION FROM EMPTY ARRAY",
                    String.format("Unable to select an item from an array with length < 1."), new Exception());
            return null;
        }
        if (items.size() != weights.size()) {
            Logger.log("WEIGHTED SELECTION FROM MISMATCHED ARRAYS",
                    String.format("Provided arrays for a weighted selection have mismatched lengths."),
                    new Exception());
            return null;
        }
        double totalWeight = 0;
        for (Number weight : weights)
            totalWeight += weight.doubleValue();

        Random rand = new Random();
        double randomNumber = rand.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (int i = 0; i < items.size(); i++) {
            cumulativeWeight += weights.get(i).doubleValue();
            if (randomNumber < cumulativeWeight) {
                return items.get(i);
            }
        }
        Logger.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."),
                new Exception());
        return null; // Edge-case failure to select
    }

    /**
     * Selects a value from the items map, with the weight for selection given by
     * the item's map value.
     * 
     * @param <T>   Object
     * @param items Map of Object to Number, where the keys are Objects to select
     *              from and the values are the weights for each object.
     *              The probability that any item i will be selected is given by
     *              {@code items[i] / sum(items.values)}
     * @return One value selected from the items map, or {@code null} if
     *         unsuccessful.
     */
    public static <T> T weightedRandSelect(Map<T, ? extends Number> items) {
        if (items == null || items.isEmpty()) {
            Logger.log("INVALID SELECTION FROM EMPTY ARRAY",
                    String.format("Unable to select an item from an empty or null array."), new Exception());
            return null;
        }
        // map requires bijective relationship between keys and values

        double totalWeight = 0.0;
        for (Number weight : items.values())
            totalWeight += weight.doubleValue();
        if (totalWeight == 0.0)
            return randSelect(items.keySet());

        Random rand = new Random();
        double randomNumber = rand.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (T item : items.keySet()) {
            cumulativeWeight += items.get(item).doubleValue();
            if (randomNumber < cumulativeWeight) {
                return item;
            }
        }
        Logger.log("FAILURE TO SELECT", String.format("The weighted selection failed to select an item."),
                new Exception());
        return null; // Edge-case failure to select
    }

    public static int probabilisticCount(float f) {
        int count = 0;
        while (randPercent() <= f)
            count++;
        return count;
    }

}
