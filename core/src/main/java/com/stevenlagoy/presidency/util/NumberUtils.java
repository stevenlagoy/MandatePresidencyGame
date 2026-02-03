package com.stevenlagoy.presidency.util;

import java.util.HashMap;
import java.util.Map;

public final class NumberUtils {

    private NumberUtils() {
    }

    private static Map<Integer, Boolean> primeCache = new HashMap<>();

    public static boolean isPrime(int value) {
        if (value <= 1)
            return false;
        if (primeCache.containsKey(value))
            return primeCache.get(value);

        for (int i = 2; i <= Math.sqrt(value); i++) {
            if (value % i == 0) {
                primeCache.put(value, false);
                return false;
            }
        }
        primeCache.put(value, true);
        return true;
    }

    /**
     * Return the smallest prime value greater the passed value
     * 
     * @param value
     * @return Prime value greater than value
     * @see #isPrime(int)
     */
    public static int nextPrime(int value) {
        if (value < 1)
            return 1;
        else if (value == 1)
            return 2;
        while (!isPrime(++value)) {
        }
        return value;
    }

    /** Suffixes to place after numbers in ordinal form. */
    static String[] suffixes = { "th", "st", "nd", "rd", "th" };

    /**
     * Takes an int value and returns a String for the ordinal form of that number.
     * <p>
     * Example: toOrdinal(1) -> "1st", toOrdinal(2) -> "2nd", toOrdinal(5) -> "5th"
     * 
     * @param value
     * @return The ordinal form of the value
     */
    public static String toOrdinal(int value) {
        int index;
        switch (Math.abs(value) % 100) {
            case 11:
            case 12:
            case 13:
                index = 0;
                break;
            default:
                index = Math.abs(value) % 10 <= 3 ? Math.abs(value) % 10 : 4;
        }
        return (value < 0 ? "negative " : "") + String.valueOf(Math.abs(value)) + suffixes[index];
    }
}
